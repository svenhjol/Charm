package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.MobHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.init.CharmAdvancements;

import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Villagers are attracted when the player holds a block of emeralds.",
    requiresMixins = {"AddEntityCallback", "PlayerTickCallback"})
public class VillagersFollowEmeraldBlocks extends CharmModule {
    public static final Identifier TRIGGER_LURED_VILLAGER = new Identifier(Charm.MOD_ID, "lured_villager");

    @Override
    public void init() {
        AddEntityCallback.EVENT.register(this::followEmerald);
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
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

    private void handlePlayerTick(PlayerEntity player) {
        if (!player.world.isClient
            && player.world.getTime() % 40 == 0
            && player.getMainHandStack().getItem() == Items.EMERALD_BLOCK
        ) {
            List<VillagerEntity> villagers = player.world.getNonSpectatingEntities(VillagerEntity.class, new Box(player.getBlockPos()).expand(8.0D));
            if (!villagers.isEmpty())
                triggerLuredVillager((ServerPlayerEntity) player);
        }
    }

    public static void triggerLuredVillager(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_LURED_VILLAGER);
    }
}
