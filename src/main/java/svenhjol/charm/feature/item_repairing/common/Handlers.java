package svenhjol.charm.feature.item_repairing.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import svenhjol.charm.feature.item_repairing.ItemRepairing;
import svenhjol.charm.charmony.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<ItemRepairing> {
    public Handlers(ItemRepairing feature) {
        super(feature);
    }

    @SuppressWarnings({"RedundantIfStatement", "unused"})
    public boolean anvilRepair(AnvilMenu menu, Player player, ItemStack leftStack, ItemStack rightStack) {
        if (feature().repairTridents() && leftStack.is(Items.TRIDENT) && rightStack.is(Items.PRISMARINE_SHARD)) {
            return true;
        }

        if (feature().repairElytra() && !player.level().getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA)
            && leftStack.is(Items.ELYTRA) && rightStack.is(Items.LEATHER)) {
            return true;
        }

        if (feature().repairNetheriteItems() && leftStack.is(Tags.REPAIRABLE_USING_SCRAP) && rightStack.is(Items.NETHERITE_SCRAP)) {
            return true;
        }

        return false;
    }
}
