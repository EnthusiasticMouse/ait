package loqor.ait.core.item.blueprint;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import loqor.ait.AITMod;

public class BlueprintItem extends Item {

    public BlueprintItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putString("Blueprint", BlueprintRegistry.getInstance().getRandom().id().toString());
        return stack;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        BlueprintSchema blueprint = getSchema(stack);
        if (blueprint == null) return;

        tooltip.add(Text.translatable("ait.blueprint.tooltip").formatted(Formatting.BLUE)
                .append(blueprint.text().copy().formatted(Formatting.GRAY)));
    }

    public static BlueprintSchema getSchema(ItemStack stack) {
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtElement element = nbt.get("Blueprint");

        if (element == null)
            return null;

        Identifier id = Identifier.tryParse(element.asString());

        if (id == null) {
            AITMod.LOGGER.warn("Couldn't parse blueprint id: '{}'", element.asString());
            return null;
        }

        BlueprintSchema schema = BlueprintRegistry.getInstance().get(id);

        if (schema == null) {
            AITMod.LOGGER.warn("Couldn't find blueprint with id: '{}'!", id);
            return null;
        }

        return schema;
    }
}
