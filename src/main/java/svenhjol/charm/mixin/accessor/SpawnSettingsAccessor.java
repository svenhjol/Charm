package svenhjol.charm.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

@Mixin(MobSpawnSettings.class)
@CharmMixin(required = true)
public interface SpawnSettingsAccessor {
    @Mutable
    @Accessor
    Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> getSpawners();

    @Mutable
    @Accessor
    Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> getSpawnCosts();

    @Mutable
    @Accessor
    void setSpawners(Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawners);

    @Mutable
    @Accessor
    void setSpawnCosts(Map<EntityType<?>, MobSpawnSettings.MobSpawnCost> spawnCosts);
}
