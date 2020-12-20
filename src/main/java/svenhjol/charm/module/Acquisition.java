package svenhjol.charm.module;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.EnchantmentsHelper;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.enchantment.AcquisitionEnchantment;
import svenhjol.charm.event.PlayerBreakBlockAfterBreak;

import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Tools with the Acquisition enchantment automatically pick up drops.")
public class Acquisition extends CharmModule {
    public static AcquisitionEnchantment ACQUISITION;

    @Override
    public void register() {
        ACQUISITION = new AcquisitionEnchantment(this);
    }

    @Override
    public void init() {
        PlayerBreakBlockAfterBreak.EVENT.register(this::tryBreakBlock);
    }

    private ActionResult tryBreakBlock(ServerWorld world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        ItemStack held = player.getMainHandStack();

        if (!EnchantmentsHelper.has(held, ACQUISITION))
            return ActionResult.PASS;

        Block block = state.getBlock();

        // do the normal afterBreak stuff
        player.incrementStat(Stats.MINED.getOrCreateStat(block));
        player.addExhaustion(0.005F);

        List<ItemStack> dropped = Block.getDroppedStacks(state, world, pos, blockEntity, player, held);
        dropped.forEach(drop -> {
            PlayerHelper.addOrDropStack(player, drop);
        });

        state.onStacksDropped(world, pos, held);

        return ActionResult.SUCCESS;
    }
}
