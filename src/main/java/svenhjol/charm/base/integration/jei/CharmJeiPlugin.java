package svenhjol.charm.base.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.item.ItemStack;
import svenhjol.charm.crafting.feature.Composter;
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

        // set inputs by chance
        Map<Float, List<String>> composterInputs = Composter.getInputsByChance();
        Map<Float, List<ItemStack>> inputs = new TreeMap<>();
        Float[] chances = composterInputs.keySet().toArray(new Float[0]);
        Arrays.sort(chances);

        for (Float chance : chances) {
            inputs.put(chance, new ArrayList<>());
            composterInputs.get(chance).forEach(name -> {
                inputs.get(chance).addAll(ItemHelper.getItemStacksFromItemString(name));
            });
        }

        registry.addRecipeCatalyst(new ItemStack(Composter.composter), composter.getUid());
        registry.addRecipes(inputs.keySet()
                .stream()
                .map(chance -> new ComposterRecipe(inputs.get(chance), outputs, chance))
                .collect(Collectors.toList()), composter.getUid());
    }
}
