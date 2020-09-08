package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.meson.event.EntityDropsCallback;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(description = "Strays spawn anywhere within their biome and have a chance to drop blue ice.")
public class StrayImprovements extends MesonModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Spawn anywhere in biome", description = "If true, strays can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop blue ice when killed", description = "If true, strays drop blue ice when killed.")
    public static boolean dropIce = true;

    @Config(name = "Maximum drops", description = "Maximum blue ice dropped when stray is killed.")
    public static int maxDrops = 2;

    @Override
    public void initWhenEnabled() {
        EntityDropsCallback.EVENT.register(((entity, source, lootingLevel) -> {
            if (dropIce
                && !entity.world.isClient
                && entity instanceof StrayEntity
            ) {
                World world = entity.getEntityWorld();
                BlockPos pos = entity.getBlockPos();
                int amount = ItemHelper.getAmountWithLooting(world.random, (int)maxDrops, lootingLevel, (float)lootingBoost);
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.BLUE_ICE, amount)));
            }
            return ActionResult.PASS;
        }));
    }

    public static boolean canSpawn() {
        return Meson.enabled("charm:stray_improvements") && spawnAnywhere;
    }
}
