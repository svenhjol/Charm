package svenhjol.charm.mixin.woodcutters;

import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(RecipeBookType.class)
@Unique
public class AddRecipeBookTypeMixin {
    @SuppressWarnings("target")
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
        int newId = variants.get(variants.size() - 1).ordinal() + 1;
        RecipeBookType newEntry = invokeInit(newName, newId);
        variants.add(newEntry);
        AddRecipeBookTypeMixin.$VALUES = variants.toArray(new RecipeBookType[0]);
    }
}
