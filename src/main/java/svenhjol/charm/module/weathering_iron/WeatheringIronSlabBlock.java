package svenhjol.charm.module.weathering_iron;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.block.CharmSlabBlock;
import svenhjol.charm.loader.CharmModule;

import java.util.Random;

public class WeatheringIronSlabBlock extends CharmSlabBlock implements IWeatherableIron {
    private boolean fullyOxidised;
    private boolean notOxidised;

    public WeatheringIronSlabBlock(CharmModule module, String name, Block block) {
        super(module, name, Properties.copy(block));
    }

    public WeatheringIronSlabBlock fullyOxidised() {
        this.fullyOxidised = true;
        return this;
    }

    public WeatheringIronSlabBlock noOxidisation() {
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
