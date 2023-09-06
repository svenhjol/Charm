package svenhjol.charm.feature.custom_wood.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmSaplingBlock;

import java.util.function.Supplier;

public class CustomSapling {
    public final Supplier<CharmSaplingBlock> block;
    public final Supplier<CharmSaplingBlock.BlockItem> item;

    public CustomSapling(CustomWoodHolder holder) {
        var saplingId = holder.getMaterialName() + "_sapling";
        var treeId = holder.getMaterialName() + "_tree";
        var key = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(holder.getModId(), treeId));

        block = holder.getRegistry().block(saplingId, () -> new CharmSaplingBlock(holder.getFeature(), holder.getMaterial(), new AbstractTreeGrower() {
            @Override
            protected ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasBees) {
                return key;
            }
        }));

        item = holder.getRegistry().item(saplingId, () -> new CharmSaplingBlock.BlockItem(holder.getFeature(), block));

        holder.addCreativeTabItem(CustomWoodHelper.SAPLINGS, item);
    }
}
