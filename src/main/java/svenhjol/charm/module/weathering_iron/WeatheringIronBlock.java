package svenhjol.charm.module.weathering_iron;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.loader.CharmModule;

import java.util.Random;

public class WeatheringIronBlock extends CharmBlock implements IWeatherableIron {
    private boolean fullyOxidised;
    private boolean notOxidised;

    public WeatheringIronBlock(CharmModule module, String name) {
        super(module, name, Properties.copy(Blocks.IRON_BLOCK));
    }

    public WeatheringIronBlock fullyOxidised() {
        this.fullyOxidised = true;
        return this;
    }

    public WeatheringIronBlock noOxidisation() {
        this.notOxidised = true;
        return this;
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

    @Override
    public boolean hasAnyOxidation() {
        return !notOxidised;
    }
}
