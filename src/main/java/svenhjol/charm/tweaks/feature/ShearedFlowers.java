package svenhjol.charm.tweaks.feature;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.Feature;

public class ShearedFlowers extends Feature
{
    public static double chance; // chance that shared flower will drop more

    @Override
    public String getDescription()
    {
        return "Using shears to break a flower has a chance of increasing the number of dropped flowers.";
    }

    @Override
    public void setupConfig()
    {
        // internal
        chance = 0.1D;
    }

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event)
    {
        if (!event.getWorld().isRemote) {
            EntityPlayer player = event.getPlayer();

            if (player.getHeldItemMainhand().getItem() == Items.SHEARS || player.getHeldItemOffhand().getItem() == Items.SHEARS) {
                IBlockState state = event.getWorld().getBlockState(event.getPos());
                Block block = state.getBlock();
                if (block == Blocks.RED_FLOWER
                    || block == Blocks.YELLOW_FLOWER
                    || block == Blocks.DOUBLE_PLANT
                ) {
                    World world = event.getWorld();
                    if (world.rand.nextFloat() <= chance) {
                        ItemStack out = new ItemStack(block, world.rand.nextInt(3), block.getMetaFromState(state));
                        EntityHelper.spawnEntityItem(event.getWorld(), event.getPos(), out);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
