package svenhjol.charm.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.block.GlowballBlobBlock;
import svenhjol.charm.module.Glowballs;

public class GlowballEntity extends ThrownItemEntity {
    public GlowballEntity(EntityType<? extends GlowballEntity> entityType, World world) {
        super(entityType, world);
    }

    public GlowballEntity(World world, LivingEntity owner) {
        super(Glowballs.GLOWBALL, owner, world);
    }

    @Environment(EnvType.CLIENT)
    public GlowballEntity(World world, double x, double y, double z) {
        super(Glowballs.GLOWBALL, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Glowballs.GLOWBALL_ITEM;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        this.discard();

        if (!world.isClient) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                tryPlaceBlob(world, (BlockHitResult)hitResult);
            } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                tryHitEntity((EntityHitResult)hitResult);
            }
        }
    }

    private void tryPlaceBlob(World world, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        Direction side = hitResult.getSide();
        BlockState state = world.getBlockState(pos);
        BlockPos offsetPos = pos.offset(side);

        if (state.isSideSolidFullSquare(world, pos, side) && (world.isAir(offsetPos) || world.isWater(offsetPos))) {
            BlockState placedState = Glowballs.GLOWBALL_BLOCK.getDefaultState()
                .with(GlowballBlobBlock.FACING, side);

            BlockState offsetState = world.getBlockState(offsetPos);
            if (offsetState.getBlock() == Blocks.WATER)
                placedState = placedState.with(Properties.WATERLOGGED, true);

            world.setBlockState(offsetPos, placedState, 2);
            world.playSound(null, offsetPos, SoundEvents.BLOCK_NYLIUM_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return;
        }

        if (this.getOwner() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity)this.getOwner();

            if (!player.isCreative()) {
                world.playSound(null, pos, SoundEvents.BLOCK_NYLIUM_PLACE, SoundCategory.PLAYERS, 0.7F, 1.0F);
                world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GLOWSTONE_DUST, 1)));
            }
        }
    }

    private void tryHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), 1);
    }
}
