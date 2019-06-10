package svenhjol.charm.brewing.item;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import svenhjol.meson.iface.IMesonBlock;

public class ItemFlavoredCake extends ItemBlock
{
    public ItemFlavoredCake(Block block)
    {
        super(block);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        String name = ((IMesonBlock)block).getName();
        if (name == null) return super.getItemStackDisplayName(stack);

        String[] split = name.replace("cake_", "").split("_");
        for (int i = 0; i < split.length; i++) {
            split[i] = split[i].substring(0, 1).toUpperCase() + split[i].substring(1);
        }

        String newName = " " + String.join(" ", split);
        return I18n.format("charm.cake_of") + newName;
    }
}
