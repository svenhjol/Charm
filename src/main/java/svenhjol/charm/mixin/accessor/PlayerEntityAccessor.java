package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.inventory.EnderChestInventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PlayerEntity.class)
public interface PlayerEntityAccessor {
    @Accessor
    PlayerInventory getInventory();

    @Accessor
    PlayerAbilities getAbilities();

    @Accessor
    EnderChestInventory getEnderChestInventory();

    @Invoker()
    void invokeSetShoulderEntityLeft(NbtCompound nbt);

    @Invoker()
    void invokeSetShoulderEntityRight(NbtCompound nbt);

    @Invoker()
    void invokeDropShoulderEntity(NbtCompound nbt);
}
