package svenhjol.charm.base;

import net.minecraftforge.common.config.Configuration;
import svenhjol.meson.asm.MesonLoadingPlugin;

import java.io.File;
import java.util.Map;

public class CharmLoadingPlugin extends MesonLoadingPlugin
{
    public static Configuration config;

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{ "svenhjol.charm.base.CharmClassTransformer" };
    }

    @Override
    public void injectData(Map<String, Object> data) {
        super.injectData(data);

        File configFile = new File(((File) data.get("mcLocation")).getPath() + "/config/charm.cfg");
        config = new Configuration(configFile);
    }
}
