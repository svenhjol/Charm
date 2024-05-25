package svenhjol.charm.feature.piglin_pointing.common;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.levelgen.structure.Structure;
import svenhjol.charm.feature.piglin_pointing.PiglinPointing;
import svenhjol.charm.foundation.common.CommonRegistry;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.ArrayList;
import java.util.List;

public final class Registers extends RegisterHolder<PiglinPointing> {
    public final EntityDataAccessor<Boolean> entityDataIsPointing;
    public final CommonRegistry.Register<MemoryModuleType<BlockPos>> pointingAtTarget;
    public final List<Pair<TagKey<Item>, TagKey<Structure>>> DIRECTION_BARTERING = new ArrayList<>();

    public Registers(PiglinPointing feature) {
        super(feature);

        entityDataIsPointing = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
        pointingAtTarget = feature.registry().memoryModuleType("charm_pointing_at_target");

        registerDirectionBartering(Tags.PIGLIN_BARTERS_FOR_BASTIONS, Tags.PIGLIN_BASTION_LOCATED);
        registerDirectionBartering(Tags.PIGLIN_BARTERS_FOR_FORTRESSES, Tags.PIGLIN_FORTRESS_LOCATED);
    }

    @Override
    public void onEnabled() {
        // Weaken memory types so we can add our custom one in.
        var memoryTypes = new ArrayList<>(Piglin.MEMORY_TYPES);
        memoryTypes.add(pointingAtTarget.get());
        Piglin.MEMORY_TYPES = ImmutableList.copyOf(memoryTypes);
    }

    public void registerDirectionBartering(TagKey<Item> items, TagKey<Structure> structure) {
        DIRECTION_BARTERING.add(Pair.of(items, structure));
    }
}
