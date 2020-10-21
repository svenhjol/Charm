package svenhjol.charm.module;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.Charm;
import svenhjol.charm.event.EntityDropsCallback;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.PotionHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "A witch has a chance to drop a Potion of Luck when killed by a player.")
public class WitchesDropLuck extends CharmModule {
    public static double lootingBoost = 0.25D;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a witch dropping a Potion of Luck when killed by the player.")
    public static double dropChance = 0.05D;

    @Override
    public void init() {
        EntityDropsCallback.EVENT.register(this::tryDrop);
    }

    public ActionResult tryDrop(LivingEntity entity, DamageSource damageSource, int lootingLevel) {
        if (!entity.world.isClient
            && entity instanceof WitchEntity
            && damageSource.getAttacker() instanceof PlayerEntity
            && entity.world.random.nextFloat() <= (dropChance + lootingBoost * lootingLevel)
        ) {
            BlockPos pos = entity.getBlockPos();
            ItemStack potion = PotionHelper.getPotionItemStack(Potions.LUCK, 1);
            entity.world.spawnEntity(new ItemEntity(entity.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), potion));
        }
        return ActionResult.PASS;
    }
}
