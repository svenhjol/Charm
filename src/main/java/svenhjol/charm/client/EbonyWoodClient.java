package svenhjol.charm.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ClientHelper;
import svenhjol.charm.base.helper.EntityHelper;
import svenhjol.charm.module.EbonyWood;

import javax.annotation.Nullable;

public class EbonyWoodClient extends CharmClientModule {
    public EbonyWoodClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        // recolor the leaves block and item according to biome
        ColorProviderRegistry.BLOCK.register(this::handleBlockColor, EbonyWood.LEAVES);
        ColorProviderRegistry.ITEM.register(this::handleItemColor, EbonyWood.LEAVES);

        // cut-out the transparent areas of the blocks
        BlockRenderLayerMap.INSTANCE.putBlock(EbonyWood.SAPLING, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(EbonyWood.DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(EbonyWood.TRAPDOOR, RenderLayer.getCutout());

        // register boat model
        EntityHelper.registerEntityModelLayer(new Identifier(Charm.MOD_ID, "boat/ebony"), BoatEntityModel.getTexturedModelData().createModel());
    }

    private int handleBlockColor(BlockState state, @Nullable BlockRenderView world, @Nullable BlockPos pos, int tintIndex) {
        return world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
    }

    private int handleItemColor(ItemStack stack, int tintIndex) {
        BlockState blockState = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
        return ClientHelper.getBlockColors()
            .map(colors -> colors.getColor(blockState, null, null, tintIndex))
            .orElse(0);
    }
}
