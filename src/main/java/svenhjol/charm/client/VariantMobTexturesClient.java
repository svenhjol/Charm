package svenhjol.charm.client;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.*;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.enums.ICharmEnum;
import svenhjol.charm.module.VariantMobTextures;
import svenhjol.charm.render.VariantMobRenderer;
import svenhjol.charm.base.CharmModule;

import java.util.*;

public class VariantMobTexturesClient extends CharmClientModule {
    private static final String PREFIX = "textures/entity/";
    private static final Identifier DEFAULT_SHEEP = new Identifier(PREFIX + "sheep/sheep.png");

    public static List<Identifier> chickens = new ArrayList<>();
    public static List<Identifier> cows = new ArrayList<>();
    public static List<Identifier> snowGolems = new ArrayList<>();
    public static List<Identifier> squids = new ArrayList<>();
    public static List<Identifier> pigs = new ArrayList<>();
    public static List<Identifier> wolves = new ArrayList<>();

    public static List<Identifier> rareChickens = new ArrayList<>();
    public static List<Identifier> rareCows = new ArrayList<>();
    public static List<Identifier> rareSquids = new ArrayList<>();
    public static List<Identifier> rarePigs = new ArrayList<>();
    public static List<Identifier> rareWolves = new ArrayList<>();

    public static Map<Identifier, Identifier> wolvesTame = new HashMap<>();
    public static Map<Identifier, Identifier> wolvesAngry = new HashMap<>();
    public static Map<DyeColor, Identifier> sheep = new HashMap<>();

    public VariantMobTexturesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        if (VariantMobTextures.variantChickens)
            EntityRendererRegistry.INSTANCE.register(EntityType.CHICKEN, VariantMobRenderer.Chicken::new);

        if (VariantMobTextures.variantCows)
            EntityRendererRegistry.INSTANCE.register(EntityType.COW, VariantMobRenderer.Cow::new);

        if (VariantMobTextures.variantPigs)
            EntityRendererRegistry.INSTANCE.register(EntityType.PIG, VariantMobRenderer.Pig::new);

        if (VariantMobTextures.variantSheep)
            EntityRendererRegistry.INSTANCE.register(EntityType.SHEEP, VariantMobRenderer.Sheep::new);

        if (VariantMobTextures.variantSnowGolems)
            EntityRendererRegistry.INSTANCE.register(EntityType.SNOW_GOLEM, VariantMobRenderer.SnowGolem::new);

        if (VariantMobTextures.variantSquids)
            EntityRendererRegistry.INSTANCE.register(EntityType.SQUID, VariantMobRenderer.Squid::new);

