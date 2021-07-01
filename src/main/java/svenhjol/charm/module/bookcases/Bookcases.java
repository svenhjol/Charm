package svenhjol.charm.module.bookcases;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmCommonModule;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, priority = 10, description = "Bookshelves that can hold up to 9 stacks of books and maps.")
public class Bookcases extends CharmCommonModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "bookcase");
    public static final ResourceLocation TRIGGER_ADDED_BOOK_TO_BOOKCASE = new ResourceLocation(Charm.MOD_ID, "added_book_to_bookcase");
    public static final Map<IVariantMaterial, BookcaseBlock> BOOKCASE_BLOCKS = new HashMap<>();

    public static MenuType<BookcaseScreenHandler> SCREEN_HANDLER;
    public static BlockEntityType<BookcaseBlockEntity> BLOCK_ENTITY;

    public static List<Item> validItems = new ArrayList<>();

    @Config(name = "Valid books", description = "Additional items that may be placed in bookcases.")
    public static List<String> configValidItems = new ArrayList<>();

    @Override
    public void register() {
        BLOCK_ENTITY = RegistryHelper.blockEntity(ID, BookcaseBlockEntity::new);

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
            registerBookcase(this, type);
        });

        configValidItems.forEach(string -> {
            Item item = Registry.ITEM.get(new ResourceLocation(string));
            validItems.add(item);
        });

        SCREEN_HANDLER = RegistryHelper.screenHandler(ID, BookcaseScreenHandler::new);
    }

    public static BookcaseBlock registerBookcase(CharmCommonModule module, IVariantMaterial material) {
        BookcaseBlock bookcase = new BookcaseBlock(module, material);
        BOOKCASE_BLOCKS.put(material, bookcase);
        RegistryHelper.addBlocksToBlockEntity(BLOCK_ENTITY, bookcase);
        return bookcase;
    }

    public static boolean canContainItem(ItemStack stack) {
        return validItems.contains(stack.getItem());
    }

    public static void triggerAddedBookToBookcase(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_ADDED_BOOK_TO_BOOKCASE);
    }
}
