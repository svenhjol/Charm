package svenhjol.charm.base.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import svenhjol.charm.Charm;
import svenhjol.charm.enchanting.module.CurseBreak;
import svenhjol.charm.tools.item.BoundCompassItem;
import svenhjol.charm.tools.module.CompassBinding;
import svenhjol.charm.world.module.NetherPigIron;
import svenhjol.meson.Meson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@JeiPlugin
public class CharmJeiPlugin implements IModPlugin {
    private static final ResourceLocation UID = new ResourceLocation(Charm.MOD_ID, Charm.MOD_ID);

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        final IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();

        if (Meson.isModuleEnabled("charm:dimensional_compass"))
            registerDimensionalCompass(registration, factory);

        if (Meson.isModuleEnabled("charm:pig_iron_increases_durability"))
            registerPigIronRepair(registration, factory);

        if (Meson.isModuleEnabled("charm:extract_enchantments"))
            registerExtractEnchantments(registration, factory);

        if (Meson.isModuleEnabled("charm:curse_break"))
            registerCurseBreak(registration, factory);
    }

    private void registerDimensionalCompass(IRecipeRegistration registration, IVanillaRecipeFactory factory) {
        List<Object> recipes = new ArrayList<>();
        final ItemStack boundCompass = new ItemStack(CompassBinding.item);
        final ItemStack dimensionalCompass = boundCompass.copy();
        dimensionalCompass.setDisplayName(new TranslationTextComponent("item.charm.dimensional_compass"));
        BoundCompassItem.setDimensional(dimensionalCompass, true);

        recipes.add(factory.createAnvilRecipe(boundCompass,
            Collections.singletonList(new ItemStack(Items.ENDER_EYE)),
            Collections.singletonList(dimensionalCompass)
        ));

        registration.addRecipes(recipes, VanillaRecipeCategoryUid.ANVIL);
    }

    private void registerPigIronRepair(IRecipeRegistration registration, IVanillaRecipeFactory factory) {
        List<Object> recipes = new ArrayList<>();
        ItemStack damagedPick = new ItemStack(Items.DIAMOND_PICKAXE);
        damagedPick.setDamage(1000);

        ItemStack repairedPick = damagedPick.copy();
        repairedPick.setDamage(900);

        recipes.add(factory.createAnvilRecipe(damagedPick,
            Collections.singletonList(new ItemStack(NetherPigIron.item)),
            Collections.singletonList(repairedPick)
        ));

        registration.addRecipes(recipes, VanillaRecipeCategoryUid.ANVIL);
    }

    private void registerExtractEnchantments(IRecipeRegistration registration, IVanillaRecipeFactory factory) {
        List<Object> recipes = new ArrayList<>();
        Enchantment enchant = Enchantments.UNBREAKING;

        ItemStack pick1 = new ItemStack(Items.DIAMOND_PICKAXE);
        ItemStack book1 = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {{ put(enchant, 2); }}, pick1);
        EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {{ put(enchant, 1); }}, book1);

        ItemStack pick2 = new ItemStack(Items.DIAMOND_PICKAXE);
        ItemStack book2 = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {{ put(enchant, 1); }}, pick2);
        EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {{ put(enchant, 1); }}, book2);

        recipes.add(factory.createAnvilRecipe(pick1,
            Collections.singletonList(new ItemStack(Items.BOOK)),
            Collections.singletonList(book1)
        ));

        recipes.add(factory.createAnvilRecipe(pick2,
            Collections.singletonList(new ItemStack(Items.BOOK)),
            Collections.singletonList(book2)
        ));

        registration.addRecipes(recipes, VanillaRecipeCategoryUid.ANVIL);
    }

    private void registerCurseBreak(IRecipeRegistration registration, IVanillaRecipeFactory factory) {
        List<Object> recipes = new ArrayList<>();

        ItemStack normal = new ItemStack(Items.DIAMOND_PICKAXE);
        ItemStack cursed = new ItemStack(Items.DIAMOND_PICKAXE);
        EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {{ put(Enchantments.VANISHING_CURSE, 1); }}, cursed);

        ItemStack curseBreak = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {{ put(CurseBreak.enchantment, 1); }}, curseBreak);

        recipes.add(factory.createAnvilRecipe(cursed,
            Collections.singletonList(curseBreak),
            Collections.singletonList(normal)
        ));

        registration.addRecipes(recipes, VanillaRecipeCategoryUid.ANVIL);
    }
}
