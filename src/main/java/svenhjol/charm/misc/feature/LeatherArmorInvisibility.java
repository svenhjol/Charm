package svenhjol.charm.misc.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import svenhjol.meson.Feature;

import java.util.ArrayList;
import java.util.List;

public class LeatherArmorInvisibility extends Feature
{
    public static List<Item> invisibleItems = new ArrayList<>();

    @Override
    public String getDescription()
    {
        return "Leather armor is invisible and does not increase mob awareness when drinking Potion of Invisibility.";
    }

    @Override
    public void setupConfig()
    {
        invisibleItems.add(Items.LEATHER_HELMET);
        invisibleItems.add(Items.LEATHER_CHESTPLATE);
        invisibleItems.add(Items.LEATHER_LEGGINGS);
        invisibleItems.add(Items.LEATHER_BOOTS);
    }

    public static boolean isArmorInvisible(Entity entity, ItemStack stack)
    {
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase e = (EntityLivingBase)entity;
            if (e.getActivePotionEffect(MobEffects.INVISIBILITY) != null) {
                return invisibleItems.contains(stack.getItem());
            }
        }

        return false;
    }
}
