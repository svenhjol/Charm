package svenhjol.charm.loot.entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class TotemOfReturningEntry
{
    public BlockPos pos;
    public int dimension;
    public int uses;

    public TotemOfReturningEntry(BlockPos pos, int dimension, int uses)
    {
        this.pos = pos;
        this.dimension = dimension;
        this.uses = uses;
    }

    public TotemOfReturningEntry(int uses)
    {
        this.uses = uses;
    }

    public static TotemOfReturningEntry read(NBTTagCompound tag)
    {
        int uses = tag.getInteger("Uses");

        NBTTagCompound boundTo = tag.getCompoundTag("BoundTo");
        if (boundTo.isEmpty()) {
            return new TotemOfReturningEntry(uses);
        }

        return new TotemOfReturningEntry(BlockPos.fromLong(boundTo.getLong("Position")), boundTo.getInteger("Dimension"), uses);
    }

    public static void write(NBTTagCompound tag, int uses)
    {
        tag.removeTag("BoundTo");
        tag.setInteger("Uses", uses);
    }

    public static void write(NBTTagCompound tag, BlockPos pos, int dimension, int uses)
    {
        if (pos != null) {

            NBTTagCompound boundTo = new NBTTagCompound();
            boundTo.setLong("Position", pos.toLong());
            boundTo.setInteger("Dimension", dimension);
            tag.setTag("BoundTo", boundTo);

        } else {
            tag.removeTag("BoundTo");
        }

        tag.setInteger("Uses", uses);
    }
}