package svenhjol.charm.module.bat_buckets;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ItemNBTHelper;
import svenhjol.charm.helper.MobHelper;
import svenhjol.charm.item.CharmItem;

import java.util.List;
import java.util.function.Predicate;

public class BatBucketItem extends CharmItem {
    public static final String STORED_BAT_NBT = "StoredBat";

    public BatBucketItem(CharmModule module) {
        super(module, "bat_bucket", new Item.Settings()
            .group(ItemGroup.MISC)
            .maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack held = player.getStackInHand(hand);
        BlockPos pos = player.getBlockPos();
        Direction facing = player.getHorizontalFacing();

        if (!world.isClient && !player.isCreative()) {
            double x = pos.getX() + 0.5F + facing.getOffsetX();
            double y = pos.getY() + 0.65F + (world.random.nextFloat() / 2.0F) + facing.getOffsetY();
            double z = pos.getZ() + 0.5F + facing.getOffsetZ();
            BlockPos spawnPos = new BlockPos(x, y, z);

            // spawn the bat
            BatEntity bat = MobHelper.spawn(EntityType.BAT, (ServerWorld)world, spawnPos, SpawnReason.BUCKET);
            if (bat != null) {

                NbtCompound data = ItemNBTHelper.getCompound(held, STORED_BAT_NBT);
                if (!data.isEmpty())
                    bat.readCustomDataFromNbt(data);

                world.spawnEntity(bat);

                if (BatBuckets.damageBat) {
                    // damage the bat :(
                    float health = bat.getHealth();
                    bat.setHealth(health - 1.0F);
                }
            }
        }

        player.swingHand(hand);

        if (!world.isClient) {
            BatBuckets.triggerUsedBatBucket((ServerPlayerEntity) player);
            world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.NEUTRAL, 1.0F, 1.0F);

            Box box = player.getBoundingBox().expand(BatBuckets.glowingRange, BatBuckets.glowingRange / 2.0, BatBuckets.glowingRange);
            Predicate<LivingEntity> selector = entity -> true;
            StatusEffectInstance effect = new StatusEffectInstance(StatusEffects.GLOWING, BatBuckets.glowingTime * 20);
            List<LivingEntity> entities = player.world.getEntitiesByClass(LivingEntity.class, box, selector);
            entities.forEach(entity -> entity.addStatusEffect(effect));
        }

        if (!player.isCreative())
            player.setStackInHand(hand, new ItemStack(Items.BUCKET));

        return TypedActionResult.consume(held);
    }
}
