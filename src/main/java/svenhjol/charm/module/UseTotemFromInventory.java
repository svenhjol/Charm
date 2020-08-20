package svenhjol.charm.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "As long as a Totem of Undying is in your inventory, it will be consumed to protect you from death.")
public class UseTotemFromInventory extends MesonModule {
    public static ItemStack tryFromInventory(LivingEntity entity, Hand hand) {
        ItemStack totem = new ItemStack(Items.TOTEM_OF_UNDYING);

        if (Meson.enabled("charm:use_totem_from_inventory") && entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)entity;

            if (player.inventory.contains(totem)) {
                if (player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
                    return player.getOffHandStack();
                } else {
                    return player.inventory.getStack(player.inventory.getSlotWithStack(totem));
                }
            }
        }

        return entity.getStackInHand(hand);
    }
}
