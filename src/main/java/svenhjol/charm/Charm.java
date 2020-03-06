package svenhjol.charm;

import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.base.CharmCompat;
import svenhjol.charm.base.CharmMessages;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.compat.QuarkCompat;
import svenhjol.meson.MesonInstance;
import svenhjol.meson.handler.LogHandler;

@Mod(Charm.MOD_ID)
public class Charm extends MesonInstance
{
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler(Charm.MOD_ID);
    public static QuarkCompat quarkCompat;

    public Charm()
    {
        super(Charm.MOD_ID, LOG);

        CharmMessages.init(this);
        CharmSounds.init(this);
        CharmCompat.init(this);
    }
}