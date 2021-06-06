package svenhjol.charm.module.variant_mob_textures;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.enums.ICharmEnum;
import svenhjol.charm.event.ClientPlayerJoinCallback;
import svenhjol.charm.module.variant_mob_textures.VariantMobRenderer;
import svenhjol.charm.module.variant_mob_textures.VariantMobTextures;

import java.util.*;

public class VariantMobTexturesClient extends CharmClientModule {
    private static final String PREFIX = "textures/entity/";
    private static final ResourceLocation DEFAULT_SHEEP = new ResourceLocation(PREFIX + "sheep/sheep.png");

    public static List<ResourceLocation> chickens = new ArrayList<>();
    public static List<ResourceLocation> cows = new ArrayList<>();
    public static List<ResourceLocation> snowGolems = new ArrayList<>();
    public static List<ResourceLocation> squids = new ArrayList<>();
    public static List<ResourceLocation> pigs = new ArrayList<>();
    public static List<ResourceLocation> wolves = new ArrayList<>();

    public static List<ResourceLocation> rareChickens = new ArrayList<>();
    public static List<ResourceLocation> rareCows = new ArrayList<>();
    public static List<ResourceLocation> rareSquids = new ArrayList<>();
    public static List<ResourceLocation> rarePigs = new ArrayList<>();
    public static List<ResourceLocation> rareWolves = new ArrayList<>();

    public static Map<ResourceLocation, ResourceLocation> wolvesTame = new HashMap<>();
    public static Map<ResourceLocation, ResourceLocation> wolvesAngry = new HashMap<>();
    public static Map<DyeColor, ResourceLocation> sheep = new HashMap<>();

    public VariantMobTexturesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        ClientPlayerJoinCallback.EVENT.register(this::handlePlayerJoin);

        if (svenhjol.charm.module.variant_mob_textures.VariantMobTextures.variantChickens)
            EntityRendererRegistry.INSTANCE.register(EntityType.CHICKEN, svenhjol.charm.module.variant_mob_textures.VariantMobRenderer.Chicken::new);

        if (svenhjol.charm.module.variant_mob_textures.VariantMobTextures.variantCows)
            EntityRendererRegistry.INSTANCE.register(EntityType.COW, svenhjol.charm.module.variant_mob_textures.VariantMobRenderer.Cow::new);

        if (svenhjol.charm.module.variant_mob_textures.VariantMobTextures.variantPigs)
            EntityRendererRegistry.INSTANCE.register(EntityType.PIG, svenhjol.charm.module.variant_mob_textures.VariantMobRenderer.Pig::new);

        if (svenhjol.charm.module.variant_mob_textures.VariantMobTextures.variantSheep)
            EntityRendererRegistry.INSTANCE.register(EntityType.SHEEP, svenhjol.charm.module.variant_mob_textures.VariantMobRenderer.Sheep::new);

        if (svenhjol.charm.module.variant_mob_textures.VariantMobTextures.variantSnowGolems)
            EntityRendererRegistry.INSTANCE.register(EntityType.SNOW_GOLEM, svenhjol.charm.module.variant_mob_textures.VariantMobRenderer.SnowGolem::new);

        if (svenhjol.charm.module.variant_mob_textures.VariantMobTextures.variantSquids)
            EntityRendererRegistry.INSTANCE.register(EntityType.SQUID, svenhjol.charm.module.variant_mob_textures.VariantMobRenderer.Squid::new);

