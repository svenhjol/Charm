package svenhjol.charm.feature.open_both_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

@Feature(mod = Charm.MOD_ID, description = "Automatically opens double doors.")
public class OpenBothDoors extends CharmFeature {
    public static void tryOpenNeighbour(Level level, BlockState state, BlockPos pos, boolean isClosed) {
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

        neighbor.setOpen(null, level, neighborState, neighborPos, isClosed);
    }
}
