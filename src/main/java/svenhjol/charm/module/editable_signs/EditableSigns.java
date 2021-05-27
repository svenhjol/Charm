package svenhjol.charm.module.editable_signs;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;

@Module(mod = Charm.MOD_ID, description = "Right-click on a sign with an empty hand to edit its text.")
public class EditableSigns extends CharmModule {
    public static final Identifier TRIGGER_EDITED_SIGN = new Identifier(Charm.MOD_ID, "edited_sign");

    @Override
    public void init() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
    }

    private ActionResult handleUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (!world.isClient && player != null && player.getStackInHand(hand).isEmpty()) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof AbstractSignBlock) {
                player.openEditSignScreen((SignBlockEntity)world.getBlockEntity(pos));
                triggerEditedSign((ServerPlayerEntity) player);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    public static void triggerEditedSign(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_EDITED_SIGN);
    }
}
