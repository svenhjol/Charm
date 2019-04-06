package svenhjol.meson;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class MesonBlock extends Block implements IMesonBlock
{
    protected String name;

    public MesonBlock(Material material, String name)
    {
        super(material);
        this.register(name);
    }
}