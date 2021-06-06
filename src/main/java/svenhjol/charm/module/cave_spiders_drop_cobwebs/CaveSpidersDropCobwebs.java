package svenhjol.charm.module.cave_spiders_drop_cobwebs;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.event.EntityDropItemsCallback;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "Cave spiders have a chance to drop cobwebs.",
    requiresMixins = {"EntityDropItemsCallback"})
public class CaveSpidersDropCobwebs extends CharmModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Maximum drops", description = "Maximum cobwebs dropped when cave spider is killed.")
    public static int maxDrops = 2;

    @Override
    public void init() {
        EntityDropItemsCallback.AFTER.register((this::tryDropCobweb));
    }

    public InteractionResult tryDropCobweb(LivingEntity entity, DamageSource source, int lootingLevel) {
        if (!entity.level.isClientSide
            && entity instanceof CaveSpider
        ) {
            Level world = entity.getCommandSenderWorld();
            BlockPos pos = entity.blockPosition();

            int amount = ItemHelper.getAmountWithLooting(world.random, maxDrops, lootingLevel, (float)lootingBoost);
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.COBWEB, amount)));
        }

        return InteractionResult.PASS;
    }
}
