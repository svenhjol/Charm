package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Right-click on a sign to edit its text.")
public class EditableSigns extends CharmModule {
    @Override
    public void init() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
    }

    private ActionResult handleUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (!world.isClient && player != null) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof AbstractSignBlock) {
                player.openEditSignScreen((SignBlockEntity)world.getBlockEntity(pos));
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }
}
