package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;

@Mixin(SpawnSettings.class)
@CharmMixin(required = true)
public interface SpawnSettingsAccessor {
    @Mutable
    @Accessor
    Map<SpawnGroup, Pool<SpawnSettings.SpawnEntry>> getSpawners();

    @Mutable
    @Accessor
    Map<EntityType<?>, SpawnSettings.SpawnDensity> getSpawnCosts();

    @Mutable
    @Accessor
    void setSpawners(Map<SpawnGroup, Pool<SpawnSettings.SpawnEntry>> spawners);

    @Mutable
    @Accessor
    void setSpawnCosts(Map<EntityType<?>, SpawnSettings.SpawnDensity> spawnCosts);
}
