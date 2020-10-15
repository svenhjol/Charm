package svenhjol.charm.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

public class SmoothGlowstoneBlock extends MesonBlock {
    public SmoothGlowstoneBlock(MesonModule module) {
        super(module, "smooth_glowstone", Settings.copy(Blocks.GLOWSTONE));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBreak(world, pos, state, player);

        if (world.random.nextFloat() < 0.2F)
            world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 12.0F, Explosion.DestructionType.DESTROY);

    }
}
