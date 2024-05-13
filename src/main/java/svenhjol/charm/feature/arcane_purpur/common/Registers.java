package svenhjol.charm.feature.arcane_purpur.common;

import net.minecraft.world.item.BlockItem;
import svenhjol.charm.api.iface.IVariantMaterial;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpur;
import svenhjol.charm.feature.arcane_purpur.block.*;
import svenhjol.charm.foundation.block.CharmStairBlock;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<ArcanePurpur> {
    public static final String BLOCK_ID = "arcane_purpur_block";
    public static final String SLAB_ID = "arcane_purpur_slab";
    public static final String STAIRS_ID = "arcane_purpur_stairs";
    public static final String GLYPH_BLOCK_ID = "arcane_purpur_glyph_block";
    public static final String CHISELED_BLOCK_ID = "chiseled_arcane_purpur_block";
    public static final String CHISELED_GLYPH_BLOCK_ID = "chiseled_arcane_purpur_glyph_block";

    public final Supplier<IVariantMaterial> material;
    public final Supplier<ArcanePurpurBlock> block;
    public final Supplier<BlockItem> blockItem;
    public final Supplier<ArcanePurpurSlab> slab;
    public final Supplier<BlockItem> slabItem;
    public final Supplier<CharmStairBlock> stairs;
    public final Supplier<CharmStairBlock.BlockItem> stairsItem;
    public final Supplier<ArcanePurpurGlyphBlock> glyphBlock;
    public final Supplier<ArcanePurpurGlyphBlock.BlockItem> glyphBlockItem;
    public final Supplier<ChiseledArcanePurpurBlock> chiseledBlock;
    public final Supplier<ChiseledArcanePurpurBlock.BlockItem> chiseledBlockItem;
    public final Supplier<ChiseledArcanePurpurGlyphBlock> chiseledGlyphBlock;
    public final Supplier<ChiseledArcanePurpurGlyphBlock.BlockItem> chiseledGlyphBlockItem;

    public Registers(ArcanePurpur feature) {
        super(feature);
        var registry = feature().registry();

        material = () -> ArcanePurpurMaterial.ARCANE_PURPUR;

        block = registry.block(BLOCK_ID,
            ArcanePurpurBlock::new);
        blockItem = registry.item(BLOCK_ID,
            () -> new ArcanePurpurBlock.BlockItem(block));
        slab = registry.block(SLAB_ID,
            () -> new ArcanePurpurSlab(material.get()));
        slabItem = registry.item(SLAB_ID,
            () -> new ArcanePurpurSlab.BlockItem(slab));

        // Stairs register in two parts.
        var pair = registry.stairsBlock(STAIRS_ID,
            material, () -> block.get().defaultBlockState());
        stairs = pair.getFirst();
        stairsItem = pair.getSecond();

        glyphBlock = registry.block(GLYPH_BLOCK_ID,
            ArcanePurpurGlyphBlock::new);
        glyphBlockItem = registry.item(GLYPH_BLOCK_ID,
            () -> new ArcanePurpurGlyphBlock.BlockItem(glyphBlock));
        chiseledBlock = registry.block(CHISELED_BLOCK_ID,
            ChiseledArcanePurpurBlock::new);
        chiseledBlockItem = registry.item(CHISELED_BLOCK_ID,
            () -> new ChiseledArcanePurpurBlock.BlockItem(chiseledBlock));
        chiseledGlyphBlock = registry.block(CHISELED_GLYPH_BLOCK_ID,
            ChiseledArcanePurpurGlyphBlock::new);
        chiseledGlyphBlockItem = registry.item(CHISELED_GLYPH_BLOCK_ID,
            () -> new ChiseledArcanePurpurGlyphBlock.BlockItem(chiseledGlyphBlock));
    }
}
