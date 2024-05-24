package svenhjol.charm.feature.mob_textures.client;

import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.mob_textures.MobTextures;
import svenhjol.charm.foundation.feature.FeatureHolder;

import javax.annotation.Nullable;
import java.util.*;

public final class Handlers extends FeatureHolder<MobTextures> {
    private static final Map<UUID, ResourceLocation> CACHED_TEXTURE = new WeakHashMap<>();
    private static final String TEXTURES_FOLDER = "textures/entity";
    private static final String TEXTURE_TAG = "charm_texture";
    private static final ResourceLocation DEFAULT_SHEEP = ResourceLocation.parse(TEXTURES_FOLDER + "/sheep/sheep.png");
    private final List<ResourceLocation> chickens = new ArrayList<>();
    private final List<ResourceLocation> cows = new ArrayList<>();
    private final List<ResourceLocation> dolphins = new ArrayList<>();
    private final List<ResourceLocation> snowGolems = new ArrayList<>();
    private final List<ResourceLocation> squids = new ArrayList<>();
    private final List<ResourceLocation> pigs = new ArrayList<>();
    private final List<ResourceLocation> turtles = new ArrayList<>();
    private final List<ResourceLocation> wanderingTraders = new ArrayList<>();
    private final List<ResourceLocation> rareChickens = new ArrayList<>();
    private final List<ResourceLocation> rareCows = new ArrayList<>();
    private final List<ResourceLocation> rareDolphins = new ArrayList<>();
    private final List<ResourceLocation> rareSquids = new ArrayList<>();
    private final List<ResourceLocation> rarePigs = new ArrayList<>();
    private final List<ResourceLocation> rareTurtles = new ArrayList<>();
    private final Map<DyeColor, ResourceLocation> sheep = new HashMap<>();

    public Handlers(MobTextures feature) {
        super(feature);
    }

    @Nullable
    public ResourceLocation chickenTexture(Chicken entity) {
        return texture(entity, chickens, rareChickens);
    }

    @Nullable
    public ResourceLocation cowTexture(Cow entity) {
        return texture(entity, cows, rareCows);
    }

    @Nullable
    public ResourceLocation dolphinTexture(Dolphin entity) {
        return texture(entity, dolphins, rareDolphins);
    }

    @Nullable
    public ResourceLocation pigTexture(Pig entity) {
        return texture(entity, pigs, rarePigs);
    }

    public ResourceLocation sheepTexture(Sheep entity) {
        var fleeceColor = entity.getColor();
        return sheep.getOrDefault(fleeceColor, DEFAULT_SHEEP);
    }

    @Nullable
    public ResourceLocation snowGolemTexture(SnowGolem entity) {
        return texture(entity, snowGolems, ImmutableList.of());
    }

    @Nullable
    public ResourceLocation wanderingTraderTexture(WanderingTrader entity) {
        return texture(entity, wanderingTraders, ImmutableList.of());
    }

    @Nullable
    public ResourceLocation squidTexture(Squid entity) {
        return texture(entity, squids, rareSquids);
    }

    @Nullable
    public ResourceLocation turtleTexture(Turtle entity) {
        return texture(entity, turtles, rareTurtles);
    }

