package svenhjol.charm.crafting.block;

import net.minecraft.block.material.Material;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.SmoothGlowstone;
import svenhjol.meson.MesonBlock;

public class BlockSmoothGlowstone extends MesonBlock
{
    public BlockSmoothGlowstone()
    {
        super(Material.GLASS, "smooth_glowstone");
        setLightLevel(1.0f);
        setHardness(SmoothGlowstone.hardness);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}
