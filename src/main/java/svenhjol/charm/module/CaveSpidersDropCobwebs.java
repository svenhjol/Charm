package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.meson.event.EntityDropsCallback;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(description = "Cave spiders have a chance to drop cobwebs.")
public class CaveSpidersDropCobwebs extends MesonModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Maximum drops", description = "Maximum cobwebs dropped when cave spider is killed.")
    public static int maxDrops = 2;

    @Override
    public void init() {
        EntityDropsCallback.EVENT.register(((entity, source, lootingLevel) -> {
            tryDropCobweb(entity, lootingLevel);
            return ActionResult.PASS;
        }));
    }

    public void tryDropCobweb(LivingEntity entity, int lootingLevel) {
        if (!entity.world.isClient
            && entity instanceof CaveSpiderEntity
        ) {
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getBlockPos();

            int amount = ItemHelper.getAmountWithLooting(world.random, (int)maxDrops, lootingLevel, (float)lootingBoost);
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.COBWEB, amount)));
        }
    }
}
