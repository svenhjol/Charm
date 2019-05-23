package svenhjol.charm.loot.generator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import svenhjol.charm.crafting.block.BlockCrate;
import svenhjol.charm.crafting.feature.Crate;
import svenhjol.charm.loot.feature.AbandonedCrates;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonBlock;

import java.util.Random;

public class AbandonedCrateGenerator implements IWorldGenerator
{
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if (world.provider.getDimension() != 0) return;

        float f = random.nextFloat();
        if (f > AbandonedCrates.generateChance) return;

        int x = chunkX * 16 + 8;
        int z = chunkZ * 16 + 8;

        int upperLimit = AbandonedCrates.upperLimit;
        int lowerLimit = AbandonedCrates.lowerLimit;
        int range = upperLimit - lowerLimit;
        int spread = 10;
        int maxTries = AbandonedCrates.maxTries;

        for (int i = 0; i < maxTries; i++) {
            BlockPos pos = new BlockPos(x + random.nextInt(spread), lowerLimit + random.nextInt(range), z + random.nextInt(spread));
            if (!world.isAirBlock(pos)) continue;

            IBlockState state;
            do {
                pos = pos.offset(EnumFacing.DOWN);
                state = world.getBlockState(pos);
            } while(!state.isFullBlock() && pos.getY() > lowerLimit);

            if (state.isFullBlock()) {
                placeCrate(world, pos.up());
                break;
            }
        }
    }

    public void placeCrate(World world, BlockPos pos)
    {
        float f = world.rand.nextFloat();
        Crate.RARITY rarity = Crate.RARITY.COMMON;
        if (f <= AbandonedCrates.rareChance) {
            rarity = Crate.RARITY.RARE;
        } else if (f <= AbandonedCrates.valuableChance) {
            rarity = Crate.RARITY.VALUABLE;
        } else if (f <= AbandonedCrates.uncommonChance) {
            rarity = Crate.RARITY.UNCOMMON;
        }

        IBlockState state = Crate.crateSealed.getDefaultState().withProperty(BlockCrate.VARIANT, MesonBlock.WoodVariant.random());
        Crate.generateCrate(world, pos, Crate.getRandomCrateType(rarity), state);
        Meson.debug("Abandoned Crates: generated crate", pos);
    }
}
