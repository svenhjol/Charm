package svenhjol.charm.decoration.module;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.decoration.block.BookshelfChestBlock;
import svenhjol.charm.decoration.container.BookshelfChestContainer;
import svenhjol.charm.decoration.inventory.BookshelfChestScreen;
import svenhjol.charm.decoration.tileentity.BookshelfChestTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.WoodType;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.helper.ClientHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.Module;

import java.util.*;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION,
    description = "A bookshelf with 9 slots that can store books.\n" +
        "Provides the same enchanting power as a normal bookshelf as long as there is at least 1 book on the shelf.")
public class BookshelfChests extends MesonModule
{
    public static List<Class<? extends Item>> validItems = new ArrayList<>();
    public static Map<WoodType, IMesonBlock> blocks = new HashMap<>();

    @Config(name = "Valid books", description = "List of item IDs that may be placed in a bookshelf chest.")
    public static List<String> configValidItems = Arrays.asList(
        "minecraft:book",
        "minecraft:enchanted_book",
        "minecraft:written_book",
        "minecraft:writable_book",
        "minecraft:knowledge_book"
    );

    @ObjectHolder("charm:bookshelf_chest")
    public static ContainerType<BookshelfChestContainer> container;

    @ObjectHolder("charm:bookshelf_chest")
    public static TileEntityType<BookshelfChestTileEntity> tile;

    @Override
    public void init()
    {
        // create all wood types for bookshelves
        for (WoodType wood : WoodType.values()) {
            blocks.put(wood, new BookshelfChestBlock(this, wood));
        }

        // register tile and container
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, "bookshelf_chest");
        container = new ContainerType<>(BookshelfChestContainer::instance);
        tile = TileEntityType.Builder.create(BookshelfChestTileEntity::new).build(null);
        RegistryHandler.registerTile(tile, res);
        RegistryHandler.registerContainer(container, res);
    }

    public static boolean canInsertItem(ItemStack stack)
    {
        return validItems.contains(stack.getItem().getClass());
    }

    @Override
    public void setup(FMLCommonSetupEvent event)
    {
        configValidItems.forEach(string -> {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(string));
            if (item != null) {
                validItems.add(item.getClass());
            }
        });
    }

    @Override
    public void setupClient(FMLClientSetupEvent event)
    {
        ScreenManager.registerFactory(container, BookshelfChestScreen::new);
    }

    @OnlyIn(Dist.CLIENT)
    public static void effectInteract(BlockPos pos, int type)
    {
        ClientWorld world = ClientHelper.getClientWorld();
        PlayerEntity player = ClientHelper.getClientPlayer();
        if (!player.isSpectator()) {
            SoundEvent sound = type == 0 ? CharmSounds.BOOKSHELF_OPEN : CharmSounds.BOOKSHELF_CLOSE;
            world.playSound(player, pos, sound, SoundCategory.BLOCKS, 0.5f, world.rand.nextFloat() * 0.1F + 0.9F);
        }
    }
}
