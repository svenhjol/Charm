package svenhjol.charm.module.open_double_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Automatically opens double doors.")
public class OpenDoubleDoors extends CharmModule {

    public static void tryOpenNeighbour(Level level, BlockState state, BlockPos pos, boolean isClosed) {
        Direction facing = state.getValue(DoorBlock.FACING);
        DoorHingeSide hinge = state.getValue(DoorBlock.HINGE);

        BlockPos neighborPos = switch (facing) {
            case NORTH -> hinge == DoorHingeSide.LEFT ? pos.east() : pos.west();
            case SOUTH -> hinge == DoorHingeSide.LEFT ? pos.west() : pos.east();
            case WEST -> hinge == DoorHingeSide.LEFT ? pos.north() : pos.south();
            case EAST -> hinge == DoorHingeSide.LEFT ? pos.south() : pos.north();
            default -> null;
        };

        if (neighborPos == null)
            return;

        BlockState neighborState = level.getBlockState(neighborPos);

        if (!(neighborState.getBlock() instanceof DoorBlock neighbor))
            return;

        Boolean neighborPowered = neighborState.getValue(DoorBlock.POWERED);
        if (neighborPowered)
            return;

        Direction neighborFacing = neighborState.getValue(DoorBlock.FACING);
        if (neighborFacing != facing)
            return;

        DoorHingeSide neighborHinge = neighborState.getValue(DoorBlock.HINGE);

        if (neighborHinge == hinge)
            return;

        neighbor.setOpen(null, level, neighborState, neighborPos, isClosed);
    }
}
