package svenhjol.charm.mixin.feature.recipes;

import net.minecraft.world.inventory.RecipeBookType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.foundation.helper.CommonRegistryHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Enum solution from LudoCrypt:
 * @link <a href="https://github.com/SpongePowered/Mixin/issues/387#issuecomment-888408556">...</a>
 */
@SuppressWarnings({"SameParameterValue", "unused", "target"})
@Mixin(RecipeBookType.class)
@Unique
public class RecipeBookTypeMixin {
    @Shadow
    @Final
    @Mutable
    private static RecipeBookType[] $VALUES;

    static {
        var types = CommonRegistryHelper.getRecipeBookTypeEnums();
        for (String type : types) {
            addVariant(type);
        }
    }

    @Invoker("<init>")
    public static RecipeBookType invokeInit(String internalName, int internalId) {
        throw new AssertionError();
    }

    @Unique
    private static void addVariant(String newName) {
        List<RecipeBookType> variants = new ArrayList<>(Arrays.asList(RecipeBookTypeMixin.$VALUES));
        variants.add(invokeInit(newName.toUpperCase(Locale.ROOT), variants.get(variants.size() - 1).ordinal() + 1));
        RecipeBookTypeMixin.$VALUES = variants.toArray(new RecipeBookType[0]);
    }
}
