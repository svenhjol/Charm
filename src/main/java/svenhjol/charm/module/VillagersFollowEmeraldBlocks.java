package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.ActionResult;
import svenhjol.charm.Charm;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.MobHelper;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Villagers are attracted when the player holds a block of emeralds.")
public class VillagersFollowEmeraldBlocks extends CharmModule {
    @Override
    public void init() {
        AddEntityCallback.EVENT.register(this::followEmerald);
    }

    private ActionResult followEmerald(Entity entity) {
        if (entity instanceof VillagerEntity) {
            VillagerEntity villager = (VillagerEntity) entity;

            Ingredient ingredient = Ingredient.ofItems(Blocks.EMERALD_BLOCK);

            if (MobHelper.getGoals(villager).stream().noneMatch(g -> g.getGoal() instanceof TemptGoal))
                MobHelper.getGoalSelector(villager).add(3, new TemptGoal(villager, 0.6, ingredient, false));
        }

        return ActionResult.PASS;
    }
}
