package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonBlock;

public class GoldLanternBlock extends LanternBlock implements IMesonBlock
{
    public GoldLanternBlock()
    {
        super(Block.Properties
            .create(Material.IRON)
            .hardnessAndResistance(3.5F)
            .sound(SoundType.LANTERN)
            .lightValue(15));

        register();
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public String getBaseName()
    {
        return "gold_lantern";
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.DECORATIONS;
    }
}
