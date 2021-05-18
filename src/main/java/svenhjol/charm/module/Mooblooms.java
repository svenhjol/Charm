package svenhjol.charm.module;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.mixin.object.builder.SpawnRestrictionAccessor;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.BiomeHelper;
import svenhjol.charm.base.helper.MobHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.MoobloomsClient;
import svenhjol.charm.entity.MoobloomEntity;
import svenhjol.charm.entity.goal.BeeMoveToMoobloomGoal;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.init.CharmAdvancements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Module(mod = Charm.MOD_ID, client = MoobloomsClient.class, description = "Mooblooms are cow-like mobs that come in a variety of flower types. They spawn flowers where they walk and can be milked for suspicious stew.",
    requiresMixins = {"AddEntityCallback"})
public class Mooblooms extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "moobloom");
    public static final Identifier EGG_ID = new Identifier(Charm.MOD_ID, "moobloom_spawn_egg");
    public static final Identifier TRIGGER_MILKED_MOOBLOOM = new Identifier(Charm.MOD_ID, "milked_moobloom");
    public static EntityType<MoobloomEntity> MOOBLOOM;
    public static Item SPAWN_EGG;

    @Override
    public void register() {
        MOOBLOOM = RegistryHandler.entity(ID, FabricEntityTypeBuilder
            .create(SpawnGroup.CREATURE, MoobloomEntity::new)
            .dimensions(EntityDimensions.fixed(0.9F, 1.4F))
            .trackRangeBlocks(10));

        SpawnRestrictionAccessor.callRegister(MOOBLOOM, SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MoobloomEntity::canSpawn);

        // create a spawn egg for the moobloom
        SPAWN_EGG = RegistryHandler.item(EGG_ID, new SpawnEggItem(MOOBLOOM, 0xFFFF00, 0xFFFFFF, (new Item.Settings()).group(ItemGroup.MISC)));

        MobHelper.setEntityAttributes(MOOBLOOM, CowEntity.createCowAttributes());
    }

    @Override
    public void init() {
        // add goals to any spawned bees
        AddEntityCallback.EVENT.register(this::tryAddGoalsToBee);

        // add the mooblooms to flower forest biomes
        List<RegistryKey<Biome>> biomes = new ArrayList<>(Collections.singletonList(BiomeKeys.FLOWER_FOREST));

        biomes.forEach(biomeKey -> {
            BiomeHelper.addSpawnEntry(biomeKey, SpawnGroup.CREATURE, MOOBLOOM, 30, 2, 4);
        });
    }

    private ActionResult tryAddGoalsToBee(Entity entity) {
        if (entity instanceof BeeEntity) {
            BeeEntity bee = (BeeEntity)entity;
            if (MobHelper.getGoals(bee).stream().noneMatch(g -> g.getGoal() instanceof BeeMoveToMoobloomGoal))
                MobHelper.getGoalSelector(bee).add(4, new BeeMoveToMoobloomGoal(bee));
        }

        return ActionResult.PASS;
    }

    public static void triggerMilkedMoobloom(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_MILKED_MOOBLOOM);
    }
}
