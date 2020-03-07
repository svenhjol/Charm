package svenhjol.charm.tweaks.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS,
    description = "Leather armor is invisible and does not increase mob awareness when drinking Potion of Invisibility.")
public class LeatherArmorInvisibility extends MesonModule {
    public static List<Item> invisibleItems = new ArrayList<>();

    @Override
    public void init() {
        addInvisibleItem(Items.LEATHER_HELMET);
        addInvisibleItem(Items.LEATHER_CHESTPLATE);
        addInvisibleItem(Items.LEATHER_LEGGINGS);
        addInvisibleItem(Items.LEATHER_BOOTS);
    }

    public static void addInvisibleItem(Item item) {
        invisibleItems.add(item);
    }

    public static boolean isArmorInvisible(Entity entity, ItemStack stack) {
        if (entity instanceof LivingEntity) {
            LivingEntity e = (LivingEntity) entity;
            if (e.getActivePotionEffect(Effects.INVISIBILITY) != null) {
                return invisibleItems.contains(stack.getItem());
            }
        }

        return false;
    }
}
