package svenhjol.charm.tweaks.compat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import vazkii.quark.vanity.item.ItemWitchHat;

public class QuarkWitchHat
{
    public boolean isWearingWitchHat(EntityLivingBase entity)
    {
        ItemStack hat = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        return !hat.isEmpty() && hat.getItem() instanceof ItemWitchHat;
    }
}
