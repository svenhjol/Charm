package svenhjol.meson.iface;

import net.minecraft.nbt.NBTTagCompound;

public interface IMesonTile
{
    String getModId();

    void writeTag(NBTTagCompound tag);

    void readTag(NBTTagCompound tag);
}