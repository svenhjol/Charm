package svenhjol.charm.feature.core.custom_wood.holders;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.feature.core.custom_wood.blocks.CustomSaplingBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class Sapling {
    public final Supplier<CustomSaplingBlock> block;
    public final Supplier<CustomSaplingBlock.BlockItem> item;

    public Sapling(CustomWoodHolder holder) {
        var material = holder.getMaterial();
        var saplingId = holder.getMaterialName() + "_sapling";

        var tree = material.tree();
        if (tree.isEmpty()) {
            holder.owner().log().error("No tree defined for sapling!");
        }

        ResourceKey<ConfiguredFeature<?, ?>> key = ResourceKey.create(Registries.CONFIGURED_FEATURE, tree.get().location());

        var treeGrower = new AbstractTreeGrower() {
            @Nullable
            @Override
            protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource randomSource, boolean bl) {
                return key;
            }
        };

        block = holder.ownerRegistry().block(saplingId, () -> new CustomSaplingBlock(holder.getMaterial(), treeGrower));
        item = holder.ownerRegistry().item(saplingId, () -> new CustomSaplingBlock.BlockItem(block));

        holder.addItemToCreativeTab(item, CustomType.SAPLING);
    }
}
