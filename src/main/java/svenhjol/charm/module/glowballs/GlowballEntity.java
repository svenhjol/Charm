package svenhjol.charm.module.glowballs;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class GlowballEntity extends ThrowableItemProjectile {
    public GlowballEntity(EntityType<? extends GlowballEntity> entityType, Level world) {
        super(entityType, world);
    }

    public GlowballEntity(Level world, LivingEntity owner) {
        super(Glowballs.GLOWBALL, owner, world);
    }

    @Environment(EnvType.CLIENT)
    public GlowballEntity(Level world, double x, double y, double z) {
        super(Glowballs.GLOWBALL, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return Glowballs.GLOWBALL_ITEM;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        this.discard();

        if (!level.isClientSide) {
            if (hitResult.getType() == HitResult.Type.BLOCK) {
                tryPlaceBlob(level, (BlockHitResult)hitResult);
            } else if (hitResult.getType() == HitResult.Type.ENTITY) {
                tryHitEntity((EntityHitResult)hitResult);
            }
        }
    }

    private void tryPlaceBlob(Level world, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        Direction side = hitResult.getDirection();
        BlockState state = world.getBlockState(pos);
        BlockPos offsetPos = pos.relative(side);
        BlockState offsetState = world.getBlockState(offsetPos);

        if (state.isFaceSturdy(world, pos, side)
            && (world.isEmptyBlock(offsetPos) || (offsetState.getMaterial() == Material.WATER && offsetState.getValue(LiquidBlock.LEVEL) == 0))) {
            BlockState placedState = Glowballs.GLOWBALL_BLOCK.defaultBlockState()
                .setValue(GlowballBlobBlock.FACING, side);

            if (offsetState.getBlock() == Blocks.WATER)
                placedState = placedState.setValue(BlockStateProperties.WATERLOGGED, true);

            world.setBlock(offsetPos, placedState, 2);
            world.playSound(null, offsetPos, SoundEvents.NYLIUM_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (this.getOwner() instanceof Player && !world.isClientSide)
                Glowballs.triggerThrownGlowball((ServerPlayer) this.getOwner());

            return;
        }

        if (this.getOwner() instanceof Player) {
            Player player = (Player)this.getOwner();

            if (!player.isCreative()) {
                world.playSound(null, pos, SoundEvents.NYLIUM_PLACE, SoundSource.PLAYERS, 0.7F, 1.0F);
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.GLOWSTONE_DUST, 1)));
            }
        }
    }

    private void tryHitEntity(EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        entity.hurt(DamageSource.thrown(this, this.getOwner()), 1);
    }
}
