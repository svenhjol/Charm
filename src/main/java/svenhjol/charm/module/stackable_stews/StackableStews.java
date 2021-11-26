package svenhjol.charm.module.stackable_stews;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Allows stews to stack.")
public class StackableStews extends CharmModule {
    @Config(name = "Stack size", description = "Maximum stew stack size.")
    public static int stackSize = 16;

    @Config(name = "Enable suspicious stew", description = "Also apply to suspicious stew.")
    public static boolean suspiciousStew = false;

    @Override
    public void runWhenEnabled() {
        Items.MUSHROOM_STEW.maxStackSize = stackSize;
        Items.RABBIT_STEW.maxStackSize = stackSize;
        Items.BEETROOT_SOUP.maxStackSize = stackSize;

        if (suspiciousStew) {
            Items.SUSPICIOUS_STEW.maxStackSize = stackSize;
        }
    }

    public static boolean tryEatStewStack(LivingEntity entity, ItemStack stack) {
        if (!Charm.LOADER.isEnabled(StackableStews.class) || stack.getMaxStackSize() == 1)
            return false;

        if (entity instanceof Player player) {
            if (!player.getAbilities().instabuild) {
                player.addItem(new ItemStack(Items.BOWL));
            }
        }

        return true;
    }
}
