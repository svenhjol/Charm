package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.IMesonItem.IItemCustomModelRegister;

import java.util.List;
import java.util.Objects;

public final class ModelHandler
{
    @SubscribeEvent
    public static void onRegister(ModelRegistryEvent event)
    {
        ProxyRegistry.items.forEach(ModelHandler::registerItemModels);
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

    public static void registerItemModels(Item item)
    {
        int meta = 0;

        if (item instanceof IItemCustomModelRegister) {
            ((IItemCustomModelRegister) item).registerItemModels(item);
        } else {
            ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, meta, loc);
        }

        if (item instanceof IMesonItem.IVariants) {
            List<ResourceLocation> variants = ((IMesonItem.IVariants) item).getVariants();
            for (int i = 0; i < variants.size(); i++) {
                ModelResourceLocation loc = new ModelResourceLocation(variants.get(i), "inventory");
                ModelLoader.setCustomModelResourceLocation(item, i, loc);
            }
            ModelLoader.registerItemVariants(item, variants.toArray(new ResourceLocation[0]));
        }
    }

    public static void registerBlockModels(Block block)
    {
        if (block instanceof IMesonBlock.IHasCustomStateMapper) {
            ((IMesonBlock.IHasCustomStateMapper) block).setStateMapper();
        }

        if (block instanceof IMesonBlock.IHasCustomInventoryItemModel) {
            ((IMesonBlock.IHasCustomInventoryItemModel) block).setInventoryItemModel();
        } else {
            int meta = 0;
            Item item = Item.getItemFromBlock(block);
            ModelResourceLocation loc = new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, meta, loc);
        }
    }
}