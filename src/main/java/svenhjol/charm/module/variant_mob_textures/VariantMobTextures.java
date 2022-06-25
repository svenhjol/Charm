package svenhjol.charm.module.variant_mob_textures;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Mobs may spawn with different textures.")
public class VariantMobTextures extends CharmModule {
    @Config(name = "Variant cows", description = "If true, cows may spawn with different textures.")
    public static boolean variantCows = true;

    @Config(name = "Variant chickens", description = "If true, chickens may spawn with different textures.")
    public static boolean variantChickens = true;

    @Config(name = "Variant dolphins", description = "If true, dolphins may spawn with different textures.")
    public static boolean variantDolphins = true;

    @Config(name = "Variant pigs", description = "If true, pigs may spawn with different textures.")
    public static boolean variantPigs = true;

    @Config(name = "Variant sheep", description = "If true, sheep face and 'shorn' textures match their wool color.")
    public static boolean variantSheep = true;

    @Config(name = "Variant snow golems", description = "If true, snow golems may spawn with different derp faces.")
    public static boolean variantSnowGolems = true;

    @Config(name = "Variant squids", description = "If true, squids may spawn with different textures.")
    public static boolean variantSquids = true;

    @Config(name = "Variant turtles", description = "If true, turtles may spawn with different textures.")
    public static boolean variantTurtles = true;

    @Config(name = "Variant wolves", description = "If true, wolves may spawn with different textures.")
    public static boolean variantWolves = true;

    @Config(name = "Variant wandering traders", description = "If true, wandering traders may spawn with different textures.")
    public static boolean variantWanderingTraders = true;

    @Config(name = "Rare variants", description = "If true, all animals have a chance to spawn as a rare variant.")
    public static boolean rareVariants = true;

    @Config(name = "Rarity of rare variants", description = "Approximately 1 in X chance of a mob spawning as a rare variant.")
    public static int rarity = 1000;
}
