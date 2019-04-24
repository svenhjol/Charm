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
        Map<Float, List<String>> composterInputs = Composter.inputsByChance;
        Map<Float, List<ItemStack>> inputs = new HashMap<>();

        composterInputs.keySet().forEach(chance -> composterInputs.get(chance).forEach(name -> {
            if (!inputs.containsKey(chance)) inputs.put(chance, new ArrayList<>());
            inputs.get(chance).addAll(ItemHelper.getItemStacksFromItemString(name));
        }));

        // convert the inputsByChance into ItemStacks
//        Composter.inputsByChance.keySet().forEach(chance ->
//        {
//            Composter.inputsByChance.get(chance).stream().map(name ->
//            {
//                if (!map.containsKey(chance)) map.put(chance, new ArrayList<>());
//                return map.get(chance).add(ItemHelper.getItemStacksFromItemString(name));
//            });
//        });
//
//        Map<Float, List<ItemStack>> map = new HashMap<>();
//        map.put(0.3f, Arrays.asList(
//                new ItemStack(Items.BREAD),
//                new ItemStack(Items.APPLE)
//        ));

        registry.addRecipeCatalyst(new ItemStack(Composter.composter), composter.getUid());
        registry.addRecipes(inputs.keySet()
                .stream()
                .map(chance -> new ComposterRecipe(inputs.get(chance), outputs, chance))
                .collect(Collectors.toList()), composter.getUid());

    }
}
