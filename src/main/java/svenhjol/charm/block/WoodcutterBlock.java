package svenhjol.charm.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StonecutterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.screenhandler.WoodcutterScreenHandler;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.IMesonBlock;

import javax.annotation.Nullable;

public class WoodcutterBlock extends StonecutterBlock implements IMesonBlock {
    private MesonModule module;
    private static final Text TITLE = new TranslatableText("container.charm.woodcutter");

    public WoodcutterBlock(MesonModule module) {
        super(AbstractBlock.Settings.copy(Blocks.STONECUTTER));
        register(module, "woodcutter");
        this.module = module;
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(state.createScreenHandlerFactory(world, pos));
            return ActionResult.CONSUME;
        }
    }

    @Nullable
    public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
        return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity)
            -> new WoodcutterScreenHandler(i, playerInventory, ScreenHandlerContext.create(world, pos)), TITLE);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
