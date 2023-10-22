package svenhjol.charm.feature.arcane_purpur;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.StairBlock;
import svenhjol.charm.feature.arcane_purpur.block.*;
import svenhjol.charmony.common.CommonFeature;

import java.util.function.Supplier;

import static svenhjol.charm.feature.arcane_purpur.ArcanePurpurMaterial.ARCANE_PURPUR;

public class ArcanePurpur extends CommonFeature {
    static final String BLOCK_ID = "arcane_purpur_block";
    static final String SLAB_ID = "arcane_purpur_slab";
    static final String STAIRS_ID = "arcane_purpur_stairs";
    static final String GLYPH_BLOCK_ID = "arcane_purpur_glyph_block";
    static final String CHISELED_BLOCK_ID = "chiseled_arcane_purpur_block";
    static final String CHISELED_GLYPH_BLOCK_ID = "chiseled_arcane_purpur_glyph_block";
    static Supplier<ArcanePurpurBlock> block;
    static Supplier<BlockItem> blockItem;
    static Supplier<ArcanePurpurSlab> slab;
    static Supplier<BlockItem> slabItem;
    static Supplier<? extends StairBlock> stairs;
    static Supplier<BlockItem> stairsItem;
    static Supplier<ArcanePurpurGlyphBlock> glyphBlock;
    static Supplier<BlockItem> glyphBlockItem;
    static Supplier<ChiseledArcanePurpurBlock> chiseledBlock;
    static Supplier<BlockItem> chiseledBlockItem;
    static Supplier<ChiseledArcanePurpurGlyphBlock> chiseledGlyphBlock;
    static Supplier<BlockItem> chiseledGlyphBlockItem;

    @Override
    public String description() {
        return """
            Arcane Purpur is a decorative block made from Purpur and Endermite Powder.
            If the ChorusTeleport feature is enabled, a Chiseled Arcane Purpur block
            allows teleportation when a chorus fruit is eaten within range of the block.
            """;
    }

    @Override
    public void register() {
        var registry = mod().registry();

        block = registry.block(BLOCK_ID,
            () -> new ArcanePurpurBlock(this));
        blockItem = registry.item(BLOCK_ID,
            () -> new ArcanePurpurBlock.BlockItem<>(this, block));
        slab = registry.block(SLAB_ID,
            () -> new ArcanePurpurSlab(this, ARCANE_PURPUR));
        slabItem = registry.item(SLAB_ID,
            () -> new ArcanePurpurSlab.BlockItem(this, slab));

        // Stairs register differently.
        var stairs = registry.stairsBlock(STAIRS_ID,
            this, ARCANE_PURPUR, () -> block.get().defaultBlockState());
        ArcanePurpur.stairs = stairs.getFirst();
        stairsItem = stairs.getSecond();

        glyphBlock = registry.block(GLYPH_BLOCK_ID,
            () -> new ArcanePurpurGlyphBlock(this));
        glyphBlockItem = registry.item(GLYPH_BLOCK_ID,
            () -> new ArcanePurpurGlyphBlock.BlockItem<>(this, glyphBlock));
        chiseledBlock = registry.block(CHISELED_BLOCK_ID,
            () -> new ChiseledArcanePurpurBlock(this));
        chiseledBlockItem = registry.item(CHISELED_BLOCK_ID,
            () -> new ChiseledArcanePurpurBlock.BlockItem<>(this, chiseledBlock));
        chiseledGlyphBlock = registry.block(CHISELED_GLYPH_BLOCK_ID,
            () -> new ChiseledArcanePurpurGlyphBlock(this));
        chiseledGlyphBlockItem = registry.item(CHISELED_GLYPH_BLOCK_ID,
            () -> new ChiseledArcanePurpurGlyphBlock.BlockItem<>(this, chiseledGlyphBlock));
    }
}
