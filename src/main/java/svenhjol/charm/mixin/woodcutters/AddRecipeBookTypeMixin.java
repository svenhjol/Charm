package svenhjol.charm.mixin.woodcutters;

import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Enum solution from LudoCrypt:
 * @link {https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556}
 */
@SuppressWarnings({"SameParameterValue", "unused", "target"})
@Mixin(RecipeBookType.class)
@Unique
public class AddRecipeBookTypeMixin {
    @Shadow
    @Final
    @Mutable
    private static RecipeBookType[] $VALUES;

    static {
        addVariant("WOODCUTTER");
    }

    @Invoker("<init>")
    public static RecipeBookType invokeInit(String internalName, int internalId) {
        throw new AssertionError();
    }

    private static void addVariant(String newName) {
        List<RecipeBookType> variants = new ArrayList<>(Arrays.asList(AddRecipeBookTypeMixin.$VALUES));
        variants.add(invokeInit(newName, variants.get(variants.size() - 1).ordinal() + 1));
        AddRecipeBookTypeMixin.$VALUES = variants.toArray(new RecipeBookType[0]);
    }
}
