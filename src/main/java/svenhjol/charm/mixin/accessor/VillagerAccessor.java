package svenhjol.charm.mixin.accessor;

import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Villager.class)
public interface VillagerAccessor {
    @Invoker
    void invokeSetUnhappy();

    @Invoker
    boolean invokeShouldIncreaseLevel();

    @Invoker
    void invokeIncreaseMerchantCareer();
}
