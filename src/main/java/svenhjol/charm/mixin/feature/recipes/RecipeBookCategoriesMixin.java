package svenhjol.charm.mixin.feature.recipes;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.foundation.helper.ClientRegistryHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Enum solution from LudoCrypt:
 * @link <a href="https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556">...</a>
 */
@SuppressWarnings({"SameParameterValue", "unused", "target"})
@Mixin(RecipeBookCategories.class)
@Unique
public class RecipeBookCategoriesMixin {
    @Shadow
    @Final
    @Mutable
    private static RecipeBookCategories[] $VALUES;

    static {
        var categories = ClientRegistryHelper.getRecipeBookCategoryEnums();
        for (var pair : categories) {
            addVariant(pair.getFirst(), new ItemStack(pair.getSecond()));
        }
    }


    @Invoker("<init>")
    public static RecipeBookCategories invokeInit(String internalName, int internalId, ItemStack...itemStacks) {
        throw new AssertionError();
    }

    @Unique
    private static void addVariant(String newName, ItemStack ...itemStacks) {
        List<RecipeBookCategories> variants = new ArrayList<>(Arrays.asList(RecipeBookCategoriesMixin.$VALUES));
        variants.add(invokeInit(newName.toUpperCase(Locale.ROOT), variants.get(variants.size() - 1).ordinal() + 1, itemStacks));
        RecipeBookCategoriesMixin.$VALUES = variants.toArray(new RecipeBookCategories[0]);
    }
}
