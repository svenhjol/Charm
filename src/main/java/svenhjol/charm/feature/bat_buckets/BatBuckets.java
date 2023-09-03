package svenhjol.charm.feature.bat_buckets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm_api.CharmApi;
import svenhjol.charm_api.event.EntityUseEvent;
import svenhjol.charm_api.iface.IProvidesWandererTrades;
import svenhjol.charm_api.iface.IWandererTrade;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.ItemNbtHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Right-click a bat with a bucket to capture it. Right-click again to release it and locate entities around you.")
public class BatBuckets extends CharmFeature implements IProvidesWandererTrades {
    static Supplier<BatBucketItem> BAT_BUCKET_ITEM;
    private static Supplier<SoundEvent> GRAB_SOUND;
    private static Supplier<SoundEvent> RELEASE_SOUND;
    static final int GLOW_TIME = 10; // In seconds.

    @Override
    public void register() {
        var registry = Charm.instance().registry();
        BAT_BUCKET_ITEM = registry.item("bat_bucket", () -> new BatBucketItem(this));

        GRAB_SOUND = registry.soundEvent("bat_bucket_grab");
        RELEASE_SOUND = registry.soundEvent("bat_bucket_release");

        CharmApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        EntityUseEvent.INSTANCE.handle(this::handleEntityUse);
    }

    static void playGrabSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, GRAB_SOUND.get(), SoundSource.PLAYERS, 0.6F, 0.95F + level.getRandom().nextFloat() * 0.2F);
    }

    static void playReleaseSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, RELEASE_SOUND.get(), SoundSource.PLAYERS, 0.6F, 0.95F + level.getRandom().nextFloat() * 0.2F);
    }

    static void playLaunchSound(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.BAT_TAKEOFF, SoundSource.PLAYERS, 0.25F, 1F);
    }

    private InteractionResult handleEntityUse(Player player, Level level, InteractionHand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (!entity.level().isClientSide()
            && entity instanceof Bat bat
            && bat.getHealth() > 0
        ) {
            var held = player.getItemInHand(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET) {
                return InteractionResult.PASS;
            }

            var batBucket = new ItemStack(BAT_BUCKET_ITEM.get());
            var tag = new CompoundTag();
            ItemNbtHelper.setCompound(batBucket, BatBucketItem.STORED_BAT_TAG, bat.saveWithoutId(tag));

            if (held.getCount() == 1) {
                player.setItemInHand(hand, batBucket);
            } else {
                held.shrink(1);
                player.getInventory().placeItemBackInInventory(batBucket);
            }

            playGrabSound((ServerLevel)bat.level(), bat.blockPosition());
            player.getCooldowns().addCooldown(BAT_BUCKET_ITEM.get(), 30);
            player.swing(hand);
            entity.discard();

            // TODO: advancement.
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return BAT_BUCKET_ITEM.get();
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

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return List.of();
    }
}
