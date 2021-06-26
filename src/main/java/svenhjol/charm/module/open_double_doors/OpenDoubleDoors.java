package svenhjol.charm.module.open_double_doors;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.CharmModule;

import java.util.function.Function;

import static net.minecraft.world.level.block.state.properties.DoorHingeSide.LEFT;

@Module(mod = Charm.MOD_ID, description = "Automatically opens double doors.")
public class OpenDoubleDoors extends CharmModule {
    @Override
    public void init() {
        UseBlockCallback.EVENT.register(this::handleOpenDoor);
    }

    private InteractionResult handleOpenDoor(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Function<DoorBlock, InteractionResult> use =
            d -> d.use(state, level, pos, player, hand, hitResult);

        if (!DoorBlock.isWoodenDoor(state)) {
            Charm.LOG.debug("Door is not a wooden door");
            return InteractionResult.PASS;
        }

        DoorBlock door = (DoorBlock) state.getBlock();

        boolean isClosed = !door.isOpen(state);
        Direction facing = state.getValue(DoorBlock.FACING);
        DoorHingeSide hinge = state.getValue(DoorBlock.HINGE);

        BlockPos neighborPos = switch (facing) {
            case NORTH -> hinge == LEFT ? pos.east() : pos.west();
            case SOUTH -> hinge == LEFT ? pos.west() : pos.east();
            case WEST -> hinge == LEFT ? pos.north() : pos.south();
            case EAST -> hinge == LEFT ? pos.south() : pos.north();
            default -> null;
        };

        if (neighborPos == null) {
            Charm.LOG.debug("Neighbor blockpos not found");
            return use.apply(door);
        }

        BlockState neighborState = level.getBlockState(neighborPos);
        if (!DoorBlock.isWoodenDoor(neighborState)) {
            Charm.LOG.debug("Neighbor door is not a wooden door");
            return use.apply(door);
        }

        Direction neighborFacing = neighborState.getValue(DoorBlock.FACING);
        if (neighborFacing != facing) {
            Charm.LOG.debug("Neighbor door does not face the same way");
            return use.apply(door);
        }

        DoorBlock neighbor = (DoorBlock) neighborState.getBlock();
        DoorHingeSide neighborHinge = neighborState.getValue(DoorBlock.HINGE);

        if (neighborHinge == hinge) {
            Charm.LOG.debug("Neighbor hinge is not opposite");
            return use.apply(door);
        }

        use.apply(door);
        neighbor.setOpen(player, level, neighborState, neighborPos, isClosed);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
