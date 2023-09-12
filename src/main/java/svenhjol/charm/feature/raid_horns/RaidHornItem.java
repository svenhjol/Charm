package svenhjol.charm.feature.raid_horns;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.base.CharmItem;

import java.util.function.Supplier;

public class RaidHornItem extends CharmItem {
    private static final float RANGE = 256.0F; // Matches goat horns.
    private static final int DURATION = 140; // Matches goat horns.

    public RaidHornItem(CharmFeature feature) {
        super(feature, new Properties()
            .durability(RaidHorns.DURABILITY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var held = player.getItemInHand(hand);
        var range = RANGE / 16.0F;
        var pos = player.blockPosition();
        Supplier<SoundEvent> sound = null;

        if (!level.isClientSide()) {
            var serverLevel = (ServerLevel)level;

            if (serverLevel.isRaided(pos)) {
                // Try and call off the raid.
                var raid = serverLevel.getRaidAt(pos);
                if (raid != null) {
                    raid.stop();
                    sound = RaidHorns.callOffRaidSound;
                    // TODO: advancement.
                }
            } else {
                // Try and summon pillagers.
                var result = RaidHorns.trySpawnPillagers(serverLevel, pos);
                if (result) {
                    sound = RaidHorns.callPatrolSound;
                    // TODO: advancement.
                }
            }

            if (sound == null) {
                sound = RaidHorns.failSound;
            }

            player.startUsingItem(hand);
            level.playSound(null, player, sound.get(), SoundSource.RECORDS, range, 1.0F);
            player.getCooldowns().addCooldown(this, DURATION);

            if (!player.getAbilities().instabuild) {
                held.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }

        return InteractionResultHolder.consume(held);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return DURATION;
    }
}
