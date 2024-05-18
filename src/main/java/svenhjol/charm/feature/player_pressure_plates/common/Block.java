package svenhjol.charm.feature.player_pressure_plates.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class Block extends PressurePlateBlock {
    public Block() {
        super(BlockSetType.STONE, Properties.of()
            .requiresCorrectToolForDrops()
            .noCollission()
            .mapColor(MapColor.COLOR_BLACK)
            .strength(2F, 1200F));
    }

    @Override
    protected int getSignalStrength(@Nonnull Level level, @Nonnull BlockPos pos) {
        return PressurePlateBlock.getEntityCount(level, TOUCH_AABB.move(pos), Player.class) > 0 ? 15 : 0;
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Supplier<Block> block) {
            super(block.get(), new Properties());
        }
    }
}
