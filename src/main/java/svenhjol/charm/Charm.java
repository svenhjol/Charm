package svenhjol.charm;

import net.minecraftforge.fml.common.Mod;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.compat.QuarkCompat;
import svenhjol.charm.brewing.message.ClientCakeAction;
import svenhjol.charm.tools.message.ClientGlowingAction;
import svenhjol.charm.world.message.ClientRunePortalAction;
import svenhjol.meson.MesonInstance;
import svenhjol.meson.handler.LogHandler;
import svenhjol.meson.handler.PacketHandler;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.helper.ForgeHelper;

@Mod(Charm.MOD_ID)
public class Charm extends MesonInstance
{
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler(Charm.MOD_ID);
    public static QuarkCompat quarkCompat;

    public Charm()
    {
        super(Charm.MOD_ID, LOG);

        int index = 0;
        PacketHandler.HANDLER.registerMessage(index++, ClientCakeAction.class, ClientCakeAction::encode, ClientCakeAction::decode, ClientCakeAction.Handler::handle);
        PacketHandler.HANDLER.registerMessage(index++, ClientGlowingAction.class, ClientGlowingAction::encode, ClientGlowingAction::decode, ClientGlowingAction.Handler::handle);
        PacketHandler.HANDLER.registerMessage(index++, ClientRunePortalAction.class, ClientRunePortalAction::encode, ClientRunePortalAction::decode, ClientRunePortalAction.Handler::handle);
        LOG.debug("Finished registering messages");

        CharmSounds.soundsToRegister.forEach(RegistryHandler::registerSound);
        LOG.debug("Finished registering sounds");

        try {
            if (ForgeHelper.isModLoaded("quark"))
                quarkCompat = QuarkCompat.class.newInstance();

        } catch (Exception e) {
            throw new RuntimeException("Error loading QuarkCompat");
        }
    }
}