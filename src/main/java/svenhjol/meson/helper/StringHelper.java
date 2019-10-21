package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;

public class StringHelper
{
    public static String formatBlockPos(BlockPos pos)
    {
        if (pos == null) return "";
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }
}
