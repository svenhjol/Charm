package svenhjol.charm.mixin.accessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface PlayerAccessor {
    @Invoker("respawnEntityOnShoulder")
    void invokeRespawnEntityOnShoulder(CompoundTag tag);

    @Invoker("setShoulderEntityLeft")
    void invokeSetShoulderEntityLeft(CompoundTag tag);

    @Invoker("setShoulderEntityRight")
    void invokeSetShoulderEntityRight(CompoundTag tag);
}
