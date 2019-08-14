package svenhjol.charm.brewing.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.feature.Coffee;
import svenhjol.meson.MesonPotion;

public class CoffeePotion extends MesonPotion
{
    public CoffeePotion()
    {
        super(new EffectInstance(Coffee.effect, Coffee.duration * 20));

        setPotionBase(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.WATER));
        setPotionReagant(new ItemStack(Items.COCOA_BEANS));

        register(new ResourceLocation(Charm.MOD_ID, "coffee_potion"));
    }
}
