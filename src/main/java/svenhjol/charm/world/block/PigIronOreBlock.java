package svenhjol.charm.world.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

import java.util.Random;

public class PigIronOreBlock extends MesonBlock {
    public PigIronOreBlock(MesonModule module) {
        super(module, "pig_iron_ore", Properties.from(Blocks.NETHER_QUARTZ_ORE));
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        if (silktouch > 0) return 0;
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        return rand.nextInt(2);
    }
}