package svenhjol.charm.feature.variant_mob_textures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import svenhjol.charm.api.event.ClientEntityJoinEvent;
import svenhjol.charm.foundation.Registration;

import java.util.*;

public final class ClientRegistration extends Registration<VariantMobTextures> {
    static final String TEXTURES = "textures/entity";
    static final ResourceLocation DEFAULT_SHEEP = new ResourceLocation(TEXTURES + "/sheep/sheep.png");
    static final List<ResourceLocation> CHICKENS = new ArrayList<>();
    static final List<ResourceLocation> COWS = new ArrayList<>();
    static final List<ResourceLocation> DOLPHINS = new ArrayList<>();
    static final List<ResourceLocation> SNOW_GOLEMS = new ArrayList<>();
    static final List<ResourceLocation> SQUIDS = new ArrayList<>();
    static final List<ResourceLocation> PIGS = new ArrayList<>();
    static final List<ResourceLocation> TURTLES = new ArrayList<>();
    static final Map<DyeColor, ResourceLocation> SHEEP = new HashMap<>();
    static final List<ResourceLocation> WANDERING_TRADERS = new ArrayList<>();
    static final List<ResourceLocation> RARE_CHICKENS = new ArrayList<>();
    static final List<ResourceLocation> RARE_COWS = new ArrayList<>();
    static final List<ResourceLocation> RARE_DOLPHINS = new ArrayList<>();
    static final List<ResourceLocation> RARE_SQUIDS = new ArrayList<>();
    static final List<ResourceLocation> RARE_PIGS = new ArrayList<>();
    static final List<ResourceLocation> RARE_TURTLES = new ArrayList<>();

    public ClientRegistration(VariantMobTextures feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        var registry = feature.registry();

        if (VariantMobTextures.chickens) {
            registry.entityRenderer(() -> EntityType.CHICKEN,
                () -> VariantMobRenderer.RenderChicken::new);
        }

        if (VariantMobTextures.cows) {
            registry.entityRenderer(() -> EntityType.COW,
                () -> VariantMobRenderer.RenderCow::new);
        }

        if (VariantMobTextures.dolphins) {
            registry.entityRenderer(() -> EntityType.DOLPHIN,
                () -> VariantMobRenderer.RenderDolphin::new);
        }

        if (VariantMobTextures.pigs) {
            registry.entityRenderer(() -> EntityType.PIG,
                () -> VariantMobRenderer.RenderPig::new);
        }

        if (VariantMobTextures.sheep) {
            registry.entityRenderer(() -> EntityType.SHEEP,
                () -> VariantMobRenderer.RenderSheep::new);
        }

        if (VariantMobTextures.snowGolems) {
            registry.entityRenderer(() -> EntityType.SNOW_GOLEM,
                () -> VariantMobRenderer.RenderSnowGolem::new);
        }

        if (VariantMobTextures.squids) {
            registry.entityRenderer(() -> EntityType.SQUID,
                () -> VariantMobRenderer.RenderSquid::new);
        }

        if (VariantMobTextures.turtles) {
            registry.entityRenderer(() -> EntityType.TURTLE,
                () -> VariantMobRenderer.RenderTurtle::new);
        }

        if (VariantMobTextures.wanderingTraders) {
            registry.entityRenderer(() -> EntityType.WANDERING_TRADER,
                () -> VariantMobRenderer.RenderWanderingTrader::new);
        }
    }

    @Override
    public void onEnabled() {
        ClientEntityJoinEvent.INSTANCE.handle(this::handlePlayerJoin);
    }

