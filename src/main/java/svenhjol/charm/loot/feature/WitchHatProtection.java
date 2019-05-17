package svenhjol.charm.loot.feature;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.loot.compat.QuarkWitchHat;
import svenhjol.charm.world.event.SpectreAttackEvent;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;

public class WitchHatProtection extends Feature
{
    private QuarkWitchHat quarkWitchHat;

    @Override
    public String getDescription()
    {
        return "Wearing a Witch Hat protects your items from being cursed when touched by a Spectre.\n" +
                "NOTE: Quark must be installed for this feature to be enabled.";
    }

    @Override
    public void setupConfig()
    {
        try {
            quarkWitchHat = QuarkWitchHat.class.newInstance();
        } catch (Exception e) {
            Meson.runtimeException("Error loading QuarkWitchHat");
        }
    }

    @SubscribeEvent
    public void onSpectreAttack(SpectreAttackEvent event)
    {
        if (quarkWitchHat != null && quarkWitchHat.isWearingWitchHat(event.getAttacked())) {
            event.setCanceled(true);
        }
    }

    @Override
    public String[] getRequiredMods()
    {
        return new String[] { "quark" };
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
