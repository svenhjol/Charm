package svenhjol.charm.feature.storage_blocks.gunpowder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.base.CharmonyBlockItem;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.PlayerHelper;

@SuppressWarnings({"deprecation", "BooleanMethodIsAlwaysInverted", "unused"})
public class GunpowderBlock extends FallingBlock {
    public static final ResourceLocation TRIGGER_DISSOLVED_GUNPOWDER = Charm.instance().makeId("dissolved_gunpowder");

    public GunpowderBlock() {
        super(Properties.of()
            .sound(SoundType.SAND)
            .strength(0.5f));
    }

    public static CharmonyFeature getParent() {
        return Charm.instance().loader().get(StorageBlocks.class).orElseThrow();
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!tryTouchLava(level, pos, state)) {
            super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!tryTouchLava(level, pos, state)) {
            super.onPlace(state, level, pos, oldState, isMoving);
        }
    }

    protected boolean tryTouchLava(Level level, BlockPos pos, BlockState state) {
        var lavaBelow = false;

        for (var facing : Direction.values()) {
            if (facing != Direction.DOWN) {
                var below = pos.relative(facing);
                if (level.getBlockState(below).is(Blocks.LAVA)) {
                    lavaBelow = true;
                    break;
                }
            }
        }

        if (lavaBelow) {
            level.globalLevelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
            level.removeBlock(pos, true);
            level.playSound(null, pos, Gunpowder.dissolveSound.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        if (!level.isClientSide()) {
            triggerAdvancementForNearbyPlayers((ServerLevel)level, pos);
        }

        return lavaBelow;
    }

    static class BlockItem extends CharmonyBlockItem {
        public BlockItem() {
            super(getParent(), Gunpowder.block, new Properties());
        }
    }

    static void triggerAdvancementForNearbyPlayers(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(player
            -> Advancements.trigger(TRIGGER_DISSOLVED_GUNPOWDER, (ServerPlayer)player));
    }
}
