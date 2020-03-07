package svenhjol.meson.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ClientHelper {
    @OnlyIn(Dist.CLIENT)
    public static ClientWorld getClientWorld() {
        return Minecraft.getInstance().world;
    }

    @OnlyIn(Dist.CLIENT)
    public static PlayerEntity getClientPlayer() {
        return Minecraft.getInstance().player;
    }
}
