package svenhjol.meson.handler;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.container.CrateContainer;
import svenhjol.meson.Meson;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonEffect;
import svenhjol.meson.iface.IMesonItem;
import svenhjol.meson.iface.IMesonPotion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(bus = MOD)
public class RegistrationHandler
{
    public static List<IMesonBlock> BLOCKS = new ArrayList<>();
    public static List<IMesonItem> ITEMS = new ArrayList<>();
    public static List<IMesonPotion> POTIONS = new ArrayList<>();
    public static List<IMesonEffect> EFFECTS = new ArrayList<>();
    public static Map<ResourceLocation, Supplier<? extends TileEntity>> TILE_ENTITIES = new HashMap<>();

    public static void addBlock(IMesonBlock block)
    {
        ((Block)block).setRegistryName(new ResourceLocation(block.getModId(), block.getBaseName()));
        BLOCKS.add(block);
    }

    public static void addEffect(IMesonEffect effect)
    {
        ((Effect)effect).setRegistryName(new ResourceLocation(effect.getModId(), effect.getBaseName()));
        EFFECTS.add(effect);
    }

    public static void addItem(IMesonItem item)
    {
        ((Item)item).setRegistryName(new ResourceLocation(item.getModId(), item.getBaseName()));
        ITEMS.add(item);
    }

    public static void addPotion(IMesonPotion potion)
    {
        ((Potion)potion).setRegistryName(new ResourceLocation(potion.getModId(), potion.getBaseName()));
        POTIONS.add(potion);
    }

    public static void addTileEntity(ResourceLocation res, Supplier<? extends TileEntity> tile)
    {
        TILE_ENTITIES.put(res, tile);
    }

    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event)
    {
        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
            event.getRegistry().register((Block)block);
        }
        Meson.log("Block registration done");
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        final IForgeRegistry<Item> registry = event.getRegistry();

        // register items
        for (IMesonItem item : RegistrationHandler.ITEMS) {
            registry.register((Item)item);
        }

        // register block items
        for (IMesonBlock block : RegistrationHandler.BLOCKS) {
            BlockItem blockItem = block.getBlockItem();
            registry.register(blockItem);
        }

        Meson.log("Item registration done");
    }

    @SubscribeEvent
    public static void onRegisterEffects(final RegistryEvent.Register<Effect> event)
    {
        for (IMesonEffect effect : RegistrationHandler.EFFECTS) {
            event.getRegistry().register((Effect)effect);
        }
        Meson.log("Effect registration done");
    }

    @SubscribeEvent
    public static void onRegisterPotions(final RegistryEvent.Register<Potion> event)
    {
        // register potions
        for (IMesonPotion p : RegistrationHandler.POTIONS) {
            Potion potion = (Potion)p;
            event.getRegistry().register(potion);

            // register the recipe
            ((IMesonPotion)potion).registerRecipe();
        }
        Meson.log("Potion registration done");
    }

    @SubscribeEvent
    public static void onRegisterTileEntities(final RegistryEvent.Register<TileEntityType<?>> event) throws Exception
    {
        for (ResourceLocation res : TILE_ENTITIES.keySet()) {
            TileEntityType<? extends TileEntity> type = TileEntityType.Builder.create(TILE_ENTITIES.get(res)).build(null);
            event.getRegistry().register(type.setRegistryName(res));
        }

        // This is how you register things directly, for reference
        // TileEntityType<CrateTileEntity> type = TileEntityType.Builder.create(CrateTileEntity::new).build(null);
        // type.setRegistryName(Charm.MOD_ID, "crate");
        // event.getRegistry().register(type);

        Meson.log("Tile Entity registration done");
    }

    @SubscribeEvent
    public static void onRegisterContainers(final RegistryEvent.Register<ContainerType<?>> event)
    {
        ContainerType<CrateContainer> type = new ContainerType<>(CrateContainer::new);
        type.setRegistryName(new ResourceLocation(Charm.MOD_ID, "crate_container"));
        event.getRegistry().register(type);

        Meson.log("Container registration done");
    }
}
