package svenhjol.meson.handler;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.meson.Meson;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonEffect;
import svenhjol.meson.iface.IMesonItem;
import svenhjol.meson.iface.IMesonPotion;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

@Mod.EventBusSubscriber(bus = MOD)
public class RegistrationHandler
{
    // meson registry entries
    public static List<IMesonBlock> BLOCKS = new ArrayList<>();
    public static List<IMesonItem> ITEMS = new ArrayList<>();
    public static List<IMesonPotion> POTIONS = new ArrayList<>();
    public static List<IMesonEffect> EFFECTS = new ArrayList<>();
    public static Map<ResourceLocation, SoundEvent> SOUNDS = new HashMap<>();
    public static Map<ResourceLocation, Supplier<? extends TileEntity>> TILE_ENTITIES = new HashMap<>();

    // keep track of vanilla things to override
    public static List<Block> BLOCK_OVERRIDES = new ArrayList<>();
    public static List<Item> ITEM_OVERRIDES = new ArrayList<>();

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

    public static void addSound(ResourceLocation res, SoundEvent sound)
    {
        SOUNDS.put(res, sound);
    }

    public static void addTileEntity(ResourceLocation res, Supplier<? extends TileEntity> tile)
    {
        TILE_ENTITIES.put(res, tile);
    }

    public static void addBlockOverride(String id, Block block, @Nullable Item blockItem)
    {
        BLOCK_OVERRIDES.add(block.setRegistryName(new ResourceLocation(id)));

        if (blockItem != null) {
            addItemOverride(id, blockItem);
        }
    }

    public static void addItemOverride(String id, Item item)
    {
        ITEM_OVERRIDES.add(item.setRegistryName(new ResourceLocation(id)));
    }

    @SubscribeEvent
    public static void onRegisterBlocks(final RegistryEvent.Register<Block> event)
    {
        for (IMesonBlock block : BLOCKS) {
            event.getRegistry().register((Block)block);
        }

        // register block overrides
        for (Block block : BLOCK_OVERRIDES) {
            event.getRegistry().register(block);
            Registry.register(Registry.BLOCK, block.getRegistryName(), block);
        }

        Meson.log("Block registration done");
    }

    @SubscribeEvent
    public static void onRegisterItems(final RegistryEvent.Register<Item> event)
    {
        final IForgeRegistry<Item> registry = event.getRegistry();

        // register items
        for (IMesonItem item : ITEMS) {
            registry.register((Item)item);
        }

        // register block items
        for (IMesonBlock block : BLOCKS) {
            BlockItem blockItem = block.getBlockItem();
            registry.register(blockItem);
        }

        // register item overrides
        for (Item item : ITEM_OVERRIDES) {
            registry.register(item);
            Registry.register(Registry.ITEM, item.getRegistryName(), item);
        }

        Meson.log("Item registration done");
    }

    @SubscribeEvent
    public static void onRegisterEffects(final RegistryEvent.Register<Effect> event)
    {
        for (IMesonEffect effect : EFFECTS) {
            event.getRegistry().register((Effect)effect);
        }
        Meson.log("Effect registration done");
    }

    @SubscribeEvent
    public static void onRegisterPotions(final RegistryEvent.Register<Potion> event)
    {
        // register potions
        for (IMesonPotion p : POTIONS) {
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
    public static void onRegisterSounds(final RegistryEvent.Register<SoundEvent> event)
    {
        for (ResourceLocation res : SOUNDS.keySet()) {
            SoundEvent sound = SOUNDS.get(res);
            event.getRegistry().register(sound.setRegistryName(res));
        }

        Meson.log("Sound registration done");
    }
}
