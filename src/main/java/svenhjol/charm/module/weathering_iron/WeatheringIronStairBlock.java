package svenhjol.charm.module.weathering_iron;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.block.CharmStairBlock;
import svenhjol.charm.loader.CharmModule;

import java.util.Random;

public class WeatheringIronStairBlock extends CharmStairBlock implements IWeatherableIron {
    private final boolean fullyOxidised;

    public WeatheringIronStairBlock(CharmModule module, String name, Block block) {
        this(module, name, block, false);
    }

    public WeatheringIronStairBlock(CharmModule module, String name, Block block, boolean fullyOxidised) {
        super(module, name, block);
        this.fullyOxidised = fullyOxidised;
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return !isFullyOxidised();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        WeatheringIron.handleRandomTick(level, pos, state, random);
    }

    @Override
    public boolean isFullyOxidised() {
        return fullyOxidised;
    }
}