        if (svenhjol.charm.module.variant_mob_textures.VariantMobTextures.variantWolves)
            EntityRendererRegistry.INSTANCE.register(EntityType.WOLF, VariantMobRenderer.Wolf::new);

    }

    public void handlePlayerJoin(Minecraft client) {
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
        chickens.add(new ResourceLocation(PREFIX + "chicken.png"));
        cows.add(new ResourceLocation(PREFIX + "cow/cow.png"));
        pigs.add(new ResourceLocation(PREFIX + "pig/pig.png"));
        snowGolems.add(new ResourceLocation(PREFIX + "snow_golem.png"));
        squids.add(new ResourceLocation(PREFIX + "squid/squid.png"));

        ResourceLocation wolf = new ResourceLocation(PREFIX + "wolf/wolf.png");
        wolves.add(wolf);
        wolvesTame.put(wolf, new ResourceLocation(PREFIX + "wolf/wolf_tame.png"));
        wolvesAngry.put(wolf, new ResourceLocation(PREFIX + "wolf/wolf_angry.png"));

        for (int i = 1; i <= 5; i++)
            addCustomTextures(chickens, MobType.CHICKEN, "chicken" + i);

        for (int i = 1; i <= 2; i++)
            addCustomTextures(rareChickens, MobType.CHICKEN, "rare_chicken" + i);

        for (int i = 1; i <= 7; i++)
            addCustomTextures(cows, MobType.COW, "cow" + i);

        for (int i = 1; i <= 1; i++)
            addCustomTextures(rareCows, MobType.COW, "rare_cow" + i);

        for (int i = 1; i <= 4; i++)
            addCustomTextures(pigs, MobType.PIG, "pig" + i);

        for (int i = 1; i <= 2; i++)
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
            ResourceLocation res = createResource(MobType.SHEEP, "sheep_" + color.toString());
            sheep.put(color, res);
        }
    }

    public void addCustomTextures(List<ResourceLocation> set, MobType type, String... names) {
        ArrayList<String> textures = new ArrayList<>(Arrays.asList(names));

        textures.forEach(texture -> {
            ResourceLocation res = createResource(type, texture);
            set.add(res);

            if (type == MobType.WOLF) {
                wolvesTame.put(res, createResource(type, texture + "_tame"));
                wolvesAngry.put(res, createResource(type, texture + "_angry"));
            }
        });
    }

    public static ResourceLocation getChickenTexture(Chicken entity) {
        return getRandomTexture(entity, chickens, rareChickens);
    }

    public static ResourceLocation getCowTexture(Cow entity) {
        return getRandomTexture(entity, cows, rareCows);
    }

    public static ResourceLocation getPigTexture(Pig entity) {
        return getRandomTexture(entity, pigs, rarePigs);
    }

    public static ResourceLocation getSheepTexture(Sheep entity) {
        DyeColor fleeceColor = entity.getColor();
        return sheep.getOrDefault(fleeceColor, DEFAULT_SHEEP);
    }

    public static ResourceLocation getSnowGolemTexture(SnowGolem entity) {
        return getRandomTexture(entity, snowGolems, ImmutableList.of());
    }

    public static ResourceLocation getSquidTexture(Squid entity) {
        return getRandomTexture(entity, squids, rareSquids);
    }

    public static ResourceLocation getWolfTexture(Wolf entity) {
        ResourceLocation res = getRandomTexture(entity, wolves, rareWolves);

        if (entity.isTame()) {
            res = wolvesTame.get(res);
        } else if (entity.isAngryAtAllPlayers(entity.level)) {
            res = wolvesAngry.get(res);
        }

        return res;
    }

    public static ResourceLocation getRandomTexture(Entity entity, List<ResourceLocation> normalSet, List<ResourceLocation> rareSet) {
        UUID id = entity.getUUID();
        boolean isRare = svenhjol.charm.module.variant_mob_textures.VariantMobTextures.rareVariants && !rareSet.isEmpty() && (id.getLeastSignificantBits() + id.getMostSignificantBits()) % VariantMobTextures.rarity == 0;

        List<ResourceLocation> set = isRare ? rareSet : normalSet;
        int choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    private ResourceLocation createResource(MobType type, String texture) {
        return new ResourceLocation(Charm.MOD_ID, PREFIX + type.getSerializedName() + "/" + texture + ".png");
    }

    public enum MobType implements ICharmEnum { WOLF, COW, PIG, CHICKEN, SQUID, SHEEP, SNOW_GOLEM }
}
