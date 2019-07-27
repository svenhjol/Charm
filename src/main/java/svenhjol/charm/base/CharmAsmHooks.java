package svenhjol.charm.base;

import svenhjol.charm.Charm;
import svenhjol.charm.tweaks.feature.RemovePotionGlint;

public class CharmAsmHooks
{
    public static boolean removePotionGlint()
    {
        return Charm.hasFeature(RemovePotionGlint.class);
    }
}
