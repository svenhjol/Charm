package svenhjol.charm.mixin.accessor;

import net.minecraft.entity.passive.VillagerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(VillagerEntity.class)
@CharmMixin(required = true)
public interface VillagerEntityAccessor {
    @Invoker
    void invokeSayNo();

    @Invoker
    boolean invokeCanLevelUp();

    @Invoker
    void invokeLevelUp();
}
