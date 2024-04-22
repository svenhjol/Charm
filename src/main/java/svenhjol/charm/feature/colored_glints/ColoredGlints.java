package svenhjol.charm.feature.colored_glints;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.foundation.common.CommonFeature;

public class ColoredGlints extends CommonFeature {
    @Override
    public String description() {
        return """
            Customizable item enchantment colors. This feature is a helper for other Charmony features and mods.
            If disabled then other features and mods that rely on it will not function properly.""";
    }

    /**
     * Set the enchanted item's glint to the dye color.
     */
    public static void applyColoredGlint(ItemStack stack, DyeColor color) {
        stack.set(DataComponents.BASE_COLOR, color);
    }
}
