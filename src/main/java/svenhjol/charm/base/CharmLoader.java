package svenhjol.charm.base;

import net.minecraftforge.common.crafting.CraftingHelper;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.message.ClientCakeAction;
import svenhjol.charm.tools.message.ClientGlowingAction;
import svenhjol.charm.world.message.ClientRunePortalAction;
import svenhjol.meson.MesonLoader;
import svenhjol.meson.handler.PacketHandler;
import svenhjol.meson.handler.RegistryHandler;
import svenhjol.meson.loader.condition.ModuleEnabledCondition;

public class CharmLoader extends MesonLoader
{
    public CharmLoader()
    {
        super(Charm.MOD_ID);

        // register crafting recipe conditions
        ModuleEnabledCondition.Serializer modEnabledCondition = new ModuleEnabledCondition.Serializer();
        CraftingHelper.register(modEnabledCondition);
    }

    @Override
    public void earlyInit()
    {
        super.earlyInit();

        int index = 0;
        PacketHandler.HANDLER.registerMessage(index++, ClientCakeAction.class, ClientCakeAction::encode, ClientCakeAction::decode, ClientCakeAction.Handler::handle);
        PacketHandler.HANDLER.registerMessage(index++, ClientGlowingAction.class, ClientGlowingAction::encode, ClientGlowingAction::decode, ClientGlowingAction.Handler::handle);
        PacketHandler.HANDLER.registerMessage(index++, ClientRunePortalAction.class, ClientRunePortalAction::encode, ClientRunePortalAction::decode, ClientRunePortalAction.Handler::handle);

        CharmSounds.soundsToRegister.forEach(RegistryHandler::registerSound);
    }
}
