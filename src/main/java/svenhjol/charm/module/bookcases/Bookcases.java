package svenhjol.charm.module.bookcases;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.enums.IWoodMaterial;
import svenhjol.charm.enums.VanillaWoodMaterial;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, description = "Bookcases hold up to 9 stacks of books, paper and maps.")
public class Bookcases extends CharmModule {
    public static final Map<IWoodMaterial, BookcaseBlock> BOOKCASE_BLOCKS = new HashMap<>();
    public static final List<Item> VALID_ITEMS = new ArrayList<>();
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "bookcase");
    public static BlockEntityType<BookcaseBlockEntity> BLOCK_ENTITY;
    public static MenuType<BookcaseMenu> MENU;
    public static SoundEvent BOOKCASE_OPEN_SOUND;
    public static SoundEvent BOOKCASE_CLOSE_SOUND;

    public static final ResourceLocation TRIGGER_ADDED_BOOK_TO_BOOKCASE = new ResourceLocation(Charm.MOD_ID, "added_book_to_bookcase");

    @Config(name = "Valid items", description = "Items that may be placed in bookcases.")
    public static List<String> configValidItems = Arrays.asList(
        "minecraft:book",
        "minecraft:enchanted_book",
        "minecraft:written_book",
        "minecraft:writable_book",
        "minecraft:knowledge_book",
        "minecraft:paper",
        "minecraft:map",
        "minecraft:filled_map"
    );

    @Override
    public void register() {
        BLOCK_ENTITY = CommonRegistry.blockEntity(ID, BookcaseBlockEntity::new);
        MENU = CommonRegistry.menu(ID, BookcaseMenu::new);
        BOOKCASE_OPEN_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "bookcase_open"));
        BOOKCASE_CLOSE_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "bookcase_close"));

        // Register all vanilla wood bookcases.
        for (VanillaWoodMaterial material : VanillaWoodMaterial.values()) {
            registerBookcase(this, material);
        }

        // Register all items that can be placed inside bookcase containers.
        for (String configItem : configValidItems) {
            Registry.ITEM.getOptional(new ResourceLocation(configItem))
                .ifPresent(Bookcases::registerValidItem);
        }
    }

    public static BookcaseBlock registerBookcase(CharmModule module, IWoodMaterial material) {
        var bookcase = new BookcaseBlock(module, material);
        BOOKCASE_BLOCKS.put(material, bookcase);
        return bookcase;
    }

    public static void registerValidItem(Item validItem) {
        if (!VALID_ITEMS.contains(validItem)) {
            VALID_ITEMS.add(validItem);
        }
    }

    public static boolean isValidItem(ItemStack stack) {
        return VALID_ITEMS.contains(stack.getItem());
    }

    public static void triggerAddedBookToBookcase(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_ADDED_BOOK_TO_BOOKCASE);
    }
}
