package svenhjol.charm.brewing.feature;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.meson.Feature;

/**
 * Adds a recipe for brewing poisonous potatoes into potions of poison.
 */
public class PoisonousPotatoBrew extends Feature
{
    @Override
    public String getDescription()
    {
        return "Allows Potion of Poison to be created from Mundane Potions and Poisonous Potatoes.";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        PotionType normalPotionType = PotionTypes.POISON;
        ItemStack potato = new ItemStack(Items.POISONOUS_POTATO);
        PotionHelper.addMix(PotionTypes.MUNDANE, Ingredient.fromStacks(potato), normalPotionType);
    }
}
