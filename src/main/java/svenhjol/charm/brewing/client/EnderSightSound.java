package svenhjol.charm.brewing.client;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

public class EnderSightSound extends MovingSound
{
    private EntityPlayer player;

    public EnderSightSound(SoundEvent sound, EntityPlayer player, float volume, float pitch)
    {
        super(sound, SoundCategory.NEUTRAL);
        this.player = player;
        this.volume = volume;
        this.pitch = pitch;
    }

    @Override
    public void update()
    {
        this.xPosF = (float)this.player.posX;
        this.yPosF = (float)this.player.posY;
        this.zPosF = (float)this.player.posZ;
    }
}
