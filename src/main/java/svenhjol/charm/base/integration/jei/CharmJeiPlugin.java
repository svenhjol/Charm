package svenhjol.charm.base.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.feature.FlavoredCake;
import svenhjol.charm.crafting.feature.Composter;
import svenhjol.charm.crafting.feature.Crate;
import svenhjol.charm.tweaks.feature.CompassBinding;
import svenhjol.charm.world.feature.Moonstone;
import svenhjol.meson.helper.ItemHelper;

import java.util.*;
import java.util.stream.Collectors;

@JEIPlugin
public class CharmJeiPlugin implements IModPlugin
{
    private ComposterCategory composter;

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry)
    {
        composter = new ComposterCategory(registry.getJeiHelpers().getGuiHelper());
        registry.addRecipeCategories(composter);
    }

    @Override
    public void register(IModRegistry registry)
    {
        // set all potential composter outputs
        List<String> composterOutputs = Composter.outputs;
        List<ItemStack> outputs = new ArrayList<>();

        composterOutputs.forEach(name -> outputs.addAll(ItemHelper.getItemStacksFromItemString(name)));

        // set inputs by page + chance
        Map<Float, List<String>> composterInputs = Composter.getInputsByChance();
        Map<Integer, Map<Float, List<ItemStack>>> inputs = new TreeMap<>();
        Float[] chances = composterInputs.keySet().toArray(new Float[0]);
        Arrays.sort(chances);

        int page = 0;
        for (Float chance : chances) {
            int i = 0;
            for (String itemName : composterInputs.get(chance)) {
                List<ItemStack> stacks = ItemHelper.getItemStacksFromItemString(itemName);
                for (ItemStack stack : stacks) {
                    if (!inputs.containsKey(page)) {
                        inputs.put(page, new HashMap<Float, List<ItemStack>>() {{ put(chance, new ArrayList<>()); }});
                    }
                    inputs.get(page).get(chance).add(stack);
                    page += (++i % 36 == 0) ? 1 : 0; // if there are more than 36 input items, add a "page"
                }
            }
            page++;
        }

        registry.addRecipeCatalyst(new ItemStack(Composter.composter), composter.getUid());
        registry.addRecipes(inputs.keySet()
                .stream()
                .map(p -> {
                    Map.Entry<Float, List<ItemStack>> entry = inputs.get(p).entrySet().iterator().next(); // this is crazy right
                    return new ComposterRecipe(entry.getValue(), outputs, entry.getKey());
                })
                .collect(Collectors.toList()), composter.getUid());

        // add JEI descriptions for flavored cake.  Fix #139
        if (Charm.hasFeature(FlavoredCake.class)) {
            final List<ItemStack> cakeItems = new ArrayList<>();
            FlavoredCake.cakes.values().forEach(cake -> cakeItems.add(new ItemStack(cake)));
            registry.addIngredientInfo(cakeItems, VanillaTypes.ITEM, "charm.jei.flavoredcake.desc");
        }

        // JEI description for bound compass
        if (Charm.hasFeature(CompassBinding.class)) {
            registry.addIngredientInfo(new ItemStack(CompassBinding.boundCompass), VanillaTypes.ITEM, "charm.jei.boundcompass.desc");
        }

        // JEI description for crates
        if (Charm.hasFeature(Crate.class)) {
            List<ItemStack> crates = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                crates.addAll(Arrays.asList(new ItemStack(Crate.crate, 1, i), new ItemStack(Crate.crateSealed, 1, i)));
            }
            registry.addIngredientInfo(crates, VanillaTypes.ITEM, "charm.jei.crates.desc");
        }

        // JEI description for moonstone
        if (Charm.hasFeature(Moonstone.class)) {
            List<ItemStack> moonstones = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                moonstones.add(new ItemStack(Moonstone.moonstone, 1, i));
            }
            registry.addIngredientInfo(moonstones, VanillaTypes.ITEM, "charm.jei.moonstone.desc");
        }
    }
}
