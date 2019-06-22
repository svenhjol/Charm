package svenhjol.charm.brewing.potion;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmPotion;
import svenhjol.charm.brewing.feature.EnderSight;
import svenhjol.meson.registry.ProxyRegistry;

public class EnderSightPotion extends CharmPotion
{
    public static PotionEffect effect;
    public static PotionType type;

    public EnderSightPotion()
    {
        super("ender_sight", false, 0x75a117, 1);
        effect = new PotionEffect(this, EnderSight.duration * 20);
        type = new PotionType(name, effect).setRegistryName(new ResourceLocation(getModId(), name));
        ProxyRegistry.register(type);

        ItemStack eye = new ItemStack(Items.ENDER_EYE);
        PotionHelper.addMix(PotionTypes.NIGHT_VISION, Ingredient.fromStacks(eye), type);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}
