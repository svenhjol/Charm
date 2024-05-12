package svenhjol.charm.feature.variant_mob_textures;

import svenhjol.charm.feature.variant_mob_textures.client.Handlers;
import svenhjol.charm.feature.variant_mob_textures.client.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(description = "Mobs may spawn with different textures.")
public final class VariantMobTextures extends ClientFeature {
    public final Registers registers;
    public final Handlers handlers;

    @Configurable(name = "Cows", description = "If true, cows may spawn with different textures.")
    public static boolean cows = true;

    @Configurable(name = "Chickens", description = "If true, chickens may spawn with different textures.")
    public static boolean chickens = true;

    @Configurable(name = "Dolphins", description = "If true, dolphins may spawn with different textures.")
    public static boolean dolphins = true;

    @Configurable(name = "Pigs", description = "If true, pigs may spawn with different textures.")
    public static boolean pigs = true;

    @Configurable(name = "Sheep", description = "If true, sheep face and 'shorn' textures match their wool color.")
    public static boolean sheep = true;

    @Configurable(name = "Snow golems", description = "If true, snow golems may spawn with different derp faces.")
    public static boolean snowGolems = true;

    @Configurable(name = "Squids", description = "If true, squids may spawn with different textures.")
    public static boolean squids = true;

    @Configurable(name = "Turtles", description = "If true, turtles may spawn with different textures.")
    public static boolean turtles = true;

    @Configurable(name = "Wandering traders", description = "If true, wandering traders may spawn with different textures.")
    public static boolean wanderingTraders = true;

    @Configurable(name = "Chance of rare variants", description = "Approximately 1 in X chance of a mob spawning as a rare variant.\n" +
        "Set to zero to disable rare variants.")
    public static int rareVariantChance = 1000;

    public VariantMobTextures(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
