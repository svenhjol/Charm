package svenhjol.charm.feature.variant_chests;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_api.event.BlockItemRenderEvent;
import svenhjol.charm_api.iface.IVariantMaterial;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

@ClientFeature
public class VariantChestsClient extends CharmFeature {
    private VariantChestBlockEntity CACHED_NORMAL_CHEST;
    private VariantTrappedChestBlockEntity CACHED_TRAPPED_CHEST;

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(VariantChests.class));
    }

    @Override
    public void register() {
        CharmClient.instance().registry().blockEntityRenderer(VariantChests.NORMAL_BLOCK_ENTITY, () -> VariantChestBlockEntityRenderer::new);
        CharmClient.instance().registry().blockEntityRenderer(VariantChests.TRAPPED_BLOCK_ENTITY, () -> VariantChestBlockEntityRenderer::new);

        if (isEnabled()) {
            // Add chests to functional and redstone tab.
            for (var item : VariantChests.NORMAL_CHEST_BLOCKS.values()) {
                CharmClient.instance().registry().itemTab(
                    item,
                    CreativeModeTabs.FUNCTIONAL_BLOCKS,
                    Items.CHEST
                );
                CharmClient.instance().registry().itemTab(
                    item,
                    CreativeModeTabs.REDSTONE_BLOCKS,
                    Items.CHEST
                );
            }

            // Add trapped chests to redstone tab.
            for (var item : VariantChests.TRAPPED_CHEST_BLOCKS.values()) {
                CharmClient.instance().registry().itemTab(
                    item,
                    CreativeModeTabs.REDSTONE_BLOCKS,
                    Items.TRAPPED_CHEST
                );
            }
        }
    }

    @Override
    public void runWhenEnabled() {
        CACHED_NORMAL_CHEST = new VariantChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
        CACHED_TRAPPED_CHEST = new VariantTrappedChestBlockEntity(BlockPos.ZERO, Blocks.TRAPPED_CHEST.defaultBlockState());

        BlockItemRenderEvent.INSTANCE.handle(this::handleRenderBlockItem);

        // Add all custom textures to a reference map.
        for (IVariantMaterial material : VariantChests.NORMAL_CHEST_BLOCKS.keySet()) {
            String[] bases = {"trapped", "normal"};
            ChestType[] chestTypes = {ChestType.SINGLE, ChestType.LEFT, ChestType.RIGHT};

            for (String base : bases) {
                for (ChestType chestType : chestTypes) {
                    var chestTypeName = chestType == ChestType.SINGLE ? "" : "_" + chestType.getSerializedName().toLowerCase();
                    var textureId = Charm.instance().makeId("entity/chest/" + material.getSerializedName() + "_" + base + chestTypeName);

                    // Store the texture reference in the chest renderer.
                    VariantChestBlockEntityRenderer.addTexture(material, chestType, textureId, base.equals("trapped"));
                }
            }
        }
    }

    private Optional<BlockEntity> handleRenderBlockItem(ItemStack itemStack, Block block) {
        if (block instanceof VariantChestBlock chest) {
            CACHED_NORMAL_CHEST.setMaterial(chest.getMaterial());
            return Optional.of(CACHED_NORMAL_CHEST);

        } else if (block instanceof VariantTrappedChestBlock chest) {
            CACHED_TRAPPED_CHEST.setMaterial(chest.getMaterial());
            return Optional.of(CACHED_TRAPPED_CHEST);
        }

        return Optional.empty();
    }
}
