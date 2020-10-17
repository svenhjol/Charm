package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Lightweight armor is invisible and does not increase mob awareness when drinking Potion of Invisibility.")
public class ArmorInvisibility extends CharmModule {
    public static List<Item> invisibleItems = new ArrayList<>();
    public static boolean isEnabled = false;

    @Override
    public void init() {
        isEnabled = true;

        invisibleItems.addAll(Arrays.asList(
            Items.LEATHER_HELMET,
            Items.LEATHER_CHESTPLATE,
            Items.LEATHER_LEGGINGS,
            Items.LEATHER_BOOTS,
            Items.CHAINMAIL_HELMET,
            Items.CHAINMAIL_CHESTPLATE,
            Items.CHAINMAIL_LEGGINGS,
            Items.CHAINMAIL_BOOTS
        ));
    }

    public static boolean shouldArmorBeInvisible(Entity entity, ItemStack stack) {
        if (stack.isEmpty())
            return true; // air is invisible!

        if (isEnabled && entity instanceof LivingEntity) {
            if (((LivingEntity)entity).getStatusEffect(StatusEffects.INVISIBILITY) != null)
                return invisibleItems.contains(stack.getItem());
        }

        return false;
    }
}
