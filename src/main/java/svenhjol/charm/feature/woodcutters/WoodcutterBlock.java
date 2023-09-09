package svenhjol.charm.feature.woodcutters;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charmony.base.CharmBlockItem;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.TextHelper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class WoodcutterBlock extends StonecutterBlock {
    private static final Component TITLE = TextHelper.translatable("container.charm.woodcutter");
    private final CharmFeature feature;

    public WoodcutterBlock(CharmFeature feature) {
        super(Properties.of().strength(3.5F));
        this.feature = feature;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!feature.isEnabled()) {
            return InteractionResult.FAIL;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(level, pos));
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((i, playerInventory, player)
            -> new WoodcutterMenu(i, playerInventory, ContainerLevelAccess.create(level, pos)), TITLE);
    }

    public static class BlockItem extends CharmBlockItem {
        public BlockItem(CharmFeature feature, Supplier<WoodcutterBlock> block) {
            super(feature, block, new Properties());
        }
    }
}
