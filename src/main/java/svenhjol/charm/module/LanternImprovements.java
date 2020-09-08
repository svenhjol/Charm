package svenhjol.charm.module;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import svenhjol.charm.block.ImprovedLanternBlock;
import svenhjol.charm.mixin.accessor.BlocksAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.OverrideHandler;
import svenhjol.meson.iface.Module;

@Module(description = "Adds gravity to vanilla lanterns")
public class LanternImprovements extends MesonModule {
    @Override
    public void afterInit() {
        Identifier lantern = new Identifier("lantern");
        Identifier soulLantern = new Identifier("soul_lantern");

        Block lanternBlock = new ImprovedLanternBlock(AbstractBlock.Settings.copy(Blocks.LANTERN));
        Block soulLanternBlock = new ImprovedLanternBlock(AbstractBlock.Settings.copy(Blocks.SOUL_LANTERN));
        Item lanternItem = new BlockItem(lanternBlock, new Item.Settings().group(ItemGroup.DECORATIONS));
        Item soulLanternItem = new BlockItem(soulLanternBlock, new Item.Settings().group(ItemGroup.DECORATIONS));

        BlocksAccessor.setLantern(OverrideHandler.changeBlock(lantern, lanternBlock));
        BlocksAccessor.setSoulLantern(OverrideHandler.changeBlock(soulLantern, soulLanternBlock));

        // TODO probably need to change items here
        OverrideHandler.changeItem(lantern, lanternItem);
        OverrideHandler.changeItem(soulLantern, soulLanternItem);
    }
}
