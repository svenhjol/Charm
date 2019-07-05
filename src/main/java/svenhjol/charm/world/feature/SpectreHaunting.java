package svenhjol.charm.world.feature;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.EntityHelper;

public class SpectreHaunting extends Feature
{
    @Override
    public boolean checkSelf()
    {
        return Charm.hasFeature(Spectre.class);
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END
            && event.side.isServer()
            && event.player.world.getTotalWorldTime() % 30 == 0
            && event.player.world.rand.nextFloat() < 0.1f
            && event.player.world.getLightBrightness(event.player.getPosition()) < ((float)Spectre.despawnLight)/16.0
            && event.player.getPosition().getY() <= Spectre.spawnDepth
        ) {
            Meson.debug("Spectre spawned ", event.player.getPosition());
            EntityHelper.spawnEntityNearPlayer(event.player, 15,6, new ResourceLocation(Charm.MOD_ID, "spectre"));

            // check mineshaft
//            BlockPos mineshaft = WorldHelper.getNearestStructure(event.player.world, event.player.getPosition(), WorldHelper.StructureType.MINESHAFT);
//            if (mineshaft != null && WorldHelper.getDistanceSq(event.player.getPosition(), mineshaft) < 1000) {
//
//            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
