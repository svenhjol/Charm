package svenhjol.meson.event;

import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

public class StructureEventBase extends Event
{
    public static class AddComponentPartsEvent extends StructureEventBase
    {
        private StructureComponent component;
        private World world;
        private StructureBoundingBox box;

        public AddComponentPartsEvent(StructureComponent component, World world, StructureBoundingBox box)
        {
            this.component = component;
            this.world = world;
            this.box = box;
        }

        public StructureComponent getComponent()
        {
            return this.component;
        }

        public StructureBoundingBox getBox() { return this.box; }

        public World getWorld()
        {
            return this.world;
        }
    }

    @HasResult
    public static class Pre extends AddComponentPartsEvent
    {
        public Pre(StructureComponent component, World world, StructureBoundingBox bb)
        {
            super(component, world, bb);
        }

        @Override
        public boolean isCancelable()
        {
            return true;
        }
    }

    public static class Post extends AddComponentPartsEvent
    {
        public Post(StructureComponent component, World world, StructureBoundingBox bb)
        {
            super(component, world, bb);
        }
    }
}
