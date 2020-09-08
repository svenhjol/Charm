package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.ActionResult;
import svenhjol.meson.event.AddEntityCallback;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.MobHelper;
import svenhjol.meson.iface.Module;

@Module(description = "Villagers are attracted when the player holds a block of emeralds.")
public class VillagersFollowEmeraldBlocks extends MesonModule {
    @Override
    public void afterInit() {
        AddEntityCallback.EVENT.register((entity -> {
            followEmerald(entity);
            return ActionResult.PASS;
        }));
    }

    private void followEmerald(Entity entity) {
        if (entity instanceof VillagerEntity) {
            VillagerEntity villager = (VillagerEntity) entity;

            Ingredient ingredient = Ingredient.ofItems(Blocks.EMERALD_BLOCK);

            if (MobHelper.getGoals(villager).stream().noneMatch(g -> g.getGoal() instanceof TemptGoal))
                MobHelper.getGoalSelector(villager).add(3, new TemptGoal(villager, 0.6, ingredient, false));
        }
    }
}
