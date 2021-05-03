package svenhjol.charm.module;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.mixin.accessor.ItemAccessor;

@Module(mod = Charm.MOD_ID, description = "Allows stews to stack.")
public class StackableStews extends CharmModule {
    @Config(name = "Stack size", description = "Maximum stew stack size.")
    public static int stackSize = 64;

    @Config(name = "Enable suspicious stew", description = "Also apply to suspicious stew.")
    public static boolean suspiciousStew = false;

    @Override
    public void init() {
        ((ItemAccessor) Items.MUSHROOM_STEW).setMaxCount(stackSize);
        ((ItemAccessor) Items.RABBIT_STEW).setMaxCount(stackSize);
        ((ItemAccessor) Items.BEETROOT_SOUP).setMaxCount(stackSize);

        if (suspiciousStew)
            ((ItemAccessor) Items.SUSPICIOUS_STEW).setMaxCount(stackSize);

        if (ModuleHandler.enabled("charm:cooking_pots"))
            ((ItemAccessor) CookingPots.MIXED_STEW).setMaxCount(stackSize);
    }

    public static boolean tryEatStewStack(LivingEntity entity, ItemStack stack) {
        if (!ModuleHandler.enabled(StackableStews.class) || stack.getMaxCount() == 1)
            return false;

        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            if (!PlayerHelper.getAbilities(player).creativeMode)
                player.giveItemStack(new ItemStack(Items.BOWL));
        }

        return true;
    }
}
