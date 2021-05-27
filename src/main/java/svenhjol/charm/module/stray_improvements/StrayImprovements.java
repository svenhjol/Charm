package svenhjol.charm.module.stray_improvements;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.event.EntityDropItemsCallback;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "Strays spawn anywhere within their biome and have a chance to drop blue ice.",
    requiresMixins = {"EntityDropItemsCallback"})
public class StrayImprovements extends CharmModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Spawn anywhere in biome", description = "If true, strays can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop blue ice when killed", description = "If true, strays drop blue ice when killed.")
    public static boolean dropIce = true;

    @Config(name = "Maximum drops", description = "Maximum blue ice dropped when stray is killed.")
    public static int maxDrops = 2;

    @Override
    public void init() {
        EntityDropItemsCallback.AFTER.register(this::tryDrop);
    }

    public static boolean canSpawn() {
        return ModuleHandler.enabled("charm:stray_improvements") && spawnAnywhere;
    }

    private ActionResult tryDrop(Entity entity, DamageSource source, int lootingLevel) {
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
    }
}
