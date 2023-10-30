package svenhjol.charm.feature.variant_mob_textures;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony_api.event.ClientEntityJoinEvent;

import javax.annotation.Nullable;
import java.util.*;

public class VariantMobTextures extends ClientFeature {
    static final String TEXTURES = "textures/entity";
    static final String TEXTURE_TAG = "charm_texture";
    static final Map<UUID, ResourceLocation> CACHED_TEXTURE = new WeakHashMap<>();
    static final ResourceLocation DEFAULT_SHEEP = new ResourceLocation(TEXTURES + "/sheep/sheep.png");
    static final List<ResourceLocation> CHICKENS = new ArrayList<>();
    static final List<ResourceLocation> COWS = new ArrayList<>();
    static final List<ResourceLocation> DOLPHINS = new ArrayList<>();
    static final List<ResourceLocation> SNOW_GOLEMS = new ArrayList<>();
    static final List<ResourceLocation> SQUIDS = new ArrayList<>();
    static final List<ResourceLocation> PIGS = new ArrayList<>();
    static final List<ResourceLocation> TURTLES = new ArrayList<>();
    static final List<ResourceLocation> WOLVES = new ArrayList<>();
    static final Map<DyeColor, ResourceLocation> SHEEP = new HashMap<>();
    static final List<ResourceLocation> WANDERING_TRADERS = new ArrayList<>();
    static final List<ResourceLocation> RARE_CHICKENS = new ArrayList<>();
    static final List<ResourceLocation> RARE_COWS = new ArrayList<>();
    static final List<ResourceLocation> RARE_DOLPHINS = new ArrayList<>();
    static final List<ResourceLocation> RARE_SQUIDS = new ArrayList<>();
    static final List<ResourceLocation> RARE_PIGS = new ArrayList<>();
    static final List<ResourceLocation> RARE_TURTLES = new ArrayList<>();
    static final List<ResourceLocation> RARE_WOLVES = new ArrayList<>();

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

    @Configurable(name = "Wolves", description = "If true, wolves may spawn with different textures.")
    public static boolean wolves = true;

    @Configurable(name = "Wandering traders", description = "If true, wandering traders may spawn with different textures.")
    public static boolean wanderingTraders = true;

    @Configurable(name = "Chance of rare variants", description = "Approximately 1 in X chance of a mob spawning as a rare variant.\n" +
        "Set to zero to disable rare variants.")
    public static int rareVariantChance = 1000;

    @Override
    public boolean canBeDisabled() {
        return true;
    }

    @Override
    public String description() {
        return "Mobs may spawn with different textures.";
    }

    @Override
    public void register() {
        var registry = mod().registry();

        if (chickens) {
            registry.entityRenderer(() -> EntityType.CHICKEN,
                () -> VariantMobRenderer.RenderChicken::new);
        }

        if (cows) {
            registry.entityRenderer(() -> EntityType.COW,
                () -> VariantMobRenderer.RenderCow::new);
        }

        if (dolphins) {
            registry.entityRenderer(() -> EntityType.DOLPHIN,
                () -> VariantMobRenderer.RenderDolphin::new);
        }

        if (pigs) {
            registry.entityRenderer(() -> EntityType.PIG,
                () -> VariantMobRenderer.RenderPig::new);
        }

        if (sheep) {
            registry.entityRenderer(() -> EntityType.SHEEP,
                () -> VariantMobRenderer.RenderSheep::new);
        }

        if (snowGolems) {
            registry.entityRenderer(() -> EntityType.SNOW_GOLEM,
                () -> VariantMobRenderer.RenderSnowGolem::new);
        }

        if (squids) {
            registry.entityRenderer(() -> EntityType.SQUID,
                () -> VariantMobRenderer.RenderSquid::new);
        }

        if (turtles) {
            registry.entityRenderer(() -> EntityType.TURTLE,
                () -> VariantMobRenderer.RenderTurtle::new);
        }

        if (wolves) {
            registry.entityRenderer(() -> EntityType.WOLF,
                () -> VariantMobRenderer.RenderWolf::new);
        }

        if (wanderingTraders) {
            registry.entityRenderer(() -> EntityType.WANDERING_TRADER,
                () -> VariantMobRenderer.RenderWanderingTrader::new);
        }
    }

    @Override
    public void runWhenEnabled() {
        ClientEntityJoinEvent.INSTANCE.handle(this::handlePlayerJoin);
    }

