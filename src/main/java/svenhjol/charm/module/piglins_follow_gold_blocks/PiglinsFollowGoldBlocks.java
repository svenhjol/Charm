package svenhjol.charm.module.piglins_follow_gold_blocks;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.helper.MobHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Piglins are attracted when the player holds a block of gold.")
public class PiglinsFollowGoldBlocks extends CharmModule {
    public static final ResourceLocation TRIGGER_LURED_PIGLIN = new ResourceLocation(Charm.MOD_ID, "lured_piglin");

    @Override
    public void runWhenEnabled() {
        AddEntityCallback.EVENT.register(this::followGold);
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    private InteractionResult followGold(Entity entity) {
        if (entity instanceof Piglin) {
            Piglin piglin = (Piglin) entity;

            Ingredient ingredient = Ingredient.of(Blocks.GOLD_BLOCK);

            if (MobHelper.getGoals(piglin).stream().noneMatch(g -> g.getGoal() instanceof TemptGoal))
                MobHelper.getGoalSelector(piglin).addGoal(1, new TemptGoal(piglin, 0.6, ingredient, false));
        }

        return InteractionResult.PASS;
    }

    private void handlePlayerTick(Player player) {
        if (!player.level.isClientSide
                && player.level.getGameTime() % 40 == 0
                && player.getMainHandItem().getItem() == Items.GOLD_BLOCK
        ) {
            List<Piglin> piglins = player.level.getEntitiesOfClass(Piglin.class, new AABB(player.blockPosition()).inflate(8.0D));
            if (!piglins.isEmpty())
                triggerLuredPiglin((ServerPlayer) player);
        }
    }

    public static void triggerLuredPiglin(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_LURED_PIGLIN);
    }
}
