package svenhjol.charm.module.husk_improvements;

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
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.EntityDropItemsCallback;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.loader.CharmCommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Husks spawn anywhere within their biome and have a chance to drop sand.")
public class HuskImprovements extends CharmCommonModule {
    public static double lootingBoost = 0.3D;

    @Config(name = "Spawn anywhere in biome", description = "If true, husks can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop sand when killed", description = "If true, husks drop sand when killed.")
    public static boolean dropSand = true;

    @Config(name = "Maximum drops", description = "Maximum sand dropped when husk is killed.")
    public static int maxDrops = 2;

    @Override
    public void run() {
        EntityDropItemsCallback.AFTER.register(this::tryDrop);
    }

    public static boolean canSpawn() {
        return Charm.LOADER.isEnabled("charm:husk_improvements") && spawnAnywhere;
    }

    private InteractionResult tryDrop(Entity entity, DamageSource source, int lootingLevel) {
        if (dropSand
            && !entity.level.isClientSide
            && entity instanceof Husk
        ) {
            Level world = entity.getCommandSenderWorld();
            BlockPos pos = entity.blockPosition();
            int amount = ItemHelper.getAmountWithLooting(world.random, maxDrops, lootingLevel, (float)lootingBoost);
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Blocks.SAND, amount)));
        }
        return InteractionResult.PASS;
    }
}
