package svenhjol.charm.automation.feature;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import svenhjol.charm.automation.block.BlockVariableRedstoneLight;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.IMesonBlock;

public class VariableRedstoneLamp extends Feature
{
    IMesonBlock block;

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        block = new BlockVariableRedstoneLight();
    }
}
