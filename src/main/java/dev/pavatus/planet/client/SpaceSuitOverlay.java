package dev.pavatus.planet.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.pavatus.config.AITConfig;
import dev.pavatus.planet.core.item.SpacesuitItem;
import dev.pavatus.planet.core.planet.Planet;
import dev.pavatus.planet.core.planet.PlanetRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.RotationAxis;

import loqor.ait.AITMod;
import loqor.ait.core.AITItems;
import loqor.ait.core.AITTags;
import loqor.ait.core.blockentities.FabricatorBlockEntity;
import loqor.ait.core.blocks.FabricatorBlock;
import loqor.ait.core.item.SonicItem;

public class SpaceSuitOverlay implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, float v) {
        MinecraftClient mc = MinecraftClient.getInstance();
        MatrixStack stack = drawContext.getMatrices();

        if (mc.player == null || mc.world == null)
            return;

        Planet planet = PlanetRegistry.getInstance().get(mc.world);

        if (!mc.options.getPerspective().isFirstPerson())
            return;

        TextRenderer textRenderer = mc.textRenderer;

        if (mc.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            Block block = mc.player.getWorld().getBlockState(((BlockHitResult) mc.crosshairTarget).getBlockPos())
                    .getBlock();
            if (block instanceof FabricatorBlock) {
                BlockEntity entity = mc.player.getWorld().getBlockEntity(((BlockHitResult) mc.crosshairTarget).getBlockPos());
                if (entity instanceof FabricatorBlockEntity fabricatorBlockEntity) {
                    stack.push();
                    stack.translate((drawContext.getScaledWindowWidth() / 2), (drawContext.getScaledWindowHeight() / 2), -20);
                    stack.scale(0.75f, 0.75f, 0.75f);
                    stack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(((float) mc.player.age / 200.0f) * 360f));
                    stack.translate(-((float) 83 / 2), -((float) 83 / 2), 0);
                    RenderSystem.disableDepthTest();
                    RenderSystem.depthMask(false);
                    drawContext.setShaderColor(1.0F, 1.0F, 1.0F, 0.8f);
                    RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR,
                            GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE,
                            GlStateManager.DstFactor.ZERO);
                    drawContext.drawTexture(AITMod.id("textures/gui/tardis/monitor/security_menu.png"), 0,
                            0, 0, 0, 138, 83, 83, 256, 256);
                    RenderSystem.setShaderColor(1, 1, 1, 1);
                    RenderSystem.defaultBlendFunc();
                    RenderSystem.depthMask(true);
                    RenderSystem.enableDepthTest();
                    stack.pop();
                    ItemStack fabricatorItemStack = fabricatorBlockEntity.getShowcaseStack();
                    float centerX = (float) drawContext.getScaledWindowWidth() / 2 - 8f;
                    float centerY = (float) drawContext.getScaledWindowHeight() / 2 - 8f;
                    double angleStep = 2 * Math.PI / fabricatorItemStack.getCount();

                    for (int i = 0; i < fabricatorItemStack.getCount(); i++) {
                        double angle = i * angleStep - Math.PI / 2; // Start from the top
                        int x = (int) (centerX + Math.cos(angle) * 32);
                        int y = (int) (centerY + Math.sin(angle) * 32);

                        stack.push();
                        stack.translate(x, y, -10);
                        RenderSystem.setShaderColor(0, 0, 0, 0.5f); // Set shadow color
                        stack.push();
                        stack.translate(0, 0, -12);
                        drawContext.drawItem(fabricatorItemStack, 1, 1); // Draw shadow
                        stack.pop();
                        RenderSystem.setShaderColor(1, 1, 1, 1); // Reset color
                        drawContext.drawItem(fabricatorItemStack, 0, 0); // Draw item
                        stack.pop();
                    }
                }
            }
        }

        if (planet != null &&mc.player.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof SpacesuitItem) {
            stack.push();
            stack.scale(1.5f, 1.5f, 1.5f);

            drawContext.drawTextWithShadow(textRenderer,
                    Text.literal(this.getTemperatureType(AITMod.CONFIG, planet)),
                    0, 0, 0xFFFFFF);

            stack.pop();
            stack.push();
            stack.scale(1.5f, 1.5f, 1.5f);
            String oxygen = "" + Planet.getOxygenInTank(mc.player);
            drawContext.drawTextWithShadow(textRenderer, Text.literal(
                    oxygen.substring(0, 3) + "L / " + SpacesuitItem.MAX_OXYGEN + "L"), 0, 50, 0xFFFFFF);
            stack.pop();
        }

        // TODO move this to a separate class OR generalize this class
        if ((mc.player.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == AITItems.SONIC_SCREWDRIVER
                || mc.player.getEquippedStack(EquipmentSlot.OFFHAND).getItem() == AITItems.SONIC_SCREWDRIVER)
                && playerIsLookingAtSonicInteractable(mc.crosshairTarget, mc.player)) {
            this.renderOverlay(drawContext,
                    AITMod.id("textures/gui/overlay/sonic_can_interact.png"), 1.0F);
        }
    }

    private boolean playerIsLookingAtSonicInteractable(HitResult crosshairTarget, PlayerEntity player) {
        if (player != null) {
            if (player.getMainHandStack().getItem() instanceof SonicItem) {
                ItemStack sonic = player.getMainHandStack();
                if (sonic == null)
                    return false;
                NbtCompound nbt = sonic.getOrCreateNbt();
                if (!nbt.contains(SonicItem.FUEL_KEY))
                    return false;
                if (crosshairTarget.getType() == HitResult.Type.BLOCK) {
                    Block block = player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos())
                            .getBlock();
                    return !(block instanceof AirBlock) && nbt.getDouble(SonicItem.FUEL_KEY) > 0
                            && player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos())
                                    .isIn(AITTags.Blocks.SONIC_INTERACTABLE);
                }
            } else if (player.getOffHandStack().getItem() instanceof SonicItem) {
                ItemStack sonic = player.getOffHandStack();
                if (sonic == null)
                    return false;
                NbtCompound nbt = sonic.getOrCreateNbt();
                if (!nbt.contains(SonicItem.FUEL_KEY))
                    return false;
                if (crosshairTarget.getType() == HitResult.Type.BLOCK) {
                    Block block = player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos())
                            .getBlock();
                    return !(block instanceof AirBlock) && nbt.getDouble(SonicItem.FUEL_KEY) > 0
                            && player.getWorld().getBlockState(((BlockHitResult) crosshairTarget).getBlockPos())
                                    .isIn(AITTags.Blocks.SONIC_INTERACTABLE);
                }
            }
        }
        return false;
    }

    private void renderOverlay(DrawContext context, Identifier texture, float opacity) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        context.setShaderColor(1.0F, 1.0F, 1.0F, opacity);
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.ONE_MINUS_DST_COLOR,
                GlStateManager.DstFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SrcFactor.ONE,
                GlStateManager.DstFactor.ZERO);
        context.drawTexture(texture, (context.getScaledWindowWidth() / 2) - 8,
                (context.getScaledWindowHeight() / 2) - 8, 0, 0.0F, 0.0F, 16, 16, 16, 16);
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        context.setShaderColor(0.9F, 0.9F, 0.9F, 1.0F);
    }

    public String getTemperatureType(AITConfig config, Planet planet) {
        return switch(config.CLIENT.TEMPERATURE_TYPE) {
            case CELCIUS -> ("" + planet.fahrenheit()).substring(0, 5) + "°C";
            case FAHRENHEIT -> ("" + planet.fahrenheit()).substring(0, 5) + "°F";
            case KELVIN -> planet.kelvin() + "K";
        };
    }
}
