package svenhjol.charm.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.mixin.accessor.ItemAccessor;

import java.util.Stack;

@Module(mod = Charm.MOD_ID, description = "Allows stews to stack.")
public class StackableStews extends CharmModule {
    @Config(name = "Stack size", description = "Maximum stew stack size.")
    public static int stackSize = 16;

    @Config(name = "Enable suspicious stew", description = "Also apply to suspicious stew.")
    public static boolean suspiciousStew = false;

    @Override
    public void init() {
        ((ItemAccessor) Items.MUSHROOM_STEW).setMaxCount(stackSize);
        ((ItemAccessor) Items.RABBIT_STEW).setMaxCount(stackSize);
        ((ItemAccessor) Items.BEETROOT_SOUP).setMaxCount(stackSize);
        if (suspiciousStew)
            ((ItemAccessor) Items.SUSPICIOUS_STEW).setMaxCount(stackSize);
    }

    public static boolean tryEatStewStack(LivingEntity entity, ItemStack stack) {
        if (!ModuleHandler.enabled(StackableStews.class) || stack.getMaxCount() == 1)
            return false;

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!player.abilities.creativeMode)
                player.giveItemStack(new ItemStack(Items.BOWL));
        }

        return true;
    }
}
