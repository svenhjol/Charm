package svenhjol.charm.module.stray_improvements;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.event.EntityDropItemsCallback;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Strays spawn anywhere within their biome and have a chance to drop blue ice.")
public class StrayImprovements extends CharmModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Spawn anywhere in biome", description = "If true, strays can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop blue ice when killed", description = "If true, strays drop blue ice when killed.")
    public static boolean dropIce = true;

    @Config(name = "Maximum drops", description = "Maximum blue ice dropped when stray is killed.")
    public static int maxDrops = 2;

    @Override
    public void runWhenEnabled() {
        EntityDropItemsCallback.AFTER.register(this::tryDrop);
    }

    public static boolean canSpawn() {
        return Charm.LOADER.isEnabled("charm:stray_improvements") && spawnAnywhere;
    }

    private InteractionResult tryDrop(Entity entity, DamageSource source, int lootingLevel) {
        if (dropIce
            && !entity.level.isClientSide
            && entity instanceof Stray
        ) {
            Level world = entity.getCommandSenderWorld();
            BlockPos pos = entity.blockPosition();
            int amount = ItemHelper.getAmountWithLooting(world.random, (int)maxDrops, lootingLevel, (float)lootingBoost);
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.BLUE_ICE, amount)));
        }
        return InteractionResult.PASS;
    }
}
