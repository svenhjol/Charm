package svenhjol.charm.feature.glint_coloring.common;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.glint_coloring.GlintColoring;
import svenhjol.charm.foundation.feature.FeatureHolder;

import javax.annotation.Nullable;

public final class Handlers extends FeatureHolder<GlintColoring> {
    public Handlers(GlintColoring feature) {
        super(feature);
    }

    /**
     * Set the enchanted item's glint to the dye color.
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
     */
    @Nullable
    public DyeColor get(@Nullable ItemStack stack) {
        if (has(stack)) {
            return GlintColorData.get(stack).color();
        }

        return null;
    }

    /**
     * Check if stack has a colored glint.
     */
    public boolean has(@Nullable ItemStack stack) {
        return stack != null && GlintColorData.has(stack);
    }
}
