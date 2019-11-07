package svenhjol.charm.base;

import svenhjol.charm.Charm;
import svenhjol.charm.base.message.*;
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

//        PacketHandler.HANDLER.registerMessage(index++, MessageBookshelfInteract.class, MessageBookshelfInteract::encode, MessageBookshelfInteract::decode, MessageBookshelfInteract.Handler::handle);
        PacketHandler.HANDLER.registerMessage(index++, ClientCakeAction.class, ClientCakeAction::encode, ClientCakeAction::decode, ClientCakeAction.Handler::handle);
//        PacketHandler.HANDLER.registerMessage(index++, MessageCrateInteract.class, MessageCrateInteract::encode, MessageCrateInteract::decode, MessageCrateInteract.Handler::handle);
        PacketHandler.HANDLER.registerMessage(index++, ClientGlowingAction.class, ClientGlowingAction::encode, ClientGlowingAction::decode, ClientGlowingAction.Handler::handle);
//        PacketHandler.HANDLER.registerMessage(index++, MessageMagneticPickup.class, MessageMagneticPickup::encode, MessageMagneticPickup::decode, MessageMagneticPickup.Handler::handle);

        RegistryHandler.registerSound(CharmSounds.BOOKSHELF_CLOSE);
        RegistryHandler.registerSound(CharmSounds.BOOKSHELF_OPEN);
        RegistryHandler.registerSound(CharmSounds.FUMAROLE_BUBBLING);
        RegistryHandler.registerSound(CharmSounds.FUMAROLE_ERUPT);
        RegistryHandler.registerSound(CharmSounds.HOMING);
        RegistryHandler.registerSound(CharmSounds.WOOD_SMASH);
    }
}
