package svenhjol.charm.mixin.accessor;

import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(MobSpawnSettings.class)
public interface MobSpawnSettingsAccessor {
    @Mutable @Accessor
    Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> getSpawners();

    @Mutable @Accessor
    Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getMobSpawnCosts();

    @Mutable @Accessor
    void setSpawners(Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawners);

    @Mutable @Accessor
    void setMobSpawnCosts(Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> spawnCosts);
}
