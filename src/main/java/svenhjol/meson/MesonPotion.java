package svenhjol.meson;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import svenhjol.meson.iface.IMesonPotion;

public abstract class MesonPotion extends Potion implements IMesonPotion
{
    protected Ingredient potionBase;
    protected Ingredient potionReagant;

    public MesonPotion(EffectInstance... effects)
    {
        super(effects);
    }

    public void setPotionBase(ItemStack base)
    {
        this.potionBase = Ingredient.fromStacks(base);
    }

    public void setPotionReagant(ItemStack reagant)
    {
        this.potionReagant = Ingredient.fromStacks(reagant);
    }

    public void registerRecipe()
    {
        if (potionBase != null && potionReagant != null) {
            ItemStack out = PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), this);
            BrewingRecipeRegistry.addRecipe(new BrewingRecipe(potionBase, potionReagant, out));
        }
    }
}
