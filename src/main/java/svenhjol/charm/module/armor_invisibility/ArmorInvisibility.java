package svenhjol.charm.module.armor_invisibility;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Lightweight armor is invisible and does not increase mob awareness when drinking Potion of Invisibility.")
public class ArmorInvisibility extends CharmModule {
    public static List<Item> invisibleItems = new ArrayList<>();
    public static boolean isEnabled = false;

    @Override
    public void runWhenEnabled() {
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
            if (((LivingEntity)entity).getEffect(MobEffects.INVISIBILITY) != null)
                return invisibleItems.contains(stack.getItem());
        }

        return false;
    }
}
