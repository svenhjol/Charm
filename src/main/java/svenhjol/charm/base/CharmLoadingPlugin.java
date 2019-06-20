package svenhjol.charm.base;

import net.minecraftforge.common.config.Configuration;
import svenhjol.meson.asm.MesonLoadingPlugin;

import java.io.File;
import java.util.Map;

public class CharmLoadingPlugin extends MesonLoadingPlugin
{
    public static Configuration config;
    public static Configuration configAsm;

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{ "svenhjol.charm.base.CharmClassTransformer" };
    }

    @Override
    public void injectData(Map<String, Object> data) {
        super.injectData(data);

        // load the primary config so coremod can get feature configs early
        config = new Configuration(new File(((File) data.get("mcLocation")).getPath() + "/config/charm.cfg"), true);
        config.load();

        // load the ASM config so coremod can enable/disable patches
        configAsm = new Configuration(new File(((File) data.get("mcLocation")).getPath() + "/config/charm_asm.cfg"), true);
        configAsm.load();
    }
}
