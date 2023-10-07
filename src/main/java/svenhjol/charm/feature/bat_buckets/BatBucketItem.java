package svenhjol.charm.feature.bat_buckets;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.echolocation.Echolocation;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.base.CharmonyItem;
import svenhjol.charmony.helper.ItemNbtHelper;
import svenhjol.charmony.helper.MobHelper;

public class BatBucketItem extends CharmonyItem {
    static final String STORED_BAT_TAG = "stored_bat";

    public BatBucketItem(CharmonyFeature feature) {
        super(feature, new Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var held = player.getItemInHand(hand);
        var pos = player.blockPosition();
        var facing = player.getDirection();

        if (!level.isClientSide() && !player.getAbilities().instabuild) {
            var x = pos.getX() + 0.5D + facing.getStepX();
            var y = pos.getY() + 0.65D + (level.random.nextDouble() / 2.0D) + facing.getStepY();
            var z = pos.getZ() + 0.5D + facing.getStepZ();
            var spawnPos = new BlockPos((int) x, (int) y, (int) z);

            // Spawn the bat.
            MobHelper.spawn(EntityType.BAT, (ServerLevel)level, spawnPos, MobSpawnType.BUCKET, mob -> {
                var data = ItemNbtHelper.getCompound(held, STORED_BAT_TAG);
                if (!data.isEmpty()) {
                    mob.readAdditionalSaveData(data);
                }
                BatBuckets.playReleaseSound((ServerLevel)level, player.blockPosition());

                // Damage bat by half a heart.
                float health = mob.getHealth();
                mob.setHealth(health - 1.0F);
                if (mob.getHealth() > 0) {
                    BatBuckets.playLaunchSound((ServerLevel)level, mob.blockPosition());
                }
            });
        }

        player.swing(hand);

        if (!player.level().isClientSide()) {
            // TODO: advancement.
            player.addEffect(new MobEffectInstance(Echolocation.MOB_EFFECT.get(), BatBuckets.GLOW_TIME * 20));
        }

        // Put empty bucket back in player's hand.
        if (!player.getAbilities().instabuild) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }

        return InteractionResultHolder.consume(held);
    }
}
