package svenhjol.charm.feature.coral_squids;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.helper.ItemNbtHelper;
import svenhjol.charmony.helper.MobHelper;

import javax.annotation.Nullable;

public class CoralSquidBucketItem extends BucketItem {
    public static final String STORED_CORAL_SQUID = "stored_coral_squid";

    public CoralSquidBucketItem(CharmonyFeature feature) {
        super(Fluids.WATER, new Properties()
            .stacksTo(1));
    }

    @Override
    public void checkExtraContent(@Nullable Player playerEntity, Level level, ItemStack stack, BlockPos pos) {
        if (level instanceof ServerLevel) {
            MobHelper.spawn(CoralSquids.entity.get(), (ServerLevel) level, pos, MobSpawnType.BUCKET, coralSquid -> {
                var data = ItemNbtHelper.getCompound(stack, STORED_CORAL_SQUID);
                if (!data.isEmpty()) {
                    coralSquid.readAdditionalSaveData(data);
                }

                if (stack.hasCustomHoverName()) {
                    coralSquid.setCustomName(stack.getHoverName());
                }
            });
        }
    }

    @Override
    protected void playEmptySound(@Nullable Player player, LevelAccessor level, BlockPos pos) {
        level.playSound(player, pos, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }
}