    public void handlePlayerJoin(Entity entity, Level level) {
        if (!(entity instanceof Player)) return;

        // Reset all textures.
        CHICKENS.clear();
        COWS.clear();
        DOLPHINS.clear();
        PIGS.clear();
        SNOW_GOLEMS.clear();
        SQUIDS.clear();
        TURTLES.clear();
        WANDERING_TRADERS.clear();

        RARE_CHICKENS.clear();
        RARE_COWS.clear();
        RARE_DOLPHINS.clear();
        RARE_PIGS.clear();
        RARE_SQUIDS.clear();
        RARE_TURTLES.clear();

        // Add vanilla textures.
        CHICKENS.add(new ResourceLocation(TEXTURES + "/chicken.png"));
        COWS.add(new ResourceLocation(TEXTURES + "/cow/cow.png"));
        DOLPHINS.add(new ResourceLocation(TEXTURES + "/dolphin.png"));
        PIGS.add(new ResourceLocation(TEXTURES + "/pig/pig.png"));
        SNOW_GOLEMS.add(new ResourceLocation(TEXTURES + "/snow_golem.png"));
        SQUIDS.add(new ResourceLocation(TEXTURES + "/squid/squid.png"));
        TURTLES.add(new ResourceLocation(TEXTURES + "/turtle/big_sea_turtle.png"));
        WANDERING_TRADERS.add(new ResourceLocation(TEXTURES + "/wandering_trader.png"));

        for (var i = 1; i <= 5; i++) {
            registerCustomTextures(CHICKENS, VariantMobTextures.MobType.CHICKEN, "chicken" + i);
        }

        for (var i = 1; i <= 2; i++) {
            registerCustomTextures(RARE_CHICKENS, VariantMobTextures.MobType.CHICKEN, "rare_chicken" + i);
        }

        for (var i = 1; i <= 3; i++) {
            registerCustomTextures(DOLPHINS, VariantMobTextures.MobType.DOLPHIN, "dolphin" + i);
        }

        for (var i = 1; i <= 2; i++) {
            registerCustomTextures(RARE_DOLPHINS, VariantMobTextures.MobType.DOLPHIN, "rare_dolphin" + i);
        }

        for (var i = 1; i <= 3; i++) {
            registerCustomTextures(TURTLES, VariantMobTextures.MobType.TURTLE, "turtle" + i);
        }

        for (var i = 1; i <= 1; i++) {
            registerCustomTextures(RARE_TURTLES, VariantMobTextures.MobType.TURTLE, "rare_turtle" + i);
        }

        for (var i = 1; i <= 7; i++) {
            registerCustomTextures(COWS, VariantMobTextures.MobType.COW, "cow" + i);
        }

        for (var i = 1; i <= 4; i++) {
            registerCustomTextures(WANDERING_TRADERS, VariantMobTextures.MobType.WANDERING_TRADER, "wandering_trader" + i);
        }

        for (var i = 1; i <= 1; i++) {
            registerCustomTextures(RARE_COWS, VariantMobTextures.MobType.COW, "rare_cow" + i);
        }

        for (var i = 1; i <= 4; i++) {
            registerCustomTextures(PIGS, VariantMobTextures.MobType.PIG, "pig" + i);
        }

        for (var i = 1; i <= 2; i++) {
            registerCustomTextures(RARE_PIGS, VariantMobTextures.MobType.PIG, "rare_pig" + i);
        }

        for (var i = 1; i <= 5; i++) {
            registerCustomTextures(SNOW_GOLEMS, VariantMobTextures.MobType.SNOW_GOLEM, "snow_golem" + i);
        }

        for (var i = 1; i <= 4; i++) {
            registerCustomTextures(SQUIDS, VariantMobTextures.MobType.SQUID, "squid" + i);
        }

        for (var i = 1; i <= 1; i++) {
            registerCustomTextures(RARE_SQUIDS, VariantMobTextures.MobType.SQUID, "rare_squid" + i);
        }

        // Add all the sheep textures by dyecolor
        for (var color : DyeColor.values()) {
            var res = createResource(VariantMobTextures.MobType.SHEEP, "sheep_" + color.toString());
            SHEEP.put(color, res);
        }
    }

    private void registerCustomTextures(List<ResourceLocation> set, VariantMobTextures.MobType type, String... names) {
        ArrayList<String> textures = new ArrayList<>(Arrays.asList(names));

        textures.forEach(texture -> {
            var res = createResource(type, texture);
            set.add(res);
        });
    }

    private ResourceLocation createResource(VariantMobTextures.MobType type, String texture) {
        return feature.registry().id(TEXTURES + "/" + type.getSerializedName() + "/" + texture + ".png");
    }
}
