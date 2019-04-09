package svenhjol.charm.brewing.potion;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmPotion;
import svenhjol.charm.brewing.feature.Coffee;
import svenhjol.meson.ProxyRegistry;

import java.util.ArrayList;
import java.util.List;

public class CoffeePotion extends CharmPotion
{
    public static PotionType type;

    public CoffeePotion()
    {
        super("coffee", false, 0x160202, 3);

        List<PotionEffect> effects = new ArrayList<>();
        effects.add(new PotionEffect(MobEffects.SPEED, Coffee.duration * 20));
        effects.add(new PotionEffect(MobEffects.HASTE, Coffee.duration * 20));
        effects.add(new PotionEffect(MobEffects.STRENGTH, Coffee.duration * 20));
        PotionEffect[] potionEffects = effects.toArray(new PotionEffect[0]);

        type = new PotionType(name, potionEffects).setRegistryName(new ResourceLocation(getModId(), name));
        ProxyRegistry.register(type);

        ItemStack cocoa = new ItemStack(Items.DYE, 1, 3);
        PotionHelper.addMix(PotionTypes.WATER, Ingredient.fromStacks(cocoa), type);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}