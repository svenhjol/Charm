package svenhjol.meson.handler;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.ProxyRegistry;
import svenhjol.meson.iface.IMesonBlock;
import svenhjol.meson.iface.IMesonBlock.IHasCustomItemBlockModel;
import svenhjol.meson.iface.IMesonBlock.IHasCustomStateMapper;
import svenhjol.meson.iface.IMesonItem.IItemCustomModel;
import svenhjol.meson.iface.IMesonItem.IItemVariants;

import java.util.List;
import java.util.Objects;

public final class ModelHandler
{
    @SubscribeEvent
    public static void onRegister(ModelRegistryEvent event)
    {
        ProxyRegistry.items.forEach(ModelHandler::registerModels);
        ProxyRegistry.blocks.forEach(ModelHandler::registerBlockModels);
    }

//    @SubscribeEvent
//    public static void onItemColorRegister(ColorHandlerEvent.Item event)
//    {
//        for (Item item : ProxyRegistry.items) {
//            if (!(item instanceof IMesonItem.IItemColorHandler)) continue;
//            event.getItemColors().registerItemColorHandler(((IMesonItem.IItemColorHandler)item).getItemColor(), item);
//        }
//    }

    public static void registerModels(Item item)
    {
        int meta = 0;

        if (item instanceof IItemCustomModel) {
            ((IItemCustomModel) item).registerModels(item);
        } else {
            ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, meta, loc);
        }

        if (item instanceof IItemVariants) {
            List<ResourceLocation> variants = ((IItemVariants) item).getVariants();
            for (int i = 0; i < variants.size(); i++) {
                ModelResourceLocation loc = new ModelResourceLocation(variants.get(i), "inventory");
                ModelLoader.setCustomModelResourceLocation(item, i, loc);
            }
            ModelLoader.registerItemVariants(item, variants.toArray(new ResourceLocation[0]));
        }

        if (item instanceof ItemBlock && ((ItemBlock)item).getBlock() instanceof IMesonBlock) {
            IMesonBlock block = (IMesonBlock)((ItemBlock)item).getBlock();

            String[] variants = block.getVariants();
            String name = block.getModId() + ":" + block.getName();

            if (variants.length > 1) {
                for (int i = 0; i < variants.length; i++) {
                    String variant = variants[i];
                    ModelResourceLocation inv = new ModelResourceLocation(name, "inventory,variant=" + variant);
                    ModelLoader.setCustomModelResourceLocation(item, i, inv);
                }
            } else {
                ModelResourceLocation loc = new ModelResourceLocation(name, "inventory");
                ModelLoader.setCustomModelResourceLocation(item, meta, loc);
            }

            if (block instanceof IHasCustomStateMapper) {
                ((IHasCustomStateMapper) block).setStateMapper();
            }
                ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");
                ModelLoader.setCustomModelResourceLocation(item, meta, loc);
        }
    }

    public static void registerBlockModels(Block block)
    {
        if (block instanceof IHasCustomStateMapper) {
            ((IHasCustomStateMapper) block).setStateMapper();
        }

        if (block instanceof IHasCustomItemBlockModel) {
            ((IHasCustomItemBlockModel) block).setInventoryItemModel();
        } else {
            int meta = 0;
            Item item = Item.getItemFromBlock(block);
            ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, meta, loc);
        }
    }
}