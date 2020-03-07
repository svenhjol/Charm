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

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS,
    description = "Pickaxes become the optimal harvest tool for breaking pistons.")
public class PickaxesBreakPistons extends MesonModule {
    public static Block piston;
    public static Item pistonItem;

    public static Block stickyPiston;
    public static Item stickyPistonItem;

    public static final ResourceLocation PISTON_ID = new ResourceLocation("piston");
    public static final ResourceLocation STICKY_PISTON_ID = new ResourceLocation("sticky_piston");

    @Override
    public void init() {
        piston = new PistonBlock(false, Block.Properties.from(Blocks.PISTON).harvestTool(ToolType.PICKAXE));
        pistonItem = new BlockItem(piston, (new Item.Properties()).group(ItemGroup.REDSTONE));

        stickyPiston = new PistonBlock(true, Block.Properties.from(Blocks.STICKY_PISTON).harvestTool(ToolType.PICKAXE));
        stickyPistonItem = new BlockItem(stickyPiston, (new Item.Properties()).group(ItemGroup.REDSTONE));

        if (enabled) {
            OverrideHandler.changeVanillaBlock(piston, PISTON_ID);
            OverrideHandler.changeVanillaItem(pistonItem, PISTON_ID);
            OverrideHandler.changeVanillaBlock(stickyPiston, STICKY_PISTON_ID);
            OverrideHandler.changeVanillaItem(stickyPistonItem, STICKY_PISTON_ID);
        }
    }
}
