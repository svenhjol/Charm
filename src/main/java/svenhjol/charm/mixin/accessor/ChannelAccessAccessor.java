package svenhjol.charm.mixin.accessor;

import net.minecraft.client.sounds.ChannelAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Set;

@Mixin(ChannelAccess.class)
public interface ChannelAccessAccessor {
    @Accessor
    Set<ChannelAccess.ChannelHandle> getChannels();
}
