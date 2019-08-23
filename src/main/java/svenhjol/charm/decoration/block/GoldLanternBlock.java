package svenhjol.charm.decoration.block;

import net.minecraft.block.Block;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.IMesonBlock;

public class GoldLanternBlock extends LanternBlock implements IMesonBlock
{
    protected MesonModule module;

    public GoldLanternBlock(MesonModule module)
    {
        super(Block.Properties
            .create(Material.IRON)
            .hardnessAndResistance(3.5F)
            .sound(SoundType.LANTERN)
            .lightValue(15));

        this.module = module;
        register(module, "gold_lantern");
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.DECORATIONS;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (isEnabled() || group == ItemGroup.SEARCH) {
            super.fillItemGroup(group, items);
        }
    }

    @Override
    public boolean isEnabled()
    {
        return module.isEnabled();
    }
}
