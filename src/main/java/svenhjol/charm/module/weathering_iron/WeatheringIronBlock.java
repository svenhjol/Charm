package svenhjol.charm.module.weathering_iron;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.loader.CharmModule;

import java.util.Random;

public class WeatheringIronBlock extends CharmBlock {
    public WeatheringIronBlock(CharmModule module, String name) {
        super(module, name, Properties.copy(Blocks.IRON_BLOCK));
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return this != WeatheringIron.OXIDIZED_IRON;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        WeatheringIron.handleRandomTick(level, pos, state, random);
    }
}
