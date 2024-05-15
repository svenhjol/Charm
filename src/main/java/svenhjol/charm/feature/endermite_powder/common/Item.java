package svenhjol.charm.feature.endermite_powder.common;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.endermite_powder.EndermitePowder;
import svenhjol.charm.foundation.Tags;
import svenhjol.charm.foundation.item.CharmItem;

public class Item extends CharmItem<EndermitePowder> {
    public Item() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);

        // Only available in the End.
        if (level.dimension() != Level.END) {
            return InteractionResultHolder.fail(stack);
        }

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        var x = player.blockPosition().getX();
        var y = player.blockPosition().getY();
        var z = player.blockPosition().getZ();
        player.getCooldowns().addCooldown(this, 40);


        if (level.isClientSide()) {
            player.swing(hand);
            level.playSound(player, x, y, z, feature().registers.launchSound.get(), SoundSource.PLAYERS, 0.4F, 0.9F + level.random.nextFloat() * 0.2F);
        } else {
            var serverLevel = (ServerLevel)level;
            var serverPlayer = (ServerPlayer)player;
            var pos = serverLevel.findNearestMapStructure(Tags.ENDERMITE_POWDER_LOCATED, player.blockPosition(), 1500, false);
            if (pos != null) {
                var entity = new Entity(level, pos.getX(), pos.getZ());
                var look = player.getLookAngle();
                entity.setPosRaw(x + look.x * 2, y + 0.5, z + look.z * 2);
                level.addFreshEntity(entity);

                feature().advancements.usedEndermitePowder(serverPlayer);
                return InteractionResultHolder.pass(stack);
            }
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public Class<EndermitePowder> typeForFeature() {
        return EndermitePowder.class;
    }
}
