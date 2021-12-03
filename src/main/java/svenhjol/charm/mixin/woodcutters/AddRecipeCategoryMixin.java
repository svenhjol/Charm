package svenhjol.charm.mixin.woodcutters;

import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.module.kilns.Kilns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum solution from LudoCrypt:
 * @link {https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556}
 */
@SuppressWarnings({"SameParameterValue", "unused", "target"})
@Mixin(RecipeBookCategories.class)
@Unique
public class AddRecipeCategoryMixin {
    @Shadow
    @Final
    @Mutable
    private static RecipeBookCategories[] $VALUES;

    static {
        addVariant("WOODCUTTER_SEARCH", new ItemStack(Items.COMPASS));
        addVariant("WOODCUTTER", new ItemStack(Kilns.KILN));
    }

    @Invoker("<init>")
    public static RecipeBookCategories invokeInit(String internalName, int internalId, ItemStack ...itemStacks) {
        throw new AssertionError();
    }

    private static void addVariant(String newName, ItemStack ...itemStacks) {
        List<RecipeBookCategories> variants = new ArrayList<>(Arrays.asList(AddRecipeCategoryMixin.$VALUES));
        variants.add(invokeInit(newName, variants.get(variants.size() - 1).ordinal() + 1, itemStacks));
        AddRecipeCategoryMixin.$VALUES = variants.toArray(new RecipeBookCategories[0]);
    }
}
