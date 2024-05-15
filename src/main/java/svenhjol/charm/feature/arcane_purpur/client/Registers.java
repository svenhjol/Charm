package svenhjol.charm.feature.arcane_purpur.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpurClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<ArcanePurpurClient> {
    public Registers(ArcanePurpurClient feature) {
        super(feature);
        var registry = feature().registry();
        var common = feature().common();

        registry.itemTab(
            common.registers.chiseledGlyphBlock,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.chiseledBlock,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.glyphBlock,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.block,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.slab,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.stairs,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.block,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
    }
}