    public void handlePlayerJoin(Entity entity, Level level) {
        if (!(entity instanceof LocalPlayer)) {
            return;
        }

        // Reset all textures.
        CHICKENS.clear();
        COWS.clear();
        DOLPHINS.clear();
        PIGS.clear();
        SNOW_GOLEMS.clear();
        SQUIDS.clear();
        TURTLES.clear();
        WOLVES.clear();
        WANDERING_TRADERS.clear();

        RARE_CHICKENS.clear();
        RARE_COWS.clear();
        RARE_DOLPHINS.clear();
        RARE_PIGS.clear();
        RARE_SQUIDS.clear();
        RARE_TURTLES.clear();
        RARE_WOLVES.clear();

        // Add vanilla textures.
        CHICKENS.add(new ResourceLocation(TEXTURES + "/chicken.png"));
        COWS.add(new ResourceLocation(TEXTURES + "/cow/cow.png"));
        DOLPHINS.add(new ResourceLocation(TEXTURES + "/dolphin.png"));
        PIGS.add(new ResourceLocation(TEXTURES + "/pig/pig.png"));
        SNOW_GOLEMS.add(new ResourceLocation(TEXTURES + "/snow_golem.png"));
        SQUIDS.add(new ResourceLocation(TEXTURES + "/squid/squid.png"));
        TURTLES.add(new ResourceLocation(TEXTURES + "/turtle/big_sea_turtle.png"));
        WANDERING_TRADERS.add(new ResourceLocation(TEXTURES + "/wandering_trader.png"));

        var wolf = new ResourceLocation(TEXTURES + "/wolf/wolf.png");
        WOLVES.add(wolf);

        for (var i = 1; i <= 5; i++) {
            addCustomTextures(CHICKENS, MobType.CHICKEN, "chicken" + i);
        }

        for (var i = 1; i <= 2; i++) {
            addCustomTextures(RARE_CHICKENS, MobType.CHICKEN, "rare_chicken" + i);
        }

        for (var i = 1; i <= 3; i++) {
            addCustomTextures(DOLPHINS, MobType.DOLPHIN, "dolphin" + i);
        }

        for (var i = 1; i <= 2; i++) {
            addCustomTextures(RARE_DOLPHINS, MobType.DOLPHIN, "rare_dolphin" + i);
        }

        for (var i = 1; i <= 3; i++) {
            addCustomTextures(TURTLES, MobType.TURTLE, "turtle" + i);
        }

        for (var i = 1; i <= 1; i++) {
            addCustomTextures(RARE_TURTLES, MobType.TURTLE, "rare_turtle" + i);
        }

        for (var i = 1; i <= 7; i++) {
            addCustomTextures(COWS, MobType.COW, "cow" + i);
        }

        for (var i = 1; i <= 4; i++) {
            addCustomTextures(WANDERING_TRADERS, MobType.WANDERING_TRADER, "wandering_trader" + i);
        }

        for (var i = 1; i <= 1; i++) {
            addCustomTextures(RARE_COWS, MobType.COW, "rare_cow" + i);
        }

        for (var i = 1; i <= 4; i++) {
            addCustomTextures(PIGS, MobType.PIG, "pig" + i);
        }

        for (var i = 1; i <= 2; i++) {
            addCustomTextures(RARE_PIGS, MobType.PIG, "rare_pig" + i);
        }

        for (var i = 1; i <= 5; i++) {
            addCustomTextures(SNOW_GOLEMS, MobType.SNOW_GOLEM, "snow_golem" + i);
        }

        for (var i = 1; i <= 4; i++) {
            addCustomTextures(SQUIDS, MobType.SQUID, "squid" + i);
        }

        for (var i = 1; i <= 1; i++) {
            addCustomTextures(RARE_SQUIDS, MobType.SQUID, "rare_squid" + i);
        }

        for (var i = 1; i <= 25; i++) {
            addCustomTextures(WOLVES, MobType.WOLF, "nlg_wolf" + i); // add NeverLoseGuy wolf textures
        }

        for (var i = 1; i <= 1; i++) {
            addCustomTextures(RARE_WOLVES, MobType.WOLF, "rare_wolf" + i);
        }

        addCustomTextures(WOLVES, MobType.WOLF, "brownwolf", "greywolf", "blackwolf", "amotwolf", "jupiter1390");

        // add all the sheep textures by dyecolor
        for (var color : DyeColor.values()) {
            var res = createResource(MobType.SHEEP, "sheep_" + color.toString());
            SHEEP.put(color, res);
        }
    }

