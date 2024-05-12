package svenhjol.charm.feature.colored_glints.common;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.colored_glints.ColoredGlints;
import svenhjol.charm.feature.colored_glints.ColoredGlintsClient;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.EnumHelper;

import javax.annotation.Nullable;

public final class Handlers extends FeatureHolder<ColoredGlints> {
    public DyeColor cachedGlintColor;

    public Handlers(ColoredGlints feature) {
        super(feature);
    }

    /**
     * Set the enchanted item's glint to the dye color.
     * Probably should only apply it to a stack with foil...
     */
    public void apply(ItemStack stack, DyeColor color) {
        ColoredGlintData.create()
            .setColor(color)
            .save(stack);
    }

    public void remove(ItemStack stack) {
        ColoredGlintData.remove(stack);
    }

    /**
     * Get the enchanted item's glint color.
     * If it isn't set then return the configured default.
     */
    public DyeColor get(@Nullable ItemStack stack) {
        if (stack == null || !ColoredGlintData.has(stack)) {
            return getDefault();
        }

        return ColoredGlintData.get(stack).color();
    }

    /**
     * Check if stack has a colored glint.
     */
    public boolean has(@Nullable ItemStack stack) {
        return stack != null && ColoredGlintData.has(stack);
    }

    /**
     * Get the configured default dye color.
     */
    public DyeColor getDefault() {
        if (cachedGlintColor == null) {
            cachedGlintColor = EnumHelper.getValueOrDefault(
                () -> DyeColor.valueOf(ColoredGlintsClient.defaultGlintColor), DyeColor.PURPLE);
        }
        return cachedGlintColor;
    }
}
