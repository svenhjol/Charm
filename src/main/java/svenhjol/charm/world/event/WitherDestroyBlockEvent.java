package svenhjol.charm.world.event;

import net.minecraft.block.Block;
import net.minecraftforge.fml.common.eventhandler.Event;

public class WitherDestroyBlockEvent extends Event
{
    private Block block;

    public WitherDestroyBlockEvent(Block block)
    {
        this.block = block;
    }

    public Block getBlock()
    {
        return block;
    }

    @Override
    public boolean isCancelable()
    {
        return true;
    }
}
