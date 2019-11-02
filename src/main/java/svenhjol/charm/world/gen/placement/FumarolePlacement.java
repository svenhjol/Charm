package svenhjol.charm.world.gen.placement;

import com.google.common.collect.Lists;
import com.mojang.datafixers.Dynamic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.placement.FrequencyConfig;
import net.minecraft.world.gen.placement.SimplePlacement;

import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Stream;

public class FumarolePlacement extends SimplePlacement<FrequencyConfig>
{
    public FumarolePlacement(Function<Dynamic<?>, ? extends FrequencyConfig> config)
    {
        super(config);
    }

    @Override
    protected Stream<BlockPos> getPositions(Random rand, FrequencyConfig config, BlockPos pos)
    {
        List<BlockPos> locations = Lists.newArrayList();

        for(int i = 0; i < rand.nextInt(rand.nextInt(config.count) + 1) + 1; ++i) {
            int x = rand.nextInt(16);
            int y = rand.nextInt(120) + 4;
            int z = rand.nextInt(16);
            locations.add(pos.add(x, y, z));
        }

        return locations.stream();
    }
}
