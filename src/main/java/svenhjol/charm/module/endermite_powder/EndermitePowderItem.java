package svenhjol.charm.module.endermite_powder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import svenhjol.charm.helper.DimensionHelper;
import svenhjol.charm.init.CharmTags;
import svenhjol.charm.item.CharmItem;
import svenhjol.charm.loader.CharmModule;

public class EndermitePowderItem extends CharmItem {
    public EndermitePowderItem(CharmModule module) {
        super(module, "endermite_powder", new Properties().tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!DimensionHelper.isEnd(level)) {
            return InteractionResultHolder.fail(stack);
        }

        if (!player.isCreative()) {
            stack.shrink(1);
        }

        int x = player.blockPosition().getX();
        int y = player.blockPosition().getY();
        int z = player.blockPosition().getZ();

        player.getCooldowns().addCooldown(this, 40);

        // client
        if (level.isClientSide) {
            player.swing(hand);
            level.playSound(player, x, y, z, EndermitePowder.LAUNCH_SOUND, SoundSource.PLAYERS, 0.4F, 0.9F + level.random.nextFloat() * 0.2F);
        }

        // server
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel)level;
            BlockPos pos = serverLevel.findNearestMapFeature(CharmTags.ENDERMITE_POWDER_LOCATED, player.blockPosition(), 1500, false);
            if (pos != null) {
                EndermitePowderEntity entity = new EndermitePowderEntity(level, pos.getX(), pos.getZ());
                Vec3 look = player.getLookAngle();

                entity.setPosRaw(x + look.x * 2, y + 0.5, z + look.z * 2);
                level.addFreshEntity(entity);

                EndermitePowder.triggerAdvancement((ServerPlayer) player);
                return InteractionResultHolder.pass(stack);
            }
        }

        return InteractionResultHolder.success(stack);
    }
}
