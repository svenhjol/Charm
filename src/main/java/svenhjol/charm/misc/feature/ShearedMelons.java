package svenhjol.charm.misc.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;

public class ShearedMelons extends Feature
{
    public static float chance;
    public static int maxPieces;

    @Override
    public String getDescription()
    {
        return "Using shears to break a melon has a chance to drop all 9 melon pieces.";
    }

    @Override
    public void setupConfig()
    {
        // internal
        chance = 0.75f;
        maxPieces = 9;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreak(BlockEvent.HarvestDropsEvent event)
    {
        if (!event.getWorld().isRemote
            && event.getHarvester() != null
            && event.getState().getBlock() == Blocks.MELON_BLOCK
        ) {
            EntityPlayer player = event.getHarvester();

            if (player.getHeldItemMainhand().getItem() == Items.SHEARS || player.getHeldItemOffhand().getItem() == Items.SHEARS) {
                if (event.getWorld().rand.nextFloat() <= chance) {
                    event.getDrops().clear();
                    event.getDrops().add(new ItemStack(Items.MELON, maxPieces));
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
