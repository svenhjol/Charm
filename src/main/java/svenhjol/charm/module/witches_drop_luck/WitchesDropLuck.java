package svenhjol.charm.module.witches_drop_luck;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potions;
import svenhjol.charm.Charm;
import svenhjol.charm.event.EntityDropItemsCallback;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.helper.PotionHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "A witch has a chance to drop a Potion of Luck when killed by a player.")
public class WitchesDropLuck extends CommonModule {
    public static double lootingBoost = 0.25D;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a witch dropping a Potion of Luck when killed by the player.")
    public static double dropChance = 0.05D;

    @Override
    public void init() {
        EntityDropItemsCallback.AFTER.register(this::tryDrop);
    }

    public InteractionResult tryDrop(LivingEntity entity, DamageSource damageSource, int lootingLevel) {
        if (!entity.level.isClientSide
            && entity instanceof Witch
            && damageSource.getEntity() instanceof Player
            && entity.level.random.nextFloat() <= (dropChance + lootingBoost * lootingLevel)
        ) {
            BlockPos pos = entity.blockPosition();
            ItemStack potion = PotionHelper.getPotionItemStack(Potions.LUCK, 1);
            entity.level.addFreshEntity(new ItemEntity(entity.getCommandSenderWorld(), pos.getX(), pos.getY(), pos.getZ(), potion));
        }
        return InteractionResult.PASS;
    }
}
