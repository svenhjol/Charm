package svenhjol.charm.module.mooblooms;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.event.AddEntityCallback;
import svenhjol.charm.helper.BiomeHelper;
import svenhjol.charm.helper.MobHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.item.CharmSpawnEggItem;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Mooblooms are cow-like mobs that come in a variety of flower types.\n" +
    "They spawn flowers where they walk and can be milked for suspicious stew.")
public class Mooblooms extends CharmModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "moobloom");
    public static final ResourceLocation TRIGGER_MILKED_MOOBLOOM = new ResourceLocation(Charm.MOD_ID, "milked_moobloom");
    public static EntityType<MoobloomEntity> MOOBLOOM;
    public static Item SPAWN_EGG;

    @Override
    public void register() {
        MOOBLOOM = CommonRegistry.entity(ID, FabricEntityTypeBuilder
            .create(MobCategory.CREATURE, MoobloomEntity::new)
            .dimensions(EntityDimensions.fixed(0.9F, 1.4F))
            .trackRangeChunks(10));

        SpawnRestrictionAccessor.callRegister(MOOBLOOM, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, MoobloomEntity::canSpawn);

        // create a spawn egg for the moobloom
        SPAWN_EGG = new CharmSpawnEggItem(this, "moobloom_spawn_egg", MOOBLOOM, 0xFFFF00, 0xFFFFFF);
        MobHelper.setEntityAttributes(MOOBLOOM, Cow.createAttributes());
    }

    @Override
    public void runWhenEnabled() {
        // add goals to any spawned bees
        AddEntityCallback.EVENT.register(this::tryAddGoalsToBee);

        // add the mooblooms to flower forest biomes
        List<ResourceKey<Biome>> biomes = new ArrayList<>(Collections.singletonList(Biomes.FLOWER_FOREST));
        biomes.forEach(biomeKey -> BiomeHelper.addSpawnEntry(biomeKey, MobCategory.CREATURE, MOOBLOOM, 30, 2, 4));
    }

    private InteractionResult tryAddGoalsToBee(Entity entity) {
        if (entity instanceof Bee bee) {
            if (bee.getGoalSelector().getAvailableGoals().stream().noneMatch(g -> g.getGoal() instanceof BeeMoveToMoobloomGoal)) {
                bee.getGoalSelector().addGoal(4, new BeeMoveToMoobloomGoal(bee));
            }
        }

        return InteractionResult.PASS;
    }

    public static void triggerMilkedMoobloom(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_MILKED_MOOBLOOM);
    }
}
