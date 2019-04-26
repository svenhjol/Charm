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
    }
}
