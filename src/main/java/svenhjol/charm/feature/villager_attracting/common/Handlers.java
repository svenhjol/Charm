package svenhjol.charm.feature.villager_attracting.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.feature.villager_attracting.VillagerAttracting;
import svenhjol.charm.charmony.feature.FeatureHolder;

import java.util.function.Predicate;

public final class Handlers extends FeatureHolder<VillagerAttracting> {
    public Handlers(VillagerAttracting feature) {
        super(feature);
    }

    @SuppressWarnings("UnusedReturnValue")
    public InteractionResult entityJoin(Entity entity, Level level) {
        if (entity instanceof Villager villager) {
            Predicate<ItemStack> ingredient = stack -> stack.is(Tags.VILLAGER_LOVED);
            var goalSelector = villager.goalSelector;

            if (goalSelector.getAvailableGoals().stream().noneMatch(g -> g.getGoal() instanceof TemptGoal)) {
                goalSelector.addGoal(3, new TemptGoal(villager, 0.6, ingredient, false));
            }
        }

        return InteractionResult.PASS;
    }

    public void playerTick(Player player) {
        if (!player.level().isClientSide
            && player.level().getGameTime() % 40 == 0
            && player.getMainHandItem().is(Tags.VILLAGER_LOVED)
        ) {
            var villagers = player.level().getEntitiesOfClass(Villager.class, new AABB(player.blockPosition()).inflate(8.0D));

            // Do advancement trigger.
            if (!villagers.isEmpty()) {
                feature().advancements.attractedVillager((ServerPlayer) player);
            }
        }
    }
}
