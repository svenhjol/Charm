package svenhjol.charm.module.editable_signs;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.init.CharmAdvancements;

@CommonModule(mod = Charm.MOD_ID, description = "Right-click on a sign with an empty hand to edit its text.")
public class EditableSigns extends svenhjol.charm.loader.CommonModule {
    public static final ResourceLocation TRIGGER_EDITED_SIGN = new ResourceLocation(Charm.MOD_ID, "edited_sign");

    @Override
    public void run() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
    }

    private InteractionResult handleUseBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (!world.isClientSide && player != null && player.getItemInHand(hand).isEmpty()) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof SignBlock) {
                player.openTextEdit((SignBlockEntity)world.getBlockEntity(pos));
                triggerEditedSign((ServerPlayer) player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    public static void triggerEditedSign(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_EDITED_SIGN);
    }
}
