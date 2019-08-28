package vazkii.quark.api;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ICollateralMover {

	default boolean isCollateralMover(World world, BlockPos source, Direction moveDirection, BlockPos pos) {
		return true;
	}
	
	MoveResult getCollateralMovement(World world, BlockPos source, Direction moveDirection, Direction side, BlockPos pos); 
	
	public static enum MoveResult {
		
		MOVE,
		BREAK,
		SKIP,
		PREVENT
		
	}
	
	
}
