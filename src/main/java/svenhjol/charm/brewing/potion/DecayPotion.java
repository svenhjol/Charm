package svenhjol.charm.brewing.potion;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.feature.Decay;
import svenhjol.meson.MesonPotion;

public class DecayPotion extends MesonPotion
{
    public DecayPotion()
    {
        super(new EffectInstance(Decay.effect, Decay.duration * 20));

        setPotionBase(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTION), Potions.AWKWARD));
        setPotionReagant(new ItemStack(Items.WITHER_ROSE));

        register(new ResourceLocation(Charm.MOD_ID, "decay_potion"));
    }
}
