package svenhjol.charm.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.module.GlowPearls;
import svenhjol.charm.module.PlaceableGlowstoneDust;

public class GlowPearlEntity extends ThrownItemEntity {
    public GlowPearlEntity(EntityType<? extends GlowPearlEntity> entityType, World world) {
        super(entityType, world);
    }

    public GlowPearlEntity(World world, LivingEntity owner) {
        super(GlowPearls.ENTITY, owner, world);
    }

    @Environment(EnvType.CLIENT)
    public GlowPearlEntity(World world, double x, double y, double z) {
        super(GlowPearls.ENTITY, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return GlowPearls.GLOW_PEARL;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        BlockPos hitPos = new BlockPos(this.getX(), this.getY(), this.getZ());
        if (!world.isClient && PlaceableGlowstoneDust.canPlaceAt(world, hitPos)) {
            this.world.setBlockState(hitPos, PlaceableGlowstoneDust.PLACED_GLOWSTONE_DUST.getDefaultState());
            this.world.playSound(null, hitPos, SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        this.remove();
    }
}
