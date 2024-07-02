package loqor.ait.tardis.control.impl;

import loqor.ait.tardis.Tardis;
import loqor.ait.tardis.control.Control;
import loqor.ait.tardis.data.travel.TravelHandlerBase;
import loqor.ait.tardis.util.TardisUtil;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class AutoPilotControl extends Control {

	public AutoPilotControl() {
		// ☸ ?
		super("protocol_116");
	}

	@Override
	public boolean runServer(Tardis tardis, ServerPlayerEntity player, ServerWorld world, BlockPos console) {
		if (tardis.sequence().hasActiveSequence() && tardis.sequence().controlPartOfSequence(this)) {
			this.addToControlSequence(tardis, player, console);
			return false;
		}

		// @TODO make a real world flight control.. later
		if (player.isSneaking() && tardis.travel().getState() == TravelHandlerBase.State.LANDED) {
			if (tardis.door().isOpen()) {
				world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_CHAIN_FALL, SoundCategory.BLOCKS, 1.0F, 1.0F);
				return true;
			} else {
				world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_CLUSTER_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
			}

			TardisUtil.teleportOutside(tardis, player);

			player.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 1, false, false, false));
			RealTardisEntity.spawnFromTardisId(tardis.travel().position().getWorld(), tardis.getUuid(), tardis.travel().position().getPos(), player, player.getBlockPos());
			return true;
		}

		boolean autopilot = tardis.travel().autopilot();
		tardis.travel().autopilot(!autopilot);
		return true;
	}

	@Override
	public SoundEvent getSound() {
		return SoundEvents.BLOCK_LEVER_CLICK;
	}
}
