package svenhjol.charm.tweaks.feature;

import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonHeadBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolItem;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.meson.Feature;

public class PickaxesBreakPistons extends Feature
{
    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event)
    {
        if (event.getEntityPlayer() != null
            && (event.getState().getBlock() instanceof PistonBlock
                || event.getState().getBlock() instanceof PistonHeadBlock)
        ) {
            ItemStack held = event.getEntityPlayer().getHeldItemMainhand();

            if (held.getItem() instanceof PickaxeItem) {
                ToolItem pickaxe = (PickaxeItem)held.getItem();
                event.setNewSpeed(pickaxe.getTier().getEfficiency());
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
