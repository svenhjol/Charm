package svenhjol.charm.module.coral_squids;

import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ItemNBTHelper;
import svenhjol.charm.helper.MobHelper;
import svenhjol.charm.item.ICharmItem;

import javax.annotation.Nullable;

public class CoralSquidBucketItem extends BucketItem implements ICharmItem {
    public static final String STORED_CORAL_SQUID = "stored_coral_squid";

    private CharmModule module;

    public CoralSquidBucketItem(CharmModule module) {
        super(Fluids.WATER, new Item.Settings()
            .group(ItemGroup.MISC)
            .maxCount(1));

        this.module = module;
        register(module, "coral_squid_bucket");
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }

    @Override
    public void onEmptied(@Nullable PlayerEntity playerEntity, World world, ItemStack stack, BlockPos pos) {
        if (world instanceof ServerWorld) {
            CoralSquidEntity coralSquid = MobHelper.spawn(CoralSquids.CORAL_SQUID, (ServerWorld) world, pos, SpawnReason.BUCKET);
            if (coralSquid != null) {
                NbtCompound data = ItemNBTHelper.getCompound(stack, STORED_CORAL_SQUID);
                if (!data.isEmpty())
                    coralSquid.readCustomDataFromNbt(data);

                if (stack.hasCustomName())
                    coralSquid.setCustomName(stack.getName());

                world.spawnEntity(coralSquid);
            }
        }
    }

    @Override
    protected void playEmptyingSound(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos) {
        world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F, 1.0F);
    }
}
