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

public final class ModelHandler
{
    @SubscribeEvent
    public static void onRegister(ModelRegistryEvent event)
    {
        ProxyRegistry.items.forEach(item -> {
            registerItemModels(item);
        });

        ProxyRegistry.blocks.forEach(block -> {
            registerBlockModels(block);
        });
    }

    public static void registerItemModels(Item item)
    {
        int meta = 0;

        if (item instanceof IItemCustomModelRegister) {
            ((IItemCustomModelRegister) item).registerItemModels(item);
        } else {
            ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
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

//        if (item instanceof MesonItemBlock) {
//            MesonItemBlock itemBlock = (MesonItemBlock)item;
//            ResourceLocation[] variants = new ResourceLocation[itemBlock.variants.size()];
//            variants = itemBlock.variants.toArray(variants);
//
//            for (int i = 0; i < variants.length; i++) {
//                ResourceLocation variant = variants[i];
//                ModelResourceLocation loc = new ModelResourceLocation(variant, "inventory");
//                ModelLoader.setCustomModelResourceLocation(itemBlock, i, loc);
//            }
//
//            ModelLoader.registerItemVariants(itemBlock, variants);
//            ModelBakery.registerItemVariants(itemBlock, variants);
//            ModelLoader.setCustomMeshDefinition(itemBlock, itemBlock.getCustomMeshDefinition());
//        } else {
//
//            ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
//            ModelLoader.setCustomModelResourceLocation(item, meta, loc);
//        }
    }

    public static void registerBlockModels(Block block)
    {
        if (block instanceof IMesonBlock.IHasCustomStateMapper) {
            ((IMesonBlock.IHasCustomStateMapper) block).setStateMapper();
        }

        if (block instanceof IMesonBlock.IHasCustomInventoryItemModel) {
            ((IMesonBlock.IHasCustomInventoryItemModel) block).setInventoryItemModel();
        } else {
            /** @todo Item variants */
            int meta = 0;
            Item item = Item.getItemFromBlock(block);
            ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
            ModelLoader.setCustomModelResourceLocation(item, meta, loc);
        }
    }
}