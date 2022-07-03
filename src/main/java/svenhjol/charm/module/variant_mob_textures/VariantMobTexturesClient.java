package svenhjol.charm.module.variant_mob_textures;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.enums.ICharmEnum;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.*;

@ClientModule(module = VariantMobTextures.class)
public class VariantMobTexturesClient extends CharmModule {
    private static final String PREFIX = "textures/entity/";
    private static final ResourceLocation DEFAULT_SHEEP = new ResourceLocation(PREFIX + "sheep/sheep.png");

    public static List<ResourceLocation> chickens = new ArrayList<>();
    public static List<ResourceLocation> cows = new ArrayList<>();
    public static List<ResourceLocation> dolphins = new ArrayList<>();
    public static List<ResourceLocation> snowGolems = new ArrayList<>();
    public static List<ResourceLocation> squids = new ArrayList<>();
    public static List<ResourceLocation> pigs = new ArrayList<>();
    public static List<ResourceLocation> turtles = new ArrayList<>();
    public static List<ResourceLocation> wolves = new ArrayList<>();
    public static List<ResourceLocation> wanderingTraders = new ArrayList<>();

    public static List<ResourceLocation> rareChickens = new ArrayList<>();
    public static List<ResourceLocation> rareCows = new ArrayList<>();
    public static List<ResourceLocation> rareDolphins = new ArrayList<>();
    public static List<ResourceLocation> rareSquids = new ArrayList<>();
    public static List<ResourceLocation> rarePigs = new ArrayList<>();
    public static List<ResourceLocation> rareTurtles = new ArrayList<>();
    public static List<ResourceLocation> rareWolves = new ArrayList<>();

    public static Map<ResourceLocation, ResourceLocation> wolvesTame = new HashMap<>();
    public static Map<ResourceLocation, ResourceLocation> wolvesAngry = new HashMap<>();
    public static Map<DyeColor, ResourceLocation> sheep = new HashMap<>();

    @Override
    public void runWhenEnabled() {
        ClientEntityEvents.ENTITY_LOAD.register(this::handlePlayerJoin);

        if (VariantMobTextures.variantChickens) {
            EntityRendererRegistry.register(EntityType.CHICKEN, VariantMobRenderer.RenderChicken::new);
        }

        if (VariantMobTextures.variantCows) {
            EntityRendererRegistry.register(EntityType.COW, VariantMobRenderer.RenderCow::new);
        }

        if (VariantMobTextures.variantDolphins) {
            EntityRendererRegistry.register(EntityType.DOLPHIN, VariantMobRenderer.RenderDolphin::new);
        }

        if (VariantMobTextures.variantPigs) {
            EntityRendererRegistry.register(EntityType.PIG, VariantMobRenderer.RenderPig::new);
        }

        if (VariantMobTextures.variantSheep) {
            EntityRendererRegistry.register(EntityType.SHEEP, VariantMobRenderer.RenderSheep::new);
        }

        if (VariantMobTextures.variantSnowGolems) {
            EntityRendererRegistry.register(EntityType.SNOW_GOLEM, VariantMobRenderer.RenderSnowGolem::new);
        }

        if (VariantMobTextures.variantSquids) {
            EntityRendererRegistry.register(EntityType.SQUID, VariantMobRenderer.RenderSquid::new);
        }

        if (VariantMobTextures.variantTurtles) {
            EntityRendererRegistry.register(EntityType.TURTLE, VariantMobRenderer.RenderTurtle::new);
        }

        if (VariantMobTextures.variantWolves) {
            EntityRendererRegistry.register(EntityType.WOLF, VariantMobRenderer.RenderWolf::new);
        }

        if (VariantMobTextures.variantWanderingTraders) {
            EntityRendererRegistry.register(EntityType.WANDERING_TRADER, VariantMobRenderer.RenderWanderingTrader::new);
        }
    }

