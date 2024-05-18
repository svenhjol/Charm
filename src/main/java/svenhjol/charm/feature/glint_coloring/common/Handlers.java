package svenhjol.charm.feature.glint_coloring.common;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.glint_coloring.GlintColoring;
import svenhjol.charm.feature.glint_coloring.GlintColoringClient;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.EnumHelper;

import javax.annotation.Nullable;

public final class Handlers extends FeatureHolder<GlintColoring> {
    public DyeColor cachedGlintColor;

    public Handlers(GlintColoring feature) {
        super(feature);
    }

    /**
     * Set the enchanted item's glint to the dye color.
     * Probably should only apply it to a stack with foil...
     */
    public void apply(ItemStack stack, DyeColor color) {
        GlintColorData.create()
            .setColor(color)
            .save(stack);
    }

    public void remove(ItemStack stack) {
        GlintColorData.remove(stack);
    }

    /**
     * Get the enchanted item's glint color.
     * If it isn't set then return the configured default.
     */
    public DyeColor get(@Nullable ItemStack stack) {
        if (stack == null || !GlintColorData.has(stack)) {
            return getDefault();
        }

        return GlintColorData.get(stack).color();
    }

    /**
     * Check if stack has a colored glint.
     */
    public boolean has(@Nullable ItemStack stack) {
        return stack != null && GlintColorData.has(stack);
    }

    /**
     * Get the configured default dye color.
     */
    public DyeColor getDefault() {
        if (cachedGlintColor == null) {
            cachedGlintColor = EnumHelper.getValueOrDefault(
                () -> DyeColor.valueOf(GlintColoringClient.defaultGlintColor), DyeColor.PURPLE);
        }
        return cachedGlintColor;
    }
}
