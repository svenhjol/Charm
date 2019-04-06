package svenhjol.charm.loot.compat;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import svenhjol.meson.Feature;
import svenhjol.meson.FeatureCompat;
import vazkii.quark.vanity.item.ItemWitchHat;

public class CompatWitchHatProtection extends FeatureCompat
{
    public CompatWitchHatProtection(Feature feature)
    {
        super(feature);
    }

    public boolean isWearingWitchHat(EntityLivingBase entity)
    {
        ItemStack hat = entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        return !hat.isEmpty() && hat.getItem() instanceof ItemWitchHat;
    }
}
