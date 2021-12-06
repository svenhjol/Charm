package svenhjol.charm.module.totem_works_from_inventory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "A totem will work from anywhere in the player's inventory as well as held in the main or offhand.")
public class TotemWorksFromInventory extends CharmModule {
    public static ItemStack tryUsingDeathTotemFromInventory(LivingEntity entity, InteractionHand hand) {
        ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);

        if (Charm.LOADER.isEnabled(TotemWorksFromInventory.class) && entity instanceof Player player) {
            Inventory inventory = player.getInventory();

            if (inventory.contains(totem)) {
                if (player.getOffhandItem().getItem() == Items.TOTEM_OF_UNDYING) {
                    return player.getOffhandItem();
                } else {
                    return inventory.getItem(inventory.findSlotMatchingItem(totem));
                }
            }
        }

        return entity.getItemInHand(hand);
    }
}
