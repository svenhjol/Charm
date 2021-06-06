package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(WoodType.class)
@CharmMixin(required = true)
public interface WoodTypeAccessor {
    @Invoker("<init>")
    static WoodType invokeInit(String name) {
        throw new IllegalStateException();
    }

    @Invoker
    static WoodType invokeRegister(WoodType type) {
        throw new IllegalStateException();
    }
}
