package svenhjol.charm.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.module.GlowBalls;
import svenhjol.charm.module.PlaceableGlowstoneDust;

public class GlowBallEntity extends ThrownItemEntity {
    public GlowBallEntity(EntityType<? extends GlowBallEntity> entityType, World world) {
        super(entityType, world);
    }

    public GlowBallEntity(World world, LivingEntity owner) {
        super(GlowBalls.ENTITY, owner, world);
    }

    @Environment(EnvType.CLIENT)
    public GlowBallEntity(World world, double x, double y, double z) {
        super(GlowBalls.ENTITY, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return GlowBalls.GLOW_BALL;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        this.discard();

        if (!world.isClient) {
            boolean result = PlaceableGlowstoneDust.tryPlaceDust(world, hitResult);

            if (result)
                return;

            // cannot place, return the glow ball
            if (this.getOwner() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)this.getOwner();

                if (!player.isCreative()) {
                    world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.PLAYERS, 0.7F, 1.0F);
                    PlayerHelper.addOrDropStack(player, new ItemStack(GlowBalls.GLOW_BALL));
                }
            }
        }
    }
}
