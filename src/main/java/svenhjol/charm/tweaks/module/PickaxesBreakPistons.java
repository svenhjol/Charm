package svenhjol.charm.tweaks.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.PistonBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ToolType;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.OverrideHandler;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS)
public class PickaxesBreakPistons extends MesonModule
{
    public static Block block;
    public static Item blockItem;
    public static final ResourceLocation ID = new ResourceLocation("piston");

    @Override
    public void init()
    {
        block = new PistonBlock(false, Block.Properties.from(Blocks.PISTON).harvestTool(ToolType.PICKAXE));
        blockItem = new BlockItem(block, (new Item.Properties()).group(ItemGroup.REDSTONE));

        if (isEnabled()) {
            OverrideHandler.changeVanillaBlock(block, ID);
            OverrideHandler.changeVanillaItem(blockItem, ID);
        }
    }
}
