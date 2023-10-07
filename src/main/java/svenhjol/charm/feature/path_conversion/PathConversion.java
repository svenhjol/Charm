package svenhjol.charm.feature.path_conversion;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony_api.event.BlockUseEvent;

import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID)
public class PathConversion extends CharmonyFeature {
    static Supplier<SoundEvent> dirtToPathSound;
    static Supplier<SoundEvent> pathToDirtSound;

    @Configurable(
        name = "Convert dirt to path",
        description = "If true, a shovel can be used to convert a dirt block to a path block."
    )
    public static boolean dirtToPath = true;

    @Configurable(
        name = "Convert path to dirt",
        description = "If true, a hoe can be used to convert a path block to a dirt block."
    )
    public static boolean pathToDirt = true;

    @Override
    public void register() {
        var registry = Charm.instance().registry();
        dirtToPathSound = registry.soundEvent("dirt_to_path");
        pathToDirtSound = registry.soundEvent("path_to_dirt");
    }

    @Override
    public void runWhenEnabled() {
        BlockUseEvent.INSTANCE.handle(this::handleBlockUse);
    }

    private InteractionResult handleBlockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var pos = hitResult.getBlockPos();
        var stack = player.getItemInHand(hand);
        var state = level.getBlockState(pos);
        var success = false;

        if (pathToDirt && stack.getItem() instanceof HoeItem && state.is(Blocks.DIRT_PATH)) {
            player.swing(hand);

            if (!level.isClientSide) {
                level.setBlock(pos, Blocks.DIRT.defaultBlockState(), 11);
                level.playSound(null, pos, pathToDirtSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            success = true;


        } else if (dirtToPath && stack.getItem() instanceof ShovelItem && state.is(Blocks.DIRT)) {
            player.swing(hand);

            if (!level.isClientSide) {
                level.setBlock(pos, Blocks.DIRT_PATH.defaultBlockState(), 11);
                level.playSound(null, pos, dirtToPathSound.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            success = true;
        }

        if (success) {
            if (!player.getAbilities().instabuild) {
                stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }
}
