package svenhjol.charm.feature.doors_open_together.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import svenhjol.charm.feature.doors_open_together.DoorsOpenTogether;
import svenhjol.charm.charmony.feature.FeatureHolder;

import java.util.ArrayList;
import java.util.List;

public final class Handlers extends FeatureHolder<DoorsOpenTogether> {
    public final List<BlockPos> neighbours = new ArrayList<>();

    public Handlers(DoorsOpenTogether feature) {
        super(feature);
    }

    public void tryOpenNeighbour(Level level, BlockState state, BlockPos pos, boolean isClosed) {
        var facing = state.getValue(DoorBlock.FACING);
        var hinge = state.getValue(DoorBlock.HINGE);

        var neighborPos = switch (facing) {
            case NORTH -> hinge == DoorHingeSide.LEFT ? pos.east() : pos.west();
            case SOUTH -> hinge == DoorHingeSide.LEFT ? pos.west() : pos.east();
            case WEST -> hinge == DoorHingeSide.LEFT ? pos.north() : pos.south();
            case EAST -> hinge == DoorHingeSide.LEFT ? pos.south() : pos.north();
            default -> null;
        };

        if (neighborPos == null) return;

        var neighborState = level.getBlockState(neighborPos);
        if (!(neighborState.getBlock() instanceof DoorBlock neighbor)) return;

        var neighborPowered = neighborState.getValue(DoorBlock.POWERED);
        if (neighborPowered) return;

        var neighborFacing = neighborState.getValue(DoorBlock.FACING);
        if (neighborFacing != facing) return;

        var neighborHinge = neighborState.getValue(DoorBlock.HINGE);
        if (neighborHinge == hinge) return;

        neighbours.add(neighborPos);
        neighbor.setOpen(null, level, neighborState, neighborPos, isClosed);
        neighbours.remove(neighborPos);
    }
}
