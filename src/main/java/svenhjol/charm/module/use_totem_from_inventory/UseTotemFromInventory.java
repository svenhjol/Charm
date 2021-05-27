package svenhjol.charm.module.use_totem_from_inventory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import svenhjol.charm.Charm;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "As long as a Totem of Undying is in your inventory, it will be consumed to protect you from death.")
public class UseTotemFromInventory extends CharmModule {
    public static ItemStack tryFromInventory(LivingEntity entity, Hand hand) {
        ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);

        if (ModuleHandler.enabled("charm:use_totem_from_inventory") && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;
            PlayerInventory inventory = PlayerHelper.getInventory(player);

            if (inventory.contains(totem)) {
                if (player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
                    return player.getOffHandStack();
                } else {
                    return inventory.getStack(inventory.getSlotWithStack(totem));
                }
            }
        }

        return entity.getStackInHand(hand);
    }
}
