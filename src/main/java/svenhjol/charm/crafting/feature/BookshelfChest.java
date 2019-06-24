package svenhjol.charm.crafting.feature;

import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.block.BlockBookshelfChest;
import svenhjol.meson.Feature;
import svenhjol.meson.registry.ProxyRegistry;
import svenhjol.meson.handler.RecipeHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookshelfChest extends Feature
{
    public static BlockBookshelfChest bookshelfChest;
    public static float hardness;
    public static List<String> validItems = new ArrayList<>();

    @Override
    public String getDescription()
    {
        return "A bookshelf with 9 slots that can store books.\n" +
                "Provides the same enchanting power as a normal bookshelf as long as there is at least 1 book on the shelf.";
    }

    @Override
    public void setupConfig()
    {
        validItems = Arrays.asList(
            propStringList("Valid bookshelf items",
                "List of items that are allowed to be added to bookshelf chest.",
                new String[] {
                    "minecraft:book",
                    "minecraft:enchanted_book",
                    "minecraft:written_book",
                    "minecraft:writable_book",
                    "minecraft:knowledge_book",
                    "quark:ancient_tome",
                    "inspirations:books[*]",
                    "forestry:catalogue",
                    "forestry:book_forester",
                    "immersiveengineering:tool[3]",
                    "tconstruct:book"
                }
            )
        );

        // internal
        hardness = 1.0f;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        bookshelfChest = new BlockBookshelfChest();
        GameRegistry.registerTileEntity(bookshelfChest.getTileEntityClass(), new ResourceLocation(Charm.MOD_ID + ":bookshelf_chest"));

        // add bookshelf chest recipe
        RecipeHandler.addShapedRecipe(ProxyRegistry.newStack(bookshelfChest, 1),
            "WWW", "WBW", "WWW",
            'W', "plankWood",
            'B', ProxyRegistry.newStack(Blocks.BOOKSHELF, 1)
        );
    }
}
