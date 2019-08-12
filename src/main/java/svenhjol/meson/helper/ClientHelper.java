package svenhjol.meson.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientHelper
{
    @OnlyIn(Dist.CLIENT)
    public static ClientWorld getClientWorld()
    {
        return Minecraft.getInstance().world;
    }
}
