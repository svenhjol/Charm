package svenhjol.charm.module.repair_elytra_from_leather;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.CheckAnvilRepairCallback;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Leather can be used to repair elytra when insomnia is disabled.")
public class RepairElytraFromLeather extends CharmModule {
    @Config(name = "Allow when insomnia enabled", description = "If true, leather can be used to repair elytra even when insomnia is disasbled.")
    public static boolean forceAllow = false;

    @Override
    public void runWhenEnabled() {
        CheckAnvilRepairCallback.EVENT.register(this::handleCheckAnvilRepair);
    }

    private boolean handleCheckAnvilRepair(AnvilMenu menu, Player player, ItemStack left, ItemStack right) {
        if (left.getItem() != Items.ELYTRA || player == null || player.level == null) {
            return false; // false to bypass
        }

        // don't activate if insomnia is enabled
        if (!forceAllow && !player.level.isClientSide && player.level.getGameRules().getBoolean(GameRules.RULE_DOINSOMNIA)) {
            return false; // false to explicitly deny repair if insomnia is enabled
        }

        return right.getItem() == Items.LEATHER;
    }
}
