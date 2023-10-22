package svenhjol.charm.feature.arcane_purpur;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.StairBlock;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony_api.iface.IVariantMaterial;

import java.util.function.Supplier;

public class ArcanePurpur extends CommonFeature {
    static final String ARCANE_PURPUR_BLOCK_ID = "arcane_purpur_block";
    static final String ARCANE_PURPUR_SLAB_ID = "arcane_purpur_slab";
    static final String ARCANE_PURPUR_STAIRS_ID = "arcane_purpur_stairs";
    static final IVariantMaterial MATERIAL = new ArcanePurpurMaterial();
    static Supplier<ArcanePurpurBlock> arcanePurpurBlock;
    static Supplier<BlockItem> arcanePurpurBlockItem;
    static Supplier<ArcanePurpurSlab> arcanePurpurSlab;
    static Supplier<BlockItem> arcanePurpurSlabItem;
    static Supplier<? extends StairBlock> arcanePurpurStairs;
    static Supplier<BlockItem> arcanePurpurStairsItem;

    @Override
    public void register() {
        var registry = mod().registry();

        arcanePurpurBlock = registry.block(ARCANE_PURPUR_BLOCK_ID,
            () -> new ArcanePurpurBlock(this));
        arcanePurpurBlockItem = registry.item(ARCANE_PURPUR_BLOCK_ID,
            () -> new ArcanePurpurBlock.BlockItem(this, arcanePurpurBlock));
        arcanePurpurSlab = registry.block(ARCANE_PURPUR_SLAB_ID,
            () -> new ArcanePurpurSlab(this, MATERIAL));
        arcanePurpurSlabItem = registry.item(ARCANE_PURPUR_SLAB_ID,
            () -> new ArcanePurpurSlab.BlockItem(this, arcanePurpurSlab));

        var stairs = registry.stairsBlock(ARCANE_PURPUR_STAIRS_ID,
            this, MATERIAL, () -> arcanePurpurBlock.get().defaultBlockState());
        arcanePurpurStairs = stairs.getFirst();
        arcanePurpurStairsItem = stairs.getSecond();
    }
}
