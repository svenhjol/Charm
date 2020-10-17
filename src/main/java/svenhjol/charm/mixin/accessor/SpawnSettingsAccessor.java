package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.SpawnSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(SpawnSettings.class)
public interface SpawnSettingsAccessor {
    @Accessor
    Map<SpawnGroup, List<SpawnSettings.SpawnEntry>> getSpawners();

    @Accessor
    Map<EntityType<?>, SpawnSettings.SpawnDensity> getSpawnCosts();

    @Accessor
    void setSpawners(Map<SpawnGroup, List<SpawnSettings.SpawnEntry>> spawners);

    @Accessor
    void setSpawnCosts(Map<EntityType<?>, SpawnSettings.SpawnDensity> spawnCosts);
}
