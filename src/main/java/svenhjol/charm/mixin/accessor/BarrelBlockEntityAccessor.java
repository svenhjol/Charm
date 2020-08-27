package svenhjol.charm.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BarrelBlockEntity.class)
public interface BarrelBlockEntityAccessor {
    @Invoker("<init>")
    static BarrelBlockEntity invokeConstructor(BlockEntityType<?> typeEntityType) {
        throw new IllegalStateException();
    }

    @Accessor
    int getViewerCount();

    @Accessor
    void setViewerCount(int numPlayersUsing);

    @Invoker()
    void callScheduleUpdate();

    @Invoker
    void callSetOpen(BlockState state, boolean open);

    @Invoker
    void callPlaySound(BlockState state, SoundEvent sound);
}
