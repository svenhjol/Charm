package svenhjol.charm.feature.colored_glints;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BooleanSupplier;

public class ColoredGlintsClient extends ClientFeature {
    static boolean enabled = false;
    static DyeColor cachedGlintColor;

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

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }

    @Override
    public Class<? extends CommonFeature> commonClass() {
        return ColoredGlints.class;
    }

    @SuppressWarnings("unused")
    public void handleClientStarted(Minecraft client) {
        ClientHandler.init();
    }
}
