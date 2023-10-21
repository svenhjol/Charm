package svenhjol.charm.feature.pigs_find_mushrooms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.PlayerHelper;
import svenhjol.charmony_api.event.EntityJoinEvent;

import java.util.UUID;
import java.util.WeakHashMap;
import java.util.function.Supplier;

public class PigsFindMushrooms extends CommonFeature {
    /**
     * Tracks the animation ticks for a pig UUID.
     */
    public static final WeakHashMap<UUID, Integer> PIG_ANIMATION_TICKS = new WeakHashMap<>();
    public static TagKey<Block> validBlocks;
    public static Supplier<SoundEvent> sniffingSound;

    @Configurable(
        name = "Chance to find mushroom",
        description = "Approximately 1 in X chance of a pig finding a mushroom per game tick.",
        requireRestart = false
    )
    public static int findChance = 1000;

    @Configurable(
        name = "Chance to erode block",
        description = "Chance (out of 1.0) of a block being converted to dirt when a pig finds a mushroom.",
        requireRestart = false
    )
    public static double erodeChance = 0.25d;

    @Override
    public String description() {
        return "Pigs have a chance to find mushrooms from mycelium and podzol blocks.";
    }

    @Override
    public void register() {
        sniffingSound = mod().registry().soundEvent("pig_sniffing");
        validBlocks = TagKey.create(BuiltInRegistries.BLOCK.key(),
            mod().id("pigs_find_mushrooms"));
    }

    @Override
    public void runWhenEnabled() {
        EntityJoinEvent.INSTANCE.handle(this::handleEntityJoin);
    }

    private void handleEntityJoin(Entity entity, Level level) {
        if (entity instanceof Pig pig) {
            var goalSelector = pig.goalSelector;
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

    public static void triggerUnearthedMushroom(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> Advancements.trigger(new ResourceLocation(Charm.ID, "unearthed_mushroom"), player));
    }
}
