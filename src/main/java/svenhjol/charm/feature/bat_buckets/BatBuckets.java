package svenhjol.charm.feature.bat_buckets;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.iface.IWandererTrade;
import svenhjol.charm.api.iface.IWandererTradeProvider;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class BatBuckets extends CommonFeature implements IWandererTradeProvider {
    static Supplier<BatBucketItem> bucketItem;
    static Supplier<SoundEvent> grabSound;
    static Supplier<SoundEvent> releaseSound;
    static final int GLOW_TIME = 10; // In seconds.

    @Override
    public String description() {
        return """
            Right-click a bat with a bucket to capture it.
            Right-click again to release it and locate entities around you.""";
    }
    @Override
    public Optional<Register<? extends Feature>> registration() {
        CharmApi.registerProvider(this);
        return Optional.of(new CommonRegistration(this));
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return bucketItem.get();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public int getCost() {
                return 8;
            }
        });
    }

    public static void triggerCapturedBat(Player player) {
        Advancements.trigger(Charm.id("captured_bat"), player);
    }

    public static void triggerUsedBatBucket(Player player) {
        Advancements.trigger(Charm.id("used_bat_bucket"), player);
    }

    public static void playGrabSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, BatBuckets.grabSound.get(), SoundSource.PLAYERS, 0.6f, 0.95f + level.getRandom().nextFloat() * 0.2f);
    }

    public static void playReleaseSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, BatBuckets.releaseSound.get(), SoundSource.PLAYERS, 0.6f, 0.95f + level.getRandom().nextFloat() * 0.2f);
    }

    public static void playLaunchSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BAT_TAKEOFF, SoundSource.PLAYERS, 0.25f, 1f);
    }
}
