package svenhjol.charm.module;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.RegistryHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.BookcaseBlock;
import svenhjol.charm.blockentity.BookcaseBlockEntity;
import svenhjol.charm.client.BookcasesClient;
import svenhjol.charm.screenhandler.BookcaseScreenHandler;

import java.util.*;

@Module(mod = Charm.MOD_ID, priority = 10, client = BookcasesClient.class, description = "Bookshelves that can hold up to 9 stacks of books and maps.")
public class Bookcases extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "bookcase");
    public static final Map<IVariantMaterial, BookcaseBlock> BOOKCASE_BLOCKS = new HashMap<>();

    public static ScreenHandlerType<BookcaseScreenHandler> SCREEN_HANDLER;
    public static BlockEntityType<BookcaseBlockEntity> BLOCK_ENTITY;

    public static List<Item> validItems = new ArrayList<>();

    @Config(name = "Valid books", description = "Additional items that may be placed in bookcases.")
    public static List<String> configValidItems = Arrays.asList(
        "strange:scroll"
    );

    @Override
    public void register() {
        validItems.addAll(Arrays.asList(
            Items.BOOK,
            Items.ENCHANTED_BOOK,
            Items.WRITTEN_BOOK,
            Items.WRITABLE_BOOK,
            Items.KNOWLEDGE_BOOK,
            Items.PAPER,
            Items.MAP,
            Items.FILLED_MAP
        ));

        VanillaVariantMaterial.getTypes().forEach(type -> {
            BOOKCASE_BLOCKS.put(type, new BookcaseBlock(this, type));
        });

        configValidItems.forEach(string -> {
            Item item = Registry.ITEM.get(new Identifier(string));
            validItems.add(item);
        });

        SCREEN_HANDLER = RegistryHandler.screenHandler(ID, BookcaseScreenHandler::new);
        BLOCK_ENTITY = RegistryHandler.blockEntity(ID, BookcaseBlockEntity::new);
    }

    public static BookcaseBlock registerBookcase(CharmModule module, IVariantMaterial material) {
        BookcaseBlock bookcase = new BookcaseBlock(module, material);
        BOOKCASE_BLOCKS.put(material, bookcase);
        RegistryHelper.addBlocksToBlockEntity(BLOCK_ENTITY, bookcase);
        return bookcase;
    }

    public static boolean canContainItem(ItemStack stack) {
        return validItems.contains(stack.getItem());
    }
}
