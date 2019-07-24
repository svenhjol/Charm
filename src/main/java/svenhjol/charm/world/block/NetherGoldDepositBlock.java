package svenhjol.charm.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.world.feature.NetherGoldDeposits;
import svenhjol.meson.MesonBlock;

import java.util.Random;

public class NetherGoldDepositBlock extends MesonBlock
{
    public NetherGoldDepositBlock()
    {
        super("nether_gold_deposit", Block.Properties
            .create(Material.ROCK)
            .sound(SoundType.STONE)
            .hardnessAndResistance(NetherGoldDeposits.hardness, NetherGoldDeposits.resistance)
        );
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch)
    {
        /* @todo Nether gold experience isn't working */
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        return rand.nextInt(2);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}
