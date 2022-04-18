package svenhjol.charm.module.weathering_iron;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.block.CharmStairBlock;
import svenhjol.charm.loader.CharmModule;

public class WeatheringIronStairBlock extends CharmStairBlock implements IWeatherableIron {
    private boolean fullyOxidised;
    private boolean notOxidised;

    public WeatheringIronStairBlock(CharmModule module, String name, Block block) {
        super(module, name, block);
    }

    public WeatheringIronStairBlock fullyOxidised() {
        this.fullyOxidised = true;
        return this;
    }

    public WeatheringIronStairBlock noOxidisation() {
        this.notOxidised = true;
        return this;
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return !isFullyOxidised();
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        WeatheringIron.handleRandomTick(level, pos, state, random);
    }

    @Override
    public boolean isFullyOxidised() {
        return fullyOxidised;
    }

    @Override
    public boolean hasAnyOxidation() {
        return !notOxidised;
    }
}