    public void handlePlayerJoin(Entity entity, Level level) {
        if (!(entity instanceof LocalPlayer player)) return;

        // reset
        chickens = new ArrayList<>();
        cows = new ArrayList<>();
        dolphins = new ArrayList<>();
        pigs = new ArrayList<>();
        snowGolems = new ArrayList<>();
        squids = new ArrayList<>();
        turtles = new ArrayList<>();
        wolves = new ArrayList<>();
        wolvesTame = new HashMap<>();
        wolvesAngry = new HashMap<>();
        wanderingTraders = new ArrayList<>();

        rareChickens = new ArrayList<>();
        rareCows = new ArrayList<>();
        rareDolphins = new ArrayList<>();
        rarePigs = new ArrayList<>();
        rareSquids = new ArrayList<>();
        rareTurtles = new ArrayList<>();
        rareWolves = new ArrayList<>();

        // add vanilla textures
        chickens.add(new ResourceLocation(PREFIX + "chicken.png"));
        cows.add(new ResourceLocation(PREFIX + "cow/cow.png"));
        dolphins.add(new ResourceLocation(PREFIX + "dolphin.png"));
        pigs.add(new ResourceLocation(PREFIX + "pig/pig.png"));
        snowGolems.add(new ResourceLocation(PREFIX + "snow_golem.png"));
        squids.add(new ResourceLocation(PREFIX + "squid/squid.png"));
        turtles.add(new ResourceLocation(PREFIX + "turtle/big_sea_turtle.png"));
        wanderingTraders.add(new ResourceLocation(PREFIX + "wandering_trader.png"));

        ResourceLocation wolf = new ResourceLocation(PREFIX + "wolf/wolf.png");
        wolves.add(wolf);
        wolvesTame.put(wolf, new ResourceLocation(PREFIX + "wolf/wolf_tame.png"));
        wolvesAngry.put(wolf, new ResourceLocation(PREFIX + "wolf/wolf_angry.png"));

        for (int i = 1; i <= 5; i++) {
            addCustomTextures(chickens, MobType.CHICKEN, "chicken" + i);
        }

        for (int i = 1; i <= 2; i++) {
            addCustomTextures(rareChickens, MobType.CHICKEN, "rare_chicken" + i);
        }

        for (int i = 1; i <= 3; i++) {
            addCustomTextures(dolphins, MobType.DOLPHIN, "dolphin" + i);
        }

        for (int i = 1; i <= 2; i++) {
            addCustomTextures(rareDolphins, MobType.DOLPHIN, "rare_dolphin" + i);
        }

        for (int i = 1; i <= 3; i++) {
            addCustomTextures(turtles, MobType.TURTLE, "turtle" + i);
        }

        for (int i = 1; i <= 1; i++) {
            addCustomTextures(rareTurtles, MobType.TURTLE, "rare_turtle" + i);
        }

        for (int i = 1; i <= 7; i++) {
            addCustomTextures(cows, MobType.COW, "cow" + i);
        }

        for (int i = 1; i <= 4; i++) {
            addCustomTextures(wanderingTraders, MobType.WANDERING_TRADER, "wandering_trader" + i);
        }

        for (int i = 1; i <= 1; i++) {
            addCustomTextures(rareCows, MobType.COW, "rare_cow" + i);
        }

        for (int i = 1; i <= 4; i++) {
            addCustomTextures(pigs, MobType.PIG, "pig" + i);
        }

        for (int i = 1; i <= 2; i++) {
            addCustomTextures(rarePigs, MobType.PIG, "rare_pig" + i);
        }

        for (int i = 1; i <= 5; i++) {
            addCustomTextures(snowGolems, MobType.SNOW_GOLEM, "snow_golem" + i);
        }

        for (int i = 1; i <= 4; i++) {
            addCustomTextures(squids, MobType.SQUID, "squid" + i);
        }

        for (int i = 1; i <= 1; i++) {
            addCustomTextures(rareSquids, MobType.SQUID, "rare_squid" + i);
        }

        for (int i = 1; i <= 25; i++) {
            addCustomTextures(wolves, MobType.WOLF, "nlg_wolf" + i); // add NeverLoseGuy wolf textures
        }

        for (int i = 1; i <= 1; i++) {
            addCustomTextures(rareWolves, MobType.WOLF, "rare_wolf" + i);
        }

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

    @Nullable
    public static ResourceLocation getChickenTexture(Chicken entity) {
        return getRandomTexture(entity, chickens, rareChickens);
    }

    @Nullable
    public static ResourceLocation getCowTexture(Cow entity) {
        return getRandomTexture(entity, cows, rareCows);
    }

    @Nullable
    public static ResourceLocation getDolphinTexture(Dolphin entity) {
        return getRandomTexture(entity, dolphins, rareDolphins);
    }

    @Nullable
    public static ResourceLocation getPigTexture(Pig entity) {
        return getRandomTexture(entity, pigs, rarePigs);
    }

    public static ResourceLocation getSheepTexture(Sheep entity) {
        DyeColor fleeceColor = entity.getColor();
        return sheep.getOrDefault(fleeceColor, DEFAULT_SHEEP);
    }

    @Nullable
    public static ResourceLocation getSnowGolemTexture(SnowGolem entity) {
        return getRandomTexture(entity, snowGolems, ImmutableList.of());
    }

    @Nullable
    public static ResourceLocation getWanderingTraderTexture(WanderingTrader entity) {
        return getRandomTexture(entity, wanderingTraders, ImmutableList.of());
    }

    @Nullable
    public static ResourceLocation getSquidTexture(Squid entity) {
        return getRandomTexture(entity, squids, rareSquids);
    }

    @Nullable
    public static ResourceLocation getTurtleTexture(Turtle entity) {
        return getRandomTexture(entity, turtles, rareTurtles);
    }

    @Nullable
    public static ResourceLocation getWolfTexture(Wolf entity) {
        ResourceLocation res = getRandomTexture(entity, wolves, rareWolves);
        if (res == null)
            return null;

        if (entity.isTame()) {
            res = wolvesTame.get(res);
        } else if (entity.isAngryAtAllPlayers(entity.level)) {
            res = wolvesAngry.get(res);
        }

        return res;
    }

    @Nullable
    public static ResourceLocation getRandomTexture(Entity entity, List<ResourceLocation> normalSet, List<ResourceLocation> rareSet) {
        UUID id = entity.getUUID();
        boolean isRare = VariantMobTextures.rareVariants && !rareSet.isEmpty() && (id.getLeastSignificantBits() + id.getMostSignificantBits()) % VariantMobTextures.rarity == 0;

        List<ResourceLocation> set = isRare ? rareSet : normalSet;
        if (set.isEmpty()) return null;

        int choice = Math.abs((int)(id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    private ResourceLocation createResource(MobType type, String texture) {
        return new ResourceLocation(Charm.MOD_ID, PREFIX + type.getSerializedName() + "/" + texture + ".png");
    }

    public enum MobType implements ICharmEnum { WOLF, COW, PIG, CHICKEN, SQUID, SHEEP, WANDERING_TRADER, TURTLE, SNOW_GOLEM, DOLPHIN }
}
