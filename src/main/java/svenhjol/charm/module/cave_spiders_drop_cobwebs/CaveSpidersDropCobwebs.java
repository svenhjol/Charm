package svenhjol.charm.module.cave_spiders_drop_cobwebs;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.CaveSpiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

    public ActionResult tryDropCobweb(LivingEntity entity, DamageSource source, int lootingLevel) {
        if (!entity.world.isClient
            && entity instanceof CaveSpiderEntity
        ) {
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getBlockPos();

            int amount = ItemHelper.getAmountWithLooting(world.random, maxDrops, lootingLevel, (float)lootingBoost);
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.COBWEB, amount)));
        }

        return ActionResult.PASS;
    }
}
