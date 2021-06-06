package svenhjol.charm.mixin.accessor;

import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(Villager.class)
@CharmMixin(required = true)
public interface VillagerAccessor {
    @Invoker
    void invokeSetUnhappy();

    @Invoker
    boolean invokeShouldIncreaseLevel();

    @Invoker
    void invokeIncreaseMerchantCareer();
}
