package svenhjol.charm.module.weathering_iron;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@CommonModule(mod = Charm.MOD_ID, description = "Iron rusts when in contact with water.")
public class WeatheringIron extends CharmModule {
    public static WeatheringIronBlock EXPOSED_IRON;
    public static WeatheringIronBlock WEATHERED_IRON;
    public static WeatheringIronBlock OXIDIZED_IRON;

    public static List<Block> WEATHERING_ORDER = new ArrayList<>();

    @Config(name = "Faces increase weathering", description = "The chance of weathering increases according to the number of block faces touching water.")
    public static boolean facesIncreaseWeathering = true;

    @Config(name = "Face multiplier", description = "When more than one block face is touching water, weathering chance increases by this amount per face.")
    public static double faceMultiplier = 0.008D;

    @Config(name = "Weathering chance", description = "Chance (out of 1.0) of a block being considered for weathering.")
    public static double chance = 0.006D;

    @Config(name = "Bubble column chance multiplier", description = "When a block is above a bubble column, weathering chance is multiplied by this value.")
    public static double bubbleColumnMultiplier = 4.0D;

    @Override
    public void register() {
        EXPOSED_IRON = new WeatheringIronBlock(this, "exposed_iron");
        WEATHERED_IRON = new WeatheringIronBlock(this, "weathered_iron");
        OXIDIZED_IRON = new WeatheringIronBlock(this, "oxidized_iron");

        WEATHERING_ORDER.add(Blocks.IRON_BLOCK);
        WEATHERING_ORDER.add(EXPOSED_IRON);
        WEATHERING_ORDER.add(WEATHERED_IRON);
        WEATHERING_ORDER.add(OXIDIZED_IRON);
    }

    @Override
    public void runWhenEnabled() {
        super.runWhenEnabled();
    }

    /**
     * @see ChangeOverTimeBlock#onRandomTick
     */
    public static void handleRandomTick(ServerLevel level, BlockPos pos, BlockState state, Random random) {
        var d = random.nextDouble();

        if (WEATHERING_ORDER.contains(state.getBlock())) {
            var chance = multiplyByTouchingFaces(level, pos);
            if (chance == 0) return; // no point continuing if no faces touching

            if (hasBubbleColumn(level, pos)) {
                chance *= bubbleColumnMultiplier;
            }

            if (d <= chance) {
                var i = WEATHERING_ORDER.indexOf(state.getBlock());
                if (i < WEATHERING_ORDER.size() - 1) {
                    var nextBlock = WEATHERING_ORDER.get(i + 1);
                    LogHelper.debug(WeatheringIron.class, "Weathering block to " + nextBlock + ", d = " + d + ", chance = " + chance, ", pos = " + pos);
                    level.setBlockAndUpdate(pos, nextBlock.defaultBlockState());
                }
            }
        }
    }

    public static int facesTouchingWater(ServerLevel level, BlockPos pos) {
        int faces = 0;

        for (Direction direction : Direction.values()) {
            if (level.isWaterAt(pos.relative(direction))) {
                faces++;
            }
        }

        return faces;
    }

    public static double multiplyByTouchingFaces(ServerLevel level, BlockPos pos) {
        int faces = facesTouchingWater(level, pos);

        if (facesIncreaseWeathering && faces > 1) {
            double newChance = chance + (faceMultiplier * faces);
            LogHelper.debug(WeatheringIron.class, "Face weathering, faces = " + faces + ", chance now = " + newChance + ", pos = " + pos);
            return newChance;
        } else if (faces > 0) {
            return chance;
        }

        return 0;
    }

    public static boolean hasBubbleColumn(ServerLevel level, BlockPos pos) {
        boolean has = level.getBlockState(pos.below()).getBlock() == Blocks.BUBBLE_COLUMN;
        if (has) {
            LogHelper.debug(WeatheringIron.class, "Bubble column weathering, pos = " + pos);
        }
        return has;
    }
}
