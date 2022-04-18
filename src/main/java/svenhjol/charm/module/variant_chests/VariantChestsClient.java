package svenhjol.charm.module.variant_chests;

import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.enums.IWoodMaterial;
import svenhjol.charm.api.event.RenderBlockItemCallback;
import svenhjol.charm.api.event.StitchTextureCallback;
import svenhjol.charm.loader.CharmModule;

import java.util.Set;

@ClientModule(module = VariantChests.class)
public class VariantChestsClient extends CharmModule {
    private final VariantChestBlockEntity CACHED_NORMAL_CHEST = new VariantChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
    private final VariantTrappedChestBlockEntity CACHED_TRAPPED_CHEST = new VariantTrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState());

    @Override
    public void register() {
        BlockEntityRendererRegistry.register(VariantChests.NORMAL_BLOCK_ENTITY, VariantChestBlockEntityRenderer::new);
        BlockEntityRendererRegistry.register(VariantChests.TRAPPED_BLOCK_ENTITY, VariantChestBlockEntityRenderer::new);

        StitchTextureCallback.EVENT.register(this::handleTextureStitch);
        RenderBlockItemCallback.EVENT.register(this::handleBlockItemRender);

        ColorProviderRegistry.ITEM.register(this::handleRegisterBoatLayerColors, VariantChests.CHEST_BOATS.values().toArray(new ItemLike[0]));
    }

    /**
     * Call a handler (getLayerColor) for all variant chest boats.
     */
    private int handleRegisterBoatLayerColors(ItemStack stack, int layer) {
        return layer == 0 ? -1 : VariantChests.getLayerColor(stack);
    }

    private void handleTextureStitch(TextureAtlas atlas, Set<ResourceLocation> textures) {
        if (atlas.location().toString().equals("minecraft:textures/atlas/chest.png")) {
            VariantChests.NORMAL_CHEST_BLOCKS.keySet().forEach(type -> {
                addChestTexture(textures, type, ChestType.LEFT);
                addChestTexture(textures, type, ChestType.RIGHT);
                addChestTexture(textures, type, ChestType.SINGLE);
            });
        }
    }

    private BlockEntity handleBlockItemRender(ItemStack stack, Block block) {
        if (block instanceof VariantChestBlock chest) {
            CACHED_NORMAL_CHEST.setMaterialType(chest.getMaterialType());
            return CACHED_NORMAL_CHEST;

        } else if (block instanceof VariantTrappedChestBlock chest) {
            CACHED_TRAPPED_CHEST.setMaterialType(chest.getMaterialType());
            return CACHED_TRAPPED_CHEST;
        }

        return null;
    }

    private void addChestTexture(Set<ResourceLocation> textures, IWoodMaterial variant, ChestType chestType) {
        String chestTypeName = chestType != ChestType.SINGLE ? "_" + chestType.getSerializedName().toLowerCase() : "";
        String[] bases = {"trapped", "normal"};

        for (String base : bases) {
            ResourceLocation id = new ResourceLocation(getModId(), "entity/chest/" + variant.getSerializedName() + "_" + base + chestTypeName);
            VariantChestBlockEntityRenderer.addTexture(variant, chestType, id, base.equals("trapped"));
            textures.add(id);
        }
    }
}
