package svenhjol.meson.decorator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public abstract class MesonOuterDecorator
{
    protected World world;
    protected BlockPos pos;
    protected Random rand;
    protected List<ChunkPos> chunks;

    public MesonOuterDecorator(World world, BlockPos pos, Random rand, List<ChunkPos> chunks)
    {
        this.world = world;
        this.pos = pos;
        this.rand = rand;
        this.chunks = chunks;
    }

    public abstract void generate();
}
