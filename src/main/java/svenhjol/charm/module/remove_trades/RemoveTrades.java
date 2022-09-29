package svenhjol.charm.module.remove_trades;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, enabledByDefault = false, description = "Removes some vanilla trades that allow infinite emeralds.")
public class RemoveTrades extends CharmModule {
    @Override
    public void runWhenEnabled() {
        VillagerHelper.removeTrade(VillagerProfession.LIBRARIAN, 3, listing
            -> listing instanceof VillagerTrades.ItemsForEmeralds itemsForEmeralds
            && itemsForEmeralds.itemStack != null && itemsForEmeralds.itemStack.is(Items.GLASS));
    }
}