    public void addCustomTextures(List<ResourceLocation> set, MobType type, String... names) {
        ArrayList<String> textures = new ArrayList<>(Arrays.asList(names));

        textures.forEach(texture -> {
            var res = createResource(type, texture);
            set.add(res);
        });
    }

    @Nullable
    public static ResourceLocation getChickenTexture(Chicken entity) {
        return getTexture(entity, CHICKENS, RARE_CHICKENS);
    }

    @Nullable
    public static ResourceLocation getCowTexture(Cow entity) {
        return getTexture(entity, COWS, RARE_COWS);
    }

    @Nullable
    public static ResourceLocation getDolphinTexture(Dolphin entity) {
        return getTexture(entity, DOLPHINS, RARE_DOLPHINS);
    }

    @Nullable
    public static ResourceLocation getPigTexture(Pig entity) {
        return getTexture(entity, PIGS, RARE_PIGS);
    }

    public static ResourceLocation getSheepTexture(Sheep entity) {
        var fleeceColor = entity.getColor();
        return SHEEP.getOrDefault(fleeceColor, DEFAULT_SHEEP);
    }

    @Nullable
    public static ResourceLocation getSnowGolemTexture(SnowGolem entity) {
        return getTexture(entity, SNOW_GOLEMS, ImmutableList.of());
    }

    @Nullable
    public static ResourceLocation getWanderingTraderTexture(WanderingTrader entity) {
        return getTexture(entity, WANDERING_TRADERS, ImmutableList.of());
    }

    @Nullable
    public static ResourceLocation getSquidTexture(Squid entity) {
        return getTexture(entity, SQUIDS, RARE_SQUIDS);
    }

    @Nullable
    public static ResourceLocation getTurtleTexture(Turtle entity) {
        return getTexture(entity, TURTLES, RARE_TURTLES);
    }

    @Nullable
    public static ResourceLocation getWolfTexture(Wolf entity) {
        var res = getTexture(entity, WOLVES, RARE_WOLVES);
        if (res == null) {
            return null;
        }

        if (entity.isTame()) {
            var path = res.getPath().split("\\.")[0] + "_tame.png";
            res = new ResourceLocation(res.getNamespace(), path);
        } else if (entity.isAngry()) {
            var path = res.getPath().split("\\.")[0] + "_angry.png";
            res = new ResourceLocation(res.getNamespace(), path);
        }

        return res;
    }

    @Nullable
    public static ResourceLocation getTexture(Entity entity, List<ResourceLocation> normalSet, List<ResourceLocation> rareSet) {
        var id = entity.getUUID();

        if (CACHED_TEXTURE.containsKey(id)) {
            var val = CACHED_TEXTURE.get(id);
            if (val != null) {
                return val;
            }
        } else {
            var defined = "";
            var tags = entity.getTags();

            for (var tag : tags) {
                if (tag.startsWith(TEXTURE_TAG)) {
                    defined = tag.split("=")[1];
                }
            }

            if (defined.isEmpty()) {
                CACHED_TEXTURE.put(id, null);
            } else {
                ResourceLocation res;
                if (defined.contains(":")) {
                    var s = defined.split(":");
                    res = new ResourceLocation(s[0], TEXTURES + "/" + s[1]);
                } else {
                    res = new ResourceLocation(Charm.ID, TEXTURES + "/" + defined);
                }
                CACHED_TEXTURE.put(id, res);
                return res;
            }
        }

        var isRare = rareVariantChance > 0
            && !rareSet.isEmpty()
            && (id.getLeastSignificantBits() + id.getMostSignificantBits()) % rareVariantChance == 0;

        var set = isRare ? rareSet : normalSet;
        if (set.isEmpty()) {
            return null;
        }

        var choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    private ResourceLocation createResource(MobType type, String texture) {
        return mod().id(TEXTURES + "/" + type.getSerializedName() + "/" + texture + ".png");
    }

    public enum MobType implements StringRepresentable {
        CHICKEN,
        COW,
        DOLPHIN,
        PIG,
        SHEEP,
        SNOW_GOLEM,
        SQUID,
        TURTLE,
        WANDERING_TRADER,
        WOLF;

        @Override
        public @NotNull String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
