package svenhjol.charm.module.ebony_wood;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.ClientRegistry;

import javax.annotation.Nullable;

@ClientModule(module = EbonyWood.class)
public class EbonyWoodClient extends CharmModule {

    @Override
    public void register() {
        // recolor the leaves block and item according to biome
        ColorProviderRegistry.BLOCK.register(this::handleBlockColor, EbonyWood.LEAVES);
        ColorProviderRegistry.ITEM.register(this::handleItemColor, EbonyWood.LEAVES);

        // cut-out the transparent areas of the blocks
        BlockRenderLayerMap.INSTANCE.putBlock(EbonyWood.SAPLING, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(EbonyWood.DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(EbonyWood.TRAPDOOR, RenderType.cutout());

        // register boat model
        ClientRegistry.entityModelLayer(new ResourceLocation(Charm.MOD_ID, "boat/ebony"), BoatModel.createBodyModel().bakeRoot());

        // register sign material
        ClientRegistry.signMaterial(EbonyWood.SIGN_TYPE);
    }

    private int handleBlockColor(BlockState state, @Nullable BlockAndTintGetter world, @Nullable BlockPos pos, int tintIndex) {
        return world != null && pos != null ? BiomeColors.getAverageFoliageColor(world, pos) : FoliageColor.getDefaultColor();
    }

    private int handleItemColor(ItemStack stack, int tintIndex) {
        BlockState blockState = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();
        return ClientHelper.getBlockColors()
            .map(colors -> colors.getColor(blockState, null, null, tintIndex))
            .orElse(0);
    }
}
