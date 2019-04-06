package svenhjol.charm.world.feature;

import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.event.StructureEventBase;
import svenhjol.meson.Feature;
import svenhjol.charm.world.decorator.inner.SwampHut;

public class SwampHutDecorations extends Feature
{
    @Override
    public String getDescription()
    {
        return "Swamp huts may generate with a filled cauldron, a chest of loot and a black cat.";
    }

    @SubscribeEvent
    public void onAddComponentParts(StructureEventBase.Post event)
    {
        if (event.getComponent() instanceof ComponentScatteredFeaturePieces.SwampHut && !event.getWorld().isRemote) {
            SwampHut hut = new SwampHut((ComponentScatteredFeaturePieces.SwampHut) event.getComponent(), event.getWorld(), event.getBox());
            hut.generate();
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
