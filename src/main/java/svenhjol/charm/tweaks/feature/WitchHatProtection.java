package svenhjol.charm.tweaks.feature;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.tweaks.compat.QuarkWitchHat;
import svenhjol.charm.world.event.SpectreAttackEvent;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.ForgeHelper;

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
    public void configure()
    {
        super.configure();

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
    public boolean isEnabled()
    {
        return enabled && ForgeHelper.areModsLoaded("quark");
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
