package svenhjol.charm.base;

import svenhjol.meson.asm.MesonLoadingPlugin;

public class CharmLoadingPlugin extends MesonLoadingPlugin
{
    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{ "svenhjol.charm.base.ClassTransformer" };
    }
}
