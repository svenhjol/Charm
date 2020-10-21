package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HuskEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.event.EntityDropsCallback;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Husks spawn anywhere within their biome and have a chance to drop sand.")
public class HuskImprovements extends CharmModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Spawn anywhere in biome", description = "If true, husks can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop sand when killed", description = "If true, husks drop sand when killed.")
    public static boolean dropSand = true;

    @Config(name = "Maximum drops", description = "Maximum sand dropped when husk is killed.")
    public static int maxDrops = 2;

    @Override
    public void init() {
        EntityDropsCallback.EVENT.register(this::tryDrop);
    }

    public static boolean canSpawn() {
        return ModuleHandler.enabled("charm:husk_improvements") && spawnAnywhere;
    }

    private ActionResult tryDrop(Entity entity, DamageSource source, int lootingLevel) {
        if (dropSand
            && !entity.world.isClient
            && entity instanceof HuskEntity
        ) {
            World world = entity.getEntityWorld();
            BlockPos pos = entity.getBlockPos();
            int amount = ItemHelper.getAmountWithLooting(world.random, maxDrops, lootingLevel, (float)lootingBoost);
            world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.SAND, amount)));
        }
        return ActionResult.PASS;
    }
}
