package svenhjol.charm.feature.colored_glints;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.helper.EnumHelper;

import javax.annotation.Nullable;

public class ColoredGlints extends CommonFeature {
    @Override
    public String description() {
        return """
            Customizable item enchantment colors. This feature is a helper for other Charmony features and mods.
            If disabled then other features and mods that rely on it will not function properly.""";
    }

    /**
     * Set the enchanted item's glint to the dye color.
     * Probably should only apply it to a stack with foil...
     */
    public static void apply(ItemStack stack, DyeColor color) {
        stack.set(DataComponents.BASE_COLOR, color);
    }

    /**
     * Get the enchanted item's glint color.
     * If it isn't set then return the configured default.
     */
    public static DyeColor get(@Nullable ItemStack stack) {
        if (!stackHasFoilAndColor(stack)) {
            return getDefault();
        }

        return stack.get(DataComponents.BASE_COLOR);
    }

    /**
     * Check if stack has a colored glint.
     */
    @SuppressWarnings("unused")
    public static boolean stackHasFoilAndColor(@Nullable ItemStack stack) {
        return stack != null && stack.hasFoil() && stack.has(DataComponents.BASE_COLOR);
    }

    /**
     * Get the configured default dye color.
     */
    @SuppressWarnings("unused")
    public static DyeColor getDefault() {
        if (ColoredGlintsClient.cachedGlintColor == null) {
            ColoredGlintsClient.cachedGlintColor = EnumHelper.getValueOrDefault(
                () -> DyeColor.valueOf(ColoredGlintsClient.defaultGlintColor), DyeColor.PURPLE);
        }
        return ColoredGlintsClient.cachedGlintColor;
    }
}
