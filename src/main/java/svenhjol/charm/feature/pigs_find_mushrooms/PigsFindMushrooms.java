package svenhjol.charm.feature.pigs_find_mushrooms;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.MobAccessor;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony_api.event.EntityJoinEvent;
import svenhjol.charmony.base.CharmFeature;

import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Pigs have a chance to find mushrooms from mycelium and podzol blocks.")
public class PigsFindMushrooms extends CharmFeature {
    /**
     * Tracks the animation ticks for a pig UUID.
     */
    public static final WeakHashMap<UUID, Integer> PIG_ANIMATION_TICKS = new WeakHashMap<>();
    public static TagKey<Block> validBlocks;
    public static Supplier<SoundEvent> sniffingSound;

    @Configurable(
        name = "Chance to find mushroom",
        description = "Approximately 1 in X chance of a pig finding a mushroom per game tick."
    )
    public static int findChance = 1000;

    @Configurable(
        name = "Chance to erode block",
        description = "Chance (out of 1.0) of a block being converted to dirt when a pig finds a mushroom."
    )
    public static double erodeChance = 0.25d;

    @Override
    public void register() {
        sniffingSound = Charm.instance().registry().soundEvent("pig_sniffing");
        validBlocks = TagKey.create(BuiltInRegistries.BLOCK.key(),
            Charm.instance().makeId("pigs_find_mushrooms"));
    }

    @Override
    public void runWhenEnabled() {
        EntityJoinEvent.INSTANCE.handle(this::handleEntityJoin);
    }

    private void handleEntityJoin(Entity entity, Level level) {
        if (entity instanceof Pig pig) {
            var goalSelector = ((MobAccessor) pig).getGoalSelector();
            if (goalSelector.getAvailableGoals().stream().noneMatch(
                g -> g.getGoal() instanceof FindMushroomGoal)) {
                goalSelector.addGoal(3, new FindMushroomGoal(pig));
            }
        }
    }

    public static float getHeadEatPositionScale(Pig pig, float f) {
        var tick = PIG_ANIMATION_TICKS.getOrDefault(pig.getUUID(), 0);
        if (tick <= 0) {
            return 0;
        }
        if (tick >= 4 && tick <= 36) {
            return 1;
        }
        if (tick < 4) {
            return ((float)tick - f) / 4.0f;
        }
        return -((float)(tick - 40) - f) / 4.0f;
    }

    public static float getHeadEatAngleScale(Pig pig, float f) {
        var tick = PIG_ANIMATION_TICKS.getOrDefault(pig.getUUID(), 0);
        if (tick > 4 && tick <= 36) {
            float g = ((float)(tick - 4) - f) / 32.0f;
            return 0.63f + 0.22f * Mth.sin(g * 28.7f);
        }
        if (tick > 0) {
            return 0.63f;
        }
        return pig.getXRot() * ((float)Math.PI / 180);
    }
}