    @Nullable
    private ResourceLocation texture(Entity entity, List<ResourceLocation> normalSet, List<ResourceLocation> rareSet) {
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
                    res = new ResourceLocation(s[0], TEXTURES_FOLDER + "/" + s[1]);
                } else {
                    res = Charm.id(TEXTURES_FOLDER + "/" + defined);
                }
                CACHED_TEXTURE.put(id, res);
                return res;
            }
        }

        var isRare = feature().rareTypeChance() > 0
            && !rareSet.isEmpty()
            && (id.getLeastSignificantBits() + id.getMostSignificantBits()) % feature().rareTypeChance() == 0;

        var set = isRare ? rareSet : normalSet;
        if (set.isEmpty()) {
            return null;
        }

        var choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    @SuppressWarnings("unused")
    public void playerJoin(Entity entity, Level level) {
        if (!(entity instanceof Player)) return;

        // Reset all textures.
        chickens.clear();
        cows.clear();
        dolphins.clear();
        pigs.clear();
        snowGolems.clear();
        squids.clear();
        turtles.clear();
        wanderingTraders.clear();

        rareChickens.clear();
        rareCows.clear();
        rareDolphins.clear();
        rarePigs.clear();
        rareSquids.clear();
        rareTurtles.clear();

        // Add vanilla textures.
        chickens.add(ResourceLocation.parse(TEXTURES_FOLDER + "/chicken.png"));
        cows.add(ResourceLocation.parse(TEXTURES_FOLDER + "/cow/cow.png"));
        dolphins.add(ResourceLocation.parse(TEXTURES_FOLDER + "/dolphin.png"));
        pigs.add(ResourceLocation.parse(TEXTURES_FOLDER + "/pig/pig.png"));
        snowGolems.add(ResourceLocation.parse(TEXTURES_FOLDER + "/snow_golem.png"));
        squids.add(ResourceLocation.parse(TEXTURES_FOLDER + "/squid/squid.png"));
        turtles.add(ResourceLocation.parse(TEXTURES_FOLDER + "/turtle/big_sea_turtle.png"));
        wanderingTraders.add(ResourceLocation.parse(TEXTURES_FOLDER + "/wandering_trader.png"));

        for (var i = 1; i <= 5; i++) {
            registerCustomTextures(chickens, MobType.CHICKEN, "chicken" + i);
        }

        for (var i = 1; i <= 2; i++) {
            registerCustomTextures(rareChickens, MobType.CHICKEN, "rare_chicken" + i);
        }

        for (var i = 1; i <= 3; i++) {
            registerCustomTextures(dolphins, MobType.DOLPHIN, "dolphin" + i);
        }

        for (var i = 1; i <= 2; i++) {
            registerCustomTextures(rareDolphins, MobType.DOLPHIN, "rare_dolphin" + i);
        }

        for (var i = 1; i <= 3; i++) {
            registerCustomTextures(turtles, MobType.TURTLE, "turtle" + i);
        }

        for (var i = 1; i <= 1; i++) {
            registerCustomTextures(rareTurtles, MobType.TURTLE, "rare_turtle" + i);
        }

        for (var i = 1; i <= 7; i++) {
            registerCustomTextures(cows, MobType.COW, "cow" + i);
        }

        for (var i = 1; i <= 4; i++) {
            registerCustomTextures(wanderingTraders, MobType.WANDERING_TRADER, "wandering_trader" + i);
        }

        for (var i = 1; i <= 1; i++) {
            registerCustomTextures(rareCows, MobType.COW, "rare_cow" + i);
        }

        for (var i = 1; i <= 4; i++) {
            registerCustomTextures(pigs, MobType.PIG, "pig" + i);
        }

        for (var i = 1; i <= 2; i++) {
            registerCustomTextures(rarePigs, MobType.PIG, "rare_pig" + i);
        }

        for (var i = 1; i <= 5; i++) {
            registerCustomTextures(snowGolems, MobType.SNOW_GOLEM, "snow_golem" + i);
        }

        for (var i = 1; i <= 4; i++) {
            registerCustomTextures(squids, MobType.SQUID, "squid" + i);
        }

        for (var i = 1; i <= 1; i++) {
            registerCustomTextures(rareSquids, MobType.SQUID, "rare_squid" + i);
        }

        // Add all the sheep textures by dyecolor
        for (var color : DyeColor.values()) {
            var res = createResource(MobType.SHEEP, "sheep_" + color.toString());
            sheep.put(color, res);
        }
    }

    private void registerCustomTextures(List<ResourceLocation> set, MobType type, String... names) {
        ArrayList<String> textures = new ArrayList<>(Arrays.asList(names));

        textures.forEach(texture -> {
            var res = createResource(type, texture);
            set.add(res);
        });
    }

    private ResourceLocation createResource(MobType type, String texture) {
        return feature().registry().id(TEXTURES_FOLDER + "/" + type.getSerializedName() + "/" + texture + ".png");
    }
}
