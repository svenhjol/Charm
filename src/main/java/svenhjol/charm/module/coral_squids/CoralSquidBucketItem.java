package svenhjol.charm.module.coral_squids;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluids;
import svenhjol.charm.helper.ItemNBTHelper;
import svenhjol.charm.helper.MobHelper;
import svenhjol.charm.item.ICharmItem;
import svenhjol.charm.loader.CharmCommonModule;

import javax.annotation.Nullable;

public class CoralSquidBucketItem extends BucketItem implements ICharmItem {
    public static final String STORED_CORAL_SQUID = "stored_coral_squid";

    private final CharmCommonModule module;

    public CoralSquidBucketItem(CharmCommonModule module) {
        super(Fluids.WATER, new Item.Properties()
            .tab(CreativeModeTab.TAB_MISC)
            .stacksTo(1));

        this.module = module;
        register(module, "coral_squid_bucket");
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }

    @Override
    public void checkExtraContent(@Nullable Player playerEntity, Level world, ItemStack stack, BlockPos pos) {
        if (world instanceof ServerLevel) {
            CoralSquidEntity coralSquid = MobHelper.spawn(CoralSquids.CORAL_SQUID, (ServerLevel) world, pos, MobSpawnType.BUCKET);
            if (coralSquid != null) {
                CompoundTag data = ItemNBTHelper.getCompound(stack, STORED_CORAL_SQUID);
                if (!data.isEmpty())
                    coralSquid.readAdditionalSaveData(data);

                if (stack.hasCustomHoverName())
                    coralSquid.setCustomName(stack.getHoverName());

                world.addFreshEntity(coralSquid);
            }
        }
    }

    @Override
    protected void playEmptySound(@Nullable Player player, LevelAccessor world, BlockPos pos) {
        world.playSound(player, pos, SoundEvents.BUCKET_EMPTY_FISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }
}
