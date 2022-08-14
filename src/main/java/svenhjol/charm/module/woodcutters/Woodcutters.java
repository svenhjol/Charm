package svenhjol.charm.module.woodcutters;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

@CommonModule(mod = Charm.MOD_ID, description = "A functional block that adds more efficient recipes for crafting wooden stairs and slabs.", priority = 1)
public class Woodcutters extends CharmModule {
    public static ResourceLocation RECIPE_ID = new ResourceLocation(Charm.MOD_ID, "woodcutting");
    public static ResourceLocation BLOCK_ID = new ResourceLocation(Charm.MOD_ID, "woodcutter");
    public static WoodcutterBlock WOODCUTTER;
    public static MenuType<WoodcutterMenu> MENU;
    public static RecipeType<WoodcuttingRecipe> RECIPE_TYPE;
    public static RecipeSerializer<WoodcuttingRecipe> RECIPE_SERIALIZER;

    public static SoundEvent USE_SOUND;
    public static RecipeBookType RECIPE_BOOK_TYPE;

    @Override
    public void register() {
        WOODCUTTER = new WoodcutterBlock(this);
        RECIPE_TYPE = CommonRegistry.recipeType(RECIPE_ID.toString());
        RECIPE_SERIALIZER = CommonRegistry.recipeSerializer(RECIPE_ID.toString(), new WoodcuttingRecipe.Serializer<>(WoodcuttingRecipe::new));
        MENU = CommonRegistry.menu(BLOCK_ID, WoodcutterMenu::new);
        USE_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "woodcutter_use"));
    }

    @Override
    public void runWhenEnabled() {
        RECIPE_BOOK_TYPE = CommonRegistry.recipeBookType("woodcutter");
    }
}
