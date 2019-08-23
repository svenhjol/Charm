package svenhjol.charm.crafting.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.crafting.module.BlockOfEnderPearls;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

import java.util.Random;

public class EnderPearlBlock extends MesonBlock
{
    public EnderPearlBlock(MesonModule module)
    {
        super(module, "ender_pearl_block", Block.Properties
            .create(Material.GLASS)
            .sound(SoundType.GLASS)
            .hardnessAndResistance(BlockOfEnderPearls.hardness)
        );
    }

    // copypasta from MyceliumBlock
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState state, World world, BlockPos pos, Random rand)
    {
        super.animateTick(state, world, pos, rand);

        if (rand.nextInt(10) == 0) {
            world.addParticle(ParticleTypes.PORTAL, (float)pos.getX() + rand.nextFloat(), (float)pos.getY() + 1.1F, (float)pos.getZ() + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
        }
    }
}
