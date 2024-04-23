package svenhjol.charm.foundation.helper;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

public final class ClientRegistryHelper {
    private static final List<Pair<String, ItemLike>> RECIPE_BOOK_CATEGORY_ENUMS = new ArrayList<>();
    private static final Map<RecipeBookType, List<RecipeBookCategories>> RECIPE_BOOK_CATEGORY_BY_TYPE = new HashMap<>();
    private static final Map<RecipeType<?>, RecipeBookCategories> RECIPE_BOOK_MAIN_CATEGORY = new HashMap<>();

    public static Map<RecipeType<?>, RecipeBookCategories> getRecipeBookMainCategory() {
        return RECIPE_BOOK_MAIN_CATEGORY;
    }

    public static List<Pair<String, ItemLike>> getRecipeBookCategoryEnums() {
        return RECIPE_BOOK_CATEGORY_ENUMS;
    }

    public static Map<RecipeBookType, List<RecipeBookCategories>> getRecipeBookCategoryByType() {
        return RECIPE_BOOK_CATEGORY_BY_TYPE;
    }

    public static <T extends ItemLike> void itemTab(Supplier<T> item, ResourceKey<CreativeModeTab> key, @Nullable ItemLike showAfter) {
        if (showAfter != null) {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.addAfter(showAfter, item.get()));
        } else {
            ItemGroupEvents.modifyEntriesEvent(key)
                .register(entries -> entries.accept(item.get()));
        }
    }

    public <R extends Recipe<?>> void recipeBookCategory(String id, Supplier<RecipeType<R>> recipeType, Supplier<RecipeBookType> recipeBookType) {
        var upper = id.toUpperCase(Locale.ROOT);
        var searchCategory = EnumHelper.getValueOrDefault(() -> RecipeBookCategories.valueOf(upper + "_SEARCH"), RecipeBookCategories.CRAFTING_SEARCH);
        var mainCategory = EnumHelper.getValueOrDefault(() -> RecipeBookCategories.valueOf(upper), RecipeBookCategories.CRAFTING_MISC);

        RECIPE_BOOK_MAIN_CATEGORY.put(recipeType.get(), mainCategory);
        RECIPE_BOOK_CATEGORY_BY_TYPE.put(recipeBookType.get(), List.of(searchCategory, mainCategory));

        var aggregateCategories = new HashMap<>(RecipeBookCategories.AGGREGATE_CATEGORIES);
        aggregateCategories.put(searchCategory, List.of(mainCategory));
        RecipeBookCategories.AGGREGATE_CATEGORIES = aggregateCategories;
    }
}
