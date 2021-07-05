package svenhjol.charm.mixin.accessor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Player.class)
public interface PlayerAccessor {
    @Accessor
    Inventory getInventory();

    @Accessor
    Abilities getAbilities();

    @Accessor
    PlayerEnderChestContainer getEnderChestInventory();

    @Invoker()
    void invokeSetShoulderEntityLeft(CompoundTag nbt);

    @Invoker()
    void invokeSetShoulderEntityRight(CompoundTag nbt);

    @Invoker()
    void invokeRespawnEntityOnShoulder(CompoundTag nbt);
}
