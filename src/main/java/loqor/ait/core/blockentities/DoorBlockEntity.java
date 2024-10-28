package loqor.ait.core.blockentities;

import java.util.Objects;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import loqor.ait.api.link.LinkableItem;
import loqor.ait.api.link.v2.block.InteriorLinkableBlockEntity;
import loqor.ait.compat.DependencyChecker;
import loqor.ait.core.AITBlockEntityTypes;
import loqor.ait.core.blocks.DoorBlock;
import loqor.ait.core.blocks.ExteriorBlock;
import loqor.ait.core.blocks.types.HorizontalDirectionalBlock;
import loqor.ait.core.item.KeyItem;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.dim.TardisDimension;
import loqor.ait.core.tardis.handler.DoorHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandler;
import loqor.ait.core.tardis.handler.travel.TravelHandlerBase;
import loqor.ait.core.tardis.util.TardisUtil;
import loqor.ait.data.DirectedBlockPos;
import loqor.ait.data.DirectedGlobalPos;

public class DoorBlockEntity extends InteriorLinkableBlockEntity {

    public DoorBlockEntity(BlockPos pos, BlockState state) {
        super(AITBlockEntityTypes.DOOR_BLOCK_ENTITY_TYPE, pos, state);
    }

    public static <T extends BlockEntity> void tick(World world, BlockPos pos, BlockState blockState, T tDoor) {
        if (Objects.equals(world.getRegistryKey(), World.OVERWORLD))
            return;

        DoorBlockEntity door = (DoorBlockEntity) tDoor;

        if (!door.isLinked())
            return;

        Tardis tardis = door.tardis().get();
        DirectedGlobalPos.Cached globalExteriorPos = tardis.travel().position();

        if (world.isClient())
            return;

        BlockPos exteriorPos = globalExteriorPos.getPos();
        World exteriorWorld = globalExteriorPos.getWorld();

        if (exteriorWorld == null || exteriorPos == null)
            return;

        if (blockState.getBlock() instanceof DoorBlock && !tardis.areShieldsActive()) {
            boolean waterlogged = blockState.get(Properties.WATERLOGGED);
            if (waterlogged && world.getServer().getTicks() % 20 == 0 && world.getRandom().nextBoolean()) {
                for (ServerPlayerEntity player : TardisUtil.getPlayersInsideInterior(tardis.asServer())) {
                    tardis.loyalty().subLevel(player, 1);
                }
            }
        }

        // woopsie daisy i forgor to put this here lelelelel
        if (exteriorWorld.getBlockState(exteriorPos).getBlock() instanceof ExteriorBlock
                && !tardis.areShieldsActive()) {
            boolean waterlogged = exteriorWorld.getBlockState(exteriorPos).get(Properties.WATERLOGGED);
            world.setBlockState(pos, blockState.with(Properties.WATERLOGGED, waterlogged && tardis.door().isOpen()),
                    Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);

            world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
            world.scheduleFluidTick(pos, blockState.getFluidState().getFluid(),
                    blockState.getFluidState().getFluid().getTickRate(world));
        }
    }

    public void useOn(World world, boolean sneaking, PlayerEntity player) {
        if (player == null || this.tardis().isEmpty())
            return;

        Tardis tardis = this.tardis().get();

        if (tardis.isGrowth() && tardis.hasGrowthExterior())
            return;

        if (player.getMainHandStack().getItem() instanceof KeyItem && !tardis.siege().isActive()) {
            ItemStack key = player.getMainHandStack();
            UUID keyId = LinkableItem.getTardisIdFromUuid(key, "tardis");

            if (Objects.equals(tardis.getUuid(), keyId)) {
                DoorHandler.toggleLock(tardis, (ServerPlayerEntity) player);
            } else {
                world.playSound(null, pos, SoundEvents.BLOCK_NOTE_BLOCK_BIT.value(), SoundCategory.BLOCKS, 1F, 0.2F);
                player.sendMessage(Text.translatable("tardis.key.identity_error"), true); // TARDIS does not identify
                                                                                            // with key
            }

            return;
        }

        if (tardis.travel().isLanded() || tardis.travel().inFlight())
            DoorHandler.useDoor(tardis, (ServerWorld) world, this.getPos(), (ServerPlayerEntity) player);
    }

    public Direction getFacing() {
        return this.getCachedState().get(HorizontalDirectionalBlock.FACING);
    }

    @Nullable @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public void onEntityCollision(Entity entity) {
        if (!TardisDimension.isTardisDimension((ServerWorld) this.getWorld()))
            return;

        if (!this.isLinked())
            return;

        Tardis tardis = this.tardis().get();

        if (tardis.door().isClosed())
            return;

        if (tardis.getLockedTardis())
            return;

        if (tardis.flight().falling().get())
            return;

        if (DependencyChecker.hasPortals() && tardis.getExterior().getVariant().hasPortals())
            return;

        TravelHandler travel = tardis.travel();

        if (travel.getState() == TravelHandlerBase.State.FLIGHT) {
            TardisUtil.dropOutside(tardis, entity);
            return;
        }

        if (travel.getState() != TravelHandlerBase.State.LANDED)
            return;

        TardisUtil.teleportOutside(tardis, entity);
    }

    @Override
    public void onLinked() {

        if (Objects.equals(this.getWorld().getRegistryKey(), World.OVERWORLD))
            return;

        if (this.tardis().isEmpty())
            return;

        this.tardis().get().getDesktop().setInteriorDoorPos(
                DirectedBlockPos.create(this.pos, (byte) RotationPropertyHelper.fromDirection(this.getFacing())));
    }

    public void onBreak() {
        if (!this.isLinked() || this.tardis().isEmpty()) return;

        this.tardis().get().door().closeDoors();
    }
}
