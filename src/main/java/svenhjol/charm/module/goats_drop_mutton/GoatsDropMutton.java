package svenhjol.charm.module.goats_drop_mutton;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.event.EntityDropItemsCallback;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Goats have a chance to drop mutton when killed.")
public class GoatsDropMutton extends CharmModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Maximum drops", description = "Maximum pieces of mutton dropped when goat is killed.")
    public static int maxDrops = 2;

    @Override
    public void runWhenEnabled() {
        EntityDropItemsCallback.AFTER.register((this::tryDropMutton));
    }

    public InteractionResult tryDropMutton(LivingEntity entity, DamageSource source, int lootingLevel) {
        if (!entity.level.isClientSide && entity instanceof Goat) {
            Level level = entity.getCommandSenderWorld();
            BlockPos pos = entity.blockPosition();
            Item item;

            int amount = ItemHelper.getAmountWithLooting(level.random, maxDrops, lootingLevel, (float)lootingBoost);

            if (entity.isOnFire()) {
                item = Items.COOKED_MUTTON;
            } else {
                item = Items.MUTTON;
            }

            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item, amount)));
        }

        return InteractionResult.PASS;
    }
}
