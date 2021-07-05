package svenhjol.charm.module.bat_buckets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.helper.ItemNbtHelper;
import svenhjol.charm.helper.MobHelper;
import svenhjol.charm.item.CharmItem;
import svenhjol.charm.loader.CharmModule;

import java.util.List;
import java.util.function.Predicate;

public class BatBucketItem extends CharmItem {
    public static final String STORED_BAT_NBT = "StoredBat";

    public BatBucketItem(CharmModule module) {
        super(module, "bat_bucket", new Item.Properties()
            .tab(CreativeModeTab.TAB_MISC)
            .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        BlockPos pos = player.blockPosition();
        Direction facing = player.getDirection();

        if (!world.isClientSide && !player.isCreative()) {
            double x = pos.getX() + 0.5F + facing.getStepX();
            double y = pos.getY() + 0.65F + (world.random.nextFloat() / 2.0F) + facing.getStepY();
            double z = pos.getZ() + 0.5F + facing.getStepZ();
            BlockPos spawnPos = new BlockPos(x, y, z);

            // spawn the bat
            Bat bat = MobHelper.spawn(EntityType.BAT, (ServerLevel)world, spawnPos, MobSpawnType.BUCKET);
            if (bat != null) {

                CompoundTag data = ItemNbtHelper.getCompound(held, STORED_BAT_NBT);
                if (!data.isEmpty())
                    bat.readAdditionalSaveData(data);

                world.addFreshEntity(bat);

                if (BatBuckets.damageBat) {
                    // damage the bat :(
                    float health = bat.getHealth();
                    bat.setHealth(health - 1.0F);
                }
            }
        }

        player.swing(hand);

        if (!world.isClientSide) {
            BatBuckets.triggerUsedBatBucket((ServerPlayer) player);
            world.playSound(null, player.blockPosition(), SoundEvents.BAT_TAKEOFF, SoundSource.NEUTRAL, 1.0F, 1.0F);

            AABB box = player.getBoundingBox().inflate(BatBuckets.glowingRange, BatBuckets.glowingRange / 2.0, BatBuckets.glowingRange);
            Predicate<LivingEntity> selector = entity -> true;
            MobEffectInstance effect = new MobEffectInstance(MobEffects.GLOWING, BatBuckets.glowingTime * 20);
            List<LivingEntity> entities = player.level.getEntitiesOfClass(LivingEntity.class, box, selector);
            entities.forEach(entity -> entity.addEffect(effect));
        }

        if (!player.isCreative())
            player.setItemInHand(hand, new ItemStack(Items.BUCKET));

        return InteractionResultHolder.consume(held);
    }
}
