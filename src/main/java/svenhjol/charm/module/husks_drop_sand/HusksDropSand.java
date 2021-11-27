package svenhjol.charm.module.husks_drop_sand;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.event.EntityDropItemsCallback;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "A husk has a chance to drop sand when killed.")
public class HusksDropSand extends CharmModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Maximum drops", description = "Maximum sand dropped when husk is killed.")
    public static int maxDrops = 2;

    @Override
    public void runWhenEnabled() {
        EntityDropItemsCallback.AFTER.register(this::tryDrop);
    }

    private InteractionResult tryDrop(Entity entity, DamageSource source, int lootingLevel) {
        if (!entity.level.isClientSide && entity instanceof Husk husk) {
            Level level = husk.getCommandSenderWorld();
            BlockPos pos = husk.blockPosition();
            int amount = ItemHelper.getAmountWithLooting(level.random, maxDrops, lootingLevel, (float)lootingBoost);
            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.SAND, amount)));
        }
        return InteractionResult.PASS;
    }
}
