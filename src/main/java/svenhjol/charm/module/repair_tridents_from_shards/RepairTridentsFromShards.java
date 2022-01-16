package svenhjol.charm.module.repair_tridents_from_shards;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.api.event.CheckAnvilRepairCallback;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Use prismarine shards to repair a small amount of trident damage.")
public class RepairTridentsFromShards extends CharmModule {
    @Override
    public void runWhenEnabled() {
        CheckAnvilRepairCallback.EVENT.register(this::handleCheckAnvilRepair);
    }

    private boolean handleCheckAnvilRepair(AnvilMenu menu, Player player, ItemStack left, ItemStack right) {
        if (left.getItem() != Items.TRIDENT || player == null || player.level == null) {
            return false; // to bypass
        }

        return right.getItem() == Items.PRISMARINE_SHARD;
    }
}
