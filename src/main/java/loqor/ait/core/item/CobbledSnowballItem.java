package loqor.ait.core.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import loqor.ait.core.AITEntityTypes;
import loqor.ait.core.entities.CobbledSnowballEntity;

public class CobbledSnowballItem extends Item {
    public CobbledSnowballItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.1f);
        if (!world.isClient) {
            CobbledSnowballEntity cobbledSnowballEntity = new CobbledSnowballEntity(AITEntityTypes.COBBLED_SNOWBALL_TYPE, world);
            cobbledSnowballEntity.setOwner(user);
            cobbledSnowballEntity.setPos(user.getX(), user.getEyeY() - (double)0.1f, user.getZ());
            cobbledSnowballEntity.setItem(itemStack);
            cobbledSnowballEntity.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
            world.spawnEntity(cobbledSnowballEntity);
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }
}