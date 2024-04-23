package svenhjol.charm.foundation.helper;

import java.util.ArrayList;
import java.util.List;

public final class CommonRegistryHelper {
    private static final List<String> RECIPE_BOOK_TYPE_ENUMS = new ArrayList<>();

    public static List<String> getRecipeBookTypeEnums() {
        return RECIPE_BOOK_TYPE_ENUMS;
    }

    public void recipeBookTypeEnum(String name) {
        RECIPE_BOOK_TYPE_ENUMS.add(name);
    }
}
