package loqor.ait.client.models.exteriors;

import static loqor.ait.core.tardis.animation.ExteriorAnimation.*;

import java.util.function.Function;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import loqor.ait.AITMod;
import loqor.ait.core.blockentities.ExteriorBlockEntity;
import loqor.ait.core.effects.ZeitonHighEffect;
import loqor.ait.core.entities.FallingTardisEntity;
import loqor.ait.core.tardis.Tardis;
import loqor.ait.core.tardis.handler.DoorHandler;
import loqor.ait.data.Loyalty;

@SuppressWarnings("rawtypes")
public abstract class ExteriorModel extends SinglePartEntityModel {

    public ExteriorModel() {
        this(RenderLayer::getEntityCutoutNoCull);
    }

    public ExteriorModel(Function<Identifier, RenderLayer> function) {
        super(function);
    }

    public void animateBlockEntity(ExteriorBlockEntity exterior) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);

        if (AITMod.CONFIG.CLIENT.ANIMATE_DOORS)
            this.updateAnimation(exterior.DOOR_STATE, this.getAnimationForDoorState(
                    exterior.prevAnimState), exterior.animationTimer);
    }

    public void renderWithAnimations(ExteriorBlockEntity exterior, ModelPart root, MatrixStack matrices,
            VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        Tardis tardis = exterior.tardis().get();

        if (tardis == null)
            return;

        float newAlpha = alpha;

        if (tardis.cloak().cloaked().get() && !ZeitonHighEffect.isHigh(MinecraftClient.getInstance().player)) {
            PlayerEntity player = MinecraftClient.getInstance().player;

            if (!(tardis.loyalty().get(player).isOf(Loyalty.Type.COMPANION))) {
                newAlpha = 0f;

                root.render(matrices, vertices, light, overlay, red, green, blue, newAlpha);
                return;
            }

            if (isNearTardis(MinecraftClient.getInstance().player, tardis, MAX_CLOAK_DISTANCE)) {
                newAlpha = 1f - (float) (distanceFromTardis(player, tardis) / MAX_CLOAK_DISTANCE);

                if (alpha != 0.105f)
                    newAlpha = newAlpha * alpha;
            } else {
                newAlpha = 0f;
            }
        }

        root.render(matrices, vertices, light, overlay, red, green, blue, newAlpha);
    }

    public void renderFalling(FallingTardisEntity falling, ModelPart root, MatrixStack matrices,
            VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        root.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(Entity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw,
            float headPitch) {
    }

    public abstract Animation getAnimationForDoorState(DoorHandler.AnimationDoorState state);
}
