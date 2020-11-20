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
import svenhjol.charm.base.helper.EnchantmentsHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.BookcaseBlock;
import svenhjol.charm.blockentity.BookcaseBlockEntity;
import svenhjol.charm.client.BookcasesClient;
import svenhjol.charm.screenhandler.BookcaseScreenHandler;

import java.util.*;

@Module(mod = Charm.MOD_ID, client = BookcasesClient.class, description = "Bookshelves that can hold up to 9 stacks of books and maps.")
public class Bookcases extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "bookcase");
    public static final Map<IVariantMaterial, BookcaseBlock> BOOKCASE_BLOCKS = new HashMap<>();

    public static ScreenHandlerType<BookcaseScreenHandler> SCREEN_HANDLER;
    public static BlockEntityType<BookcaseBlockEntity> BLOCK_ENTITY;

    public static List<Class<? extends Item>> validItems = new ArrayList<>();

    @Config(name = "Valid books", description = "Additional items that may be placed in bookcases.")
    public static List<String> configValidItems = Arrays.asList(
        "strange:scroll"
    );

    @Override
    public void register() {
        validItems.addAll(Arrays.asList(
            Items.BOOK.getClass(),
            Items.ENCHANTED_BOOK.getClass(),
            Items.WRITTEN_BOOK.getClass(),
            Items.WRITABLE_BOOK.getClass(),
            Items.KNOWLEDGE_BOOK.getClass(),
            Items.PAPER.getClass(),
            Items.MAP.getClass(),
            Items.FILLED_MAP.getClass()
        ));

        VanillaVariantMaterial.getTypes().forEach(type -> {
            BOOKCASE_BLOCKS.put(type, new BookcaseBlock(this, type));
        });

        configValidItems.forEach(string -> {
            Item item = Registry.ITEM.get(new Identifier(string));
            validItems.add(item.getClass());
        });

        SCREEN_HANDLER = RegistryHandler.screenHandler(ID, BookcaseScreenHandler::new);
        BLOCK_ENTITY = RegistryHandler.blockEntity(ID, BookcaseBlockEntity::new);
    }

    @Override
    public void init() {
        EnchantmentsHelper.ENCHANTING_BLOCKS.addAll(BOOKCASE_BLOCKS.values());
    }

    public static boolean canContainItem(ItemStack stack) {
        return validItems.contains(stack.getItem().getClass());
    }
}
