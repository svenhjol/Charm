package svenhjol.charm.feature.arcane_purpur.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.arcane_purpur.ArcanePurpurClient;

public final class Registers extends RegisterHolder<ArcanePurpurClient> {
    public Registers(ArcanePurpurClient feature) {
        super(feature);
        var registry = feature().registry();
        var common = feature().linked();

        registry.itemTab(
            common.registers.chiseledGlyphBlock.get(),
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.chiseledBlock.get(),
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.glyphBlock.get(),
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.block.get(),
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.slab.get(),
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.stairs.get(),
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
        registry.itemTab(
            common.registers.block.get(),
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.PURPUR_SLAB
        );
    }
}
