package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {
    @Invoker()
    void callSetShoulderEntityLeft(CompoundTag tag);

    @Invoker()
    void callSetShoulderEntityRight(CompoundTag tag);

    @Invoker()
    void callDropShoulderEntity(CompoundTag tag);
}
