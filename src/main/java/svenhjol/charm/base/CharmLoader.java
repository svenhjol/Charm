package svenhjol.charm.base;

import svenhjol.charm.Charm;
import svenhjol.charm.brewing.message.ClientCakeAction;
import svenhjol.charm.tools.message.ClientGlowingAction;
import svenhjol.charm.world.message.ClientRunePortalAction;
import svenhjol.meson.MesonLoader;
import svenhjol.meson.handler.PacketHandler;
import svenhjol.meson.handler.RegistryHandler;

public class CharmLoader extends MesonLoader
{
    public CharmLoader()
    {
        super(Charm.MOD_ID);
    }

    @Override
    public void earlyInit()
    {
        super.earlyInit();

        int index = 0;
        PacketHandler.HANDLER.registerMessage(index++, ClientCakeAction.class, ClientCakeAction::encode, ClientCakeAction::decode, ClientCakeAction.Handler::handle);
        PacketHandler.HANDLER.registerMessage(index++, ClientGlowingAction.class, ClientGlowingAction::encode, ClientGlowingAction::decode, ClientGlowingAction.Handler::handle);
        PacketHandler.HANDLER.registerMessage(index++, ClientRunePortalAction.class, ClientRunePortalAction::encode, ClientRunePortalAction::decode, ClientRunePortalAction.Handler::handle);

        RegistryHandler.registerSound(CharmSounds.BOOKSHELF_CLOSE);
        RegistryHandler.registerSound(CharmSounds.BOOKSHELF_OPEN);
        RegistryHandler.registerSound(CharmSounds.FUMAROLE_BUBBLING);
        RegistryHandler.registerSound(CharmSounds.FUMAROLE_ERUPT);
        RegistryHandler.registerSound(CharmSounds.HOMING);
        RegistryHandler.registerSound(CharmSounds.WOOD_SMASH);
    }
}