        if (VariantMobTextures.variantWolves)
            EntityRendererRegistry.INSTANCE.register(EntityType.WOLF, VariantMobRenderer.Wolf::new);

    }

    @Override
    public void loadWorld(MinecraftClient client) {
        // reset
        chickens = new ArrayList<>();
        cows = new ArrayList<>();
        pigs = new ArrayList<>();
        snowGolems = new ArrayList<>();
        squids = new ArrayList<>();
        wolves = new ArrayList<>();
        wolvesTame = new HashMap<>();
        wolvesAngry = new HashMap<>();

        rareChickens = new ArrayList<>();
        rareCows = new ArrayList<>();
        rarePigs = new ArrayList<>();
        rareSquids = new ArrayList<>();
        rareWolves = new ArrayList<>();

        // add vanilla textures
        chickens.add(new Identifier(PREFIX + "chicken.png"));
        cows.add(new Identifier(PREFIX + "cow/cow.png"));
        pigs.add(new Identifier(PREFIX + "pig/pig.png"));
        snowGolems.add(new Identifier(PREFIX + "snow_golem.png"));
        squids.add(new Identifier(PREFIX + "squid.png"));

        Identifier wolf = new Identifier(PREFIX + "wolf/wolf.png");
        wolves.add(wolf);
        wolvesTame.put(wolf, new Identifier(PREFIX + "wolf/wolf_tame.png"));
        wolvesAngry.put(wolf, new Identifier(PREFIX + "wolf/wolf_angry.png"));

        for (int i = 1; i <= 5; i++)
            addCustomTextures(chickens, MobType.CHICKEN, "chicken" + i);

        for (int i = 1; i <= 2; i++)
            addCustomTextures(rareChickens, MobType.CHICKEN, "rare_chicken" + i);

        for (int i = 1; i <= 7; i++)
            addCustomTextures(cows, MobType.COW, "cow" + i);

        for (int i = 1; i <= 1; i++)
            addCustomTextures(rareCows, MobType.COW, "rare_cow" + i);

        for (int i = 1; i <= 5; i++)
            addCustomTextures(pigs, MobType.PIG, "pig" + i);

        for (int i = 1; i <= 1; i++)
            addCustomTextures(rarePigs, MobType.PIG, "rare_pig" + i);

        for (int i = 1; i <= 5; i++)
            addCustomTextures(snowGolems, MobType.SNOW_GOLEM, "snow_golem" + i);

        for (int i = 1; i <= 4; i++)
            addCustomTextures(squids, MobType.SQUID, "squid" + i);

        for (int i = 1; i <= 1; i++)
            addCustomTextures(rareSquids, MobType.SQUID, "rare_squid" + i);

        for (int i = 1; i <= 25; i++)
            addCustomTextures(wolves, MobType.WOLF, "nlg_wolf" + i); // add NeverLoseGuy wolf textures

        for (int i = 1; i <= 1; i++)
            addCustomTextures(rareWolves, MobType.WOLF, "rare_wolf" + i);

        addCustomTextures(wolves, MobType.WOLF, "brownwolf", "greywolf", "blackwolf", "amotwolf", "jupiter1390");

        // add all the sheep textures by dyecolor
        for (DyeColor color : DyeColor.values()) {
            Identifier res = createResource(MobType.SHEEP, "sheep_" + color.toString());
            sheep.put(color, res);
        }
    }

    public void addCustomTextures(List<Identifier> set, MobType type, String... names) {
        ArrayList<String> textures = new ArrayList<>(Arrays.asList(names));

        textures.forEach(texture -> {
            Identifier res = createResource(type, texture);
            set.add(res);

            if (type == MobType.WOLF) {
                wolvesTame.put(res, createResource(type, texture + "_tame"));
                wolvesAngry.put(res, createResource(type, texture + "_angry"));
            }
        });
    }

    public static Identifier getChickenTexture(ChickenEntity entity) {
        return getRandomTexture(entity, chickens, rareChickens);
    }

    public static Identifier getCowTexture(CowEntity entity) {
        return getRandomTexture(entity, cows, rareCows);
    }

    public static Identifier getPigTexture(PigEntity entity) {
        return getRandomTexture(entity, pigs, rarePigs);
    }

    public static Identifier getSheepTexture(SheepEntity entity) {
        DyeColor fleeceColor = entity.getColor();
        return sheep.getOrDefault(fleeceColor, DEFAULT_SHEEP);
    }

    public static Identifier getSnowGolemTexture(SnowGolemEntity entity) {
        return getRandomTexture(entity, snowGolems, ImmutableList.of());
    }

    public static Identifier getSquidTexture(SquidEntity entity) {
        return getRandomTexture(entity, squids, rareSquids);
    }

    public static Identifier getWolfTexture(WolfEntity entity) {
        Identifier res = getRandomTexture(entity, wolves, rareWolves);

        if (entity.isTamed()) {
            res = wolvesTame.get(res);
        } else if (entity.isUniversallyAngry(entity.world)) {
            res = wolvesAngry.get(res);
        }

        return res;
    }

    public static Identifier getRandomTexture(Entity entity, List<Identifier> normalSet, List<Identifier> rareSet) {
        UUID id = entity.getUuid();
        boolean isRare = VariantMobTextures.rareVariants && !rareSet.isEmpty() && (id.getLeastSignificantBits() + id.getMostSignificantBits()) % VariantMobTextures.rarity == 0;

        List<Identifier> set = isRare ? rareSet : normalSet;
        int choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    private Identifier createResource(MobType type, String texture) {
        return new Identifier(Charm.MOD_ID, PREFIX + type.asString() + "/" + texture + ".png");
    }

    public enum MobType implements ICharmEnum { WOLF, COW, PIG, CHICKEN, SQUID, SHEEP, SNOW_GOLEM }
}
