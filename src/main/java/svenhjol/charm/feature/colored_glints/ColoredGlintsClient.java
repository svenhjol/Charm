package svenhjol.charm.feature.colored_glints;

import net.minecraft.world.item.DyeColor;
import svenhjol.charm.feature.colored_glints.client.Handlers;
import svenhjol.charm.feature.colored_glints.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;

@Feature(description = """
    Allows the default enchantment glint color to be customized.
    An item with its own custom enchantment glint color will not be overridden by this feature.""")
public final class ColoredGlintsClient extends ClientFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final ColoredGlints common;

    @Configurable(
        name = "Default glint color",
        description = """
            Overrides the default enchantment glint color.
            Must be a valid dye color name.""",
        requireRestart = false
    )
    public static String defaultGlintColor = DyeColor.PURPLE.getSerializedName();

    public ColoredGlintsClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(ColoredGlints.class);
        registers = new Registers(this);
        handlers = new Handlers(this);
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
        handlers.setEnabled(true);
    }
}
