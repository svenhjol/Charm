package svenhjol.charm.feature.mob_textures;

import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.feature.mob_textures.client.Handlers;
import svenhjol.charm.feature.mob_textures.client.Registers;

@Feature(description = "Mobs may spawn with different textures.")
public final class MobTextures extends ClientFeature {
    public final Registers registers;
    public final Handlers handlers;

    @Configurable(name = "Cows", description = "If true, cows may spawn with different textures.")
    private static boolean cows = true;

    @Configurable(name = "Chickens", description = "If true, chickens may spawn with different textures.")
    private static boolean chickens = true;

    @Configurable(name = "Dolphins", description = "If true, dolphins may spawn with different textures.")
    private static boolean dolphins = true;

    @Configurable(name = "Pigs", description = "If true, pigs may spawn with different textures.")
    private static boolean pigs = true;

    @Configurable(name = "Sheep", description = "If true, sheep face and 'shorn' textures match their wool color.")
    private static boolean sheep = true;

    @Configurable(name = "Snow golems", description = "If true, snow golems may spawn with different derp faces.")
    private static boolean snowGolems = true;

    @Configurable(name = "Squids", description = "If true, squids may spawn with different textures.")
    private static boolean squids = true;

    @Configurable(name = "Turtles", description = "If true, turtles may spawn with different textures.")
    private static boolean turtles = true;

    @Configurable(name = "Wandering traders", description = "If true, wandering traders may spawn with different textures.")
    private static boolean wanderingTraders = true;

    public MobTextures(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    public boolean cows() {
        return cows;
    }

    public boolean chickens() {
        return chickens;
    }

    public boolean dolphins() {
        return dolphins;
    }

    public boolean pigs() {
        return pigs;
    }

    public boolean sheep() {
        return sheep;
    }

    public boolean snowGolems() {
        return snowGolems;
    }

    public boolean squids() {
        return squids;
    }

    public boolean turtles() {
        return turtles;
    }

    public boolean wanderingTraders() {
        return wanderingTraders;
    }

    public int rareTypeChance() {
        return 1000;
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
