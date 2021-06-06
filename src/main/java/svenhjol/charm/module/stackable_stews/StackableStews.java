package svenhjol.charm.module.stackable_stews;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.mixin.accessor.ItemAccessor;
import svenhjol.charm.module.cooking_pots.CookingPots;

@Module(mod = Charm.MOD_ID, description = "Allows stews to stack.")
public class StackableStews extends CharmModule {
    @Config(name = "Stack size", description = "Maximum stew stack size.")
    public static int stackSize = 64;

    @Config(name = "Enable suspicious stew", description = "Also apply to suspicious stew.")
    public static boolean suspiciousStew = false;

    @Override
    public void init() {
        ((ItemAccessor) Items.MUSHROOM_STEW).setMaxStackSize(stackSize);
        ((ItemAccessor) Items.RABBIT_STEW).setMaxStackSize(stackSize);
        ((ItemAccessor) Items.BEETROOT_SOUP).setMaxStackSize(stackSize);

        if (suspiciousStew)
            ((ItemAccessor) Items.SUSPICIOUS_STEW).setMaxStackSize(stackSize);

        if (ModuleHandler.enabled("charm:cooking_pots"))
            ((ItemAccessor) CookingPots.MIXED_STEW).setMaxStackSize(stackSize);
    }

    public static boolean tryEatStewStack(LivingEntity entity, ItemStack stack) {
        if (!ModuleHandler.enabled(StackableStews.class) || stack.getMaxStackSize() == 1)
            return false;

        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (!PlayerHelper.getAbilities(player).instabuild)
                player.addItem(new ItemStack(Items.BOWL));
        }

        return true;
    }
}
