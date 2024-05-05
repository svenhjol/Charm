package svenhjol.charm.feature.colored_glints;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.helper.EnumHelper;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

public class ColoredGlints extends CommonFeature {
    static Supplier<DataComponentType<ColoredGlintData>> data;

    @Override
    public String description() {
        return """
            Customizable item enchantment colors. This feature is a helper for other Charmony features and mods.
            If disabled then other features and mods that rely on it will not function properly.""";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    /**
     * Set the enchanted item's glint to the dye color.
     * Probably should only apply it to a stack with foil...
     */
    public static void apply(ItemStack stack, DyeColor color) {
        ColoredGlintData.create()
            .setColor(color)
            .save(stack);

//        stack.set(DataComponents.BASE_COLOR, color);
    }

    public static void remove(ItemStack stack) {
        ColoredGlintData.remove(stack);

//        stack.remove(DataComponents.BASE_COLOR);
    }

    /**
     * Get the enchanted item's glint color.
     * If it isn't set then return the configured default.
     */
    public static DyeColor get(@Nullable ItemStack stack) {
        if (stack == null || !ColoredGlintData.has(stack)) {
            return getDefault();
        }

        return ColoredGlintData.get(stack).color();
    }

    /**
     * Check if stack has a colored glint.
     */
    @SuppressWarnings("unused")
    public static boolean has(@Nullable ItemStack stack) {
        return stack != null && ColoredGlintData.has(stack);
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
