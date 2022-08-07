package svenhjol.charm.module.bat_buckets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import svenhjol.charm.helper.ItemNbtHelper;
import svenhjol.charm.helper.MobHelper;
import svenhjol.charm.item.CharmItem;
import svenhjol.charm.loader.CharmModule;

public class BatBucketItem extends CharmItem {
    public static final String STORED_BAT_NBT = "StoredBat";

    public BatBucketItem(CharmModule module) {
        super(module, "bat_bucket", new Properties()
            .tab(CreativeModeTab.TAB_MISC)
            .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        BlockPos pos = player.blockPosition();
        Direction facing = player.getDirection();

        if (!level.isClientSide && !player.getAbilities().instabuild) {
            double x = pos.getX() + 0.5F + facing.getStepX();
            double y = pos.getY() + 0.65F + (level.random.nextFloat() / 2.0F) + facing.getStepY();
            double z = pos.getZ() + 0.5F + facing.getStepZ();
            BlockPos spawnPos = new BlockPos(x, y, z);

            // spawn the bat
            MobHelper.spawn(EntityType.BAT, (ServerLevel)level, spawnPos, MobSpawnType.BUCKET, mob -> {
                CompoundTag data = ItemNbtHelper.getCompound(held, STORED_BAT_NBT);
                if (!data.isEmpty()) {
                    mob.readAdditionalSaveData(data);
                }

                BatBuckets.playReleaseSound((ServerLevel) level, player.blockPosition());

                if (BatBuckets.damageBat) {
                    // damage the bat :(
                    float health = mob.getHealth();
                    mob.setHealth(health - 1.0F);
                    if (mob.getHealth() > 0) {
                        BatBuckets.playLaunchSound((ServerLevel) level, mob.blockPosition());
                    }
                }
            });
        }

        player.swing(hand);

        if (!player.level.isClientSide) {
            BatBuckets.triggerUsedBatBucket((ServerPlayer) player);

            int duration = BatBuckets.glowingTime * 20;
            player.addEffect(new MobEffectInstance(BatBuckets.ECHOLOCATION, duration));
        }

        if (!player.getAbilities().instabuild) {
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }

        return InteractionResultHolder.consume(held);
    }
}
