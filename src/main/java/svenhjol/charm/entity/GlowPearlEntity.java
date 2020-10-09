package svenhjol.charm.entity;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.module.GlowPearls;

public class GlowPearlEntity extends ThrownItemEntity {
    public GlowPearlEntity(EntityType<? extends GlowPearlEntity> entityType, World world) {
        super(entityType, world);
    }

    public GlowPearlEntity(World world, LivingEntity owner) {
        super(EntityType.ENDER_PEARL, owner, world);
    }

    @Override
    protected Item getDefaultItem() {
        return GlowPearls.GLOW_PEARL;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        BlockPos hitPos = new BlockPos(this.getX(), this.getY(), this.getZ());
        this.world.setBlockState(hitPos, Blocks.REDSTONE_WIRE.getDefaultState());
        this.remove();
    }
}
