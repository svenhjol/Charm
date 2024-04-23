package svenhjol.charm.feature.colored_glints;

import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Register;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.helper.EnumHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public class ColoredGlintsClient extends ClientFeature {
    public static boolean enabled = false;

    @Configurable(
        name = "Default glint color",
        description = """
            Overrides the default enchantment glint color.
            Must be a valid dye color name.""",
        requireRestart = false
    )
    public static String defaultGlintColor = DyeColor.PURPLE.getSerializedName();

    @Override
    public String description() {
        return """
            Allows the default enchantment glint color to be customized.
            An item with its own custom enchantment glint color will not be overridden by this feature.""";
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> {
            // Ensure uppercase dye name when retrieving value from config.
            defaultGlintColor = defaultGlintColor.toUpperCase(Locale.ROOT);
            return true;
        });
    }

    @Override
    public void onEnabled() {
        enabled = true;
    }

    /**
     * Get the enchanted item's glint color.
     * If it isn't set then return the configured default.
     */
    public static DyeColor get(@Nullable ItemStack stack) {
        if (!has(stack)) {
            return getDefault();
        }

        return stack.get(DataComponents.BASE_COLOR);
    }

    /**
     * Check if stack has a colored glint.
     */
    @SuppressWarnings("unused")
    public static boolean has(@Nullable ItemStack stack) {
        return stack != null && stack.has(DataComponents.BASE_COLOR);
    }

    /**
     * Get the configured default dye color.
     */
    @SuppressWarnings("unused")
    public static DyeColor getDefault() {
        // TODO: cache me
        return EnumHelper.getValueOrDefault(
            () -> DyeColor.valueOf(defaultGlintColor), DyeColor.PURPLE);
    }

    @Override
    public List<? extends Register<? extends Feature>> register() {
        return List.of(new ClientRegister(this));
    }

    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return ColoredGlints.class;
    }

    @SuppressWarnings("unused")
    public void handleClientStarted(Minecraft client) {
        ClientHandler.init();
    }
}
