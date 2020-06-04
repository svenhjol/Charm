package svenhjol.charm.tweaks.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tweaks.block.ImprovedLanternBlock;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.OverrideHandler;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS,
    description = "Vanilla lanterns are now waterloggable and obey gravity.")
public class LanternImprovements extends MesonModule {
    public static Block lanternBlock;
    public static Item lanternItem;
    public static final ResourceLocation LANTERN_ID = new ResourceLocation("lantern");

    @Override
    public void init() {
        Block.Properties props = Block.Properties.from(Blocks.LANTERN);
        lanternBlock = new ImprovedLanternBlock(props);
        lanternItem = new BlockItem(lanternBlock, (new Item.Properties().group(ItemGroup.DECORATIONS)));

        if (enabled) {
            OverrideHandler.changeVanillaBlock(lanternBlock, LANTERN_ID);
            OverrideHandler.changeVanillaItem(lanternItem, LANTERN_ID);
        }
    }

    public static boolean bypassStateCheck(Block block) {
        return Meson.isModuleEnabled("charm:lantern_improvements") && block instanceof ImprovedLanternBlock;
    }
}
