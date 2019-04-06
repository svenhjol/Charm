package svenhjol.charm.loot.feature;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.loot.compat.CompatWitchHatProtection;
import svenhjol.charm.world.event.SpectreAttackEvent;
import svenhjol.meson.Feature;
import svenhjol.meson.FeatureCompat;

public class WitchHatProtection extends Feature
{
    @Override
    public String getDescription()
    {
        return "Wearing a Witch Hat protects your items from being cursed when touched by a Spectre.\n" +
                "NOTE: Quark must be installed for this feature to be enabled.";
    }

    @SubscribeEvent
    public void onSpectreAttack(SpectreAttackEvent event)
    {
        CompatWitchHatProtection compat = (CompatWitchHatProtection) getCompat();
        if (compat != null && compat.isWearingWitchHat(event.getAttacked())) {
            event.setCanceled(true);
        }
    }

    @Override
    public String[] getRequiredMods()
    {
        return new String[] { "quark" };
    }

    @Override
    public Class<? extends FeatureCompat> getCompatClass()
    {
        return CompatWitchHatProtection.class;
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
