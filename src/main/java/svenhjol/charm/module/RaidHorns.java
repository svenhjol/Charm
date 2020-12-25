package svenhjol.charm.module;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.EntityDropsCallback;
import svenhjol.charm.item.RaidHornItem;

@Module(mod = Charm.MOD_ID, description = "Raid horns are sometimes dropped from raid leaders and can be used to call off or start raids.")
public class RaidHorns extends CharmModule {
    public static RaidHornItem RAID_HORN;

    public static double lootingBoost = 0.25D;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a patrol captain dropping a raid horn when killed by the player.")
    public static double dropChance = 0.05D;

    @Config(name = "Volume", description = "Volume of the raid horn sound effect when used.  1.0 is maximum volume.")
    public static double volume = 0.75D;

    @Override
    public void register() {
        RAID_HORN = new RaidHornItem(this);

        EntityDropsCallback.AFTER.register(this::tryDrop);
    }

    public ActionResult tryDrop(LivingEntity entity, DamageSource source, int lootingLevel) {
        if (!entity.world.isClient
            && entity instanceof PatrolEntity
            && source.getAttacker() instanceof PlayerEntity
            && entity.world.random.nextFloat() <= (dropChance + lootingBoost * lootingLevel)
        ) {
            if (((PatrolEntity)entity).isPatrolLeader()) {
                BlockPos pos = entity.getBlockPos();
                ItemStack potion = new ItemStack(RAID_HORN);
                entity.world.spawnEntity(new ItemEntity(entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), potion));
            }
        }

        return ActionResult.PASS;
    }
}
