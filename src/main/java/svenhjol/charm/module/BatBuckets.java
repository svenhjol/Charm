package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.client.BatBucketClient;
import svenhjol.charm.item.BatBucketItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(description = "Right-click a bat with a bucket to capture it. Right-click again to release it and locate entities around you.")
public class BatBuckets extends MesonModule {
    public static BatBucketItem BAT_BUCKET_ITEM;
    public static BatBucketClient client = null;

    public static final Identifier MSG_CLIENT_SET_GLOWING = new Identifier(Charm.MOD_ID, "client_set_glowing");

    @Config(name = "Glowing time", description = "Number of seconds that entities will receive the glowing effect.")
    public static int glowingTime = 10;

    @Config(name = "Viewing range", description = "Range (in blocks) in which entities will glow.")
    public static int glowingRange = 24;

    @Override
    public void init() {
        BAT_BUCKET_ITEM = new BatBucketItem(this);
    }

    @Override
    public void afterInit() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            capture(entity, player, hand);
            return ActionResult.PASS;
        });
    }

    @Override
    public void afterInitClient() {
        client = new BatBucketClient(this);
    }

    private void capture(Entity entity, PlayerEntity player, Hand hand) {
        if (!entity.world.isClient
            && entity instanceof BatEntity
            && ((BatEntity)entity).getHealth() > 0
        ) {
            BatEntity bat = (BatEntity)entity;
            ItemStack held = player.getStackInHand(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET)
                return;

            ItemStack batBucket = new ItemStack(BAT_BUCKET_ITEM);
            CompoundTag tag = new CompoundTag();
            ItemNBTHelper.setCompound(batBucket, BatBucketItem.STORED_BAT, bat.toTag(tag));

            if (held.getCount() == 1) {
                player.setStackInHand(hand, batBucket);
            } else {
                held.decrement(1);
                PlayerHelper.addOrDropStack(player, batBucket);
            }

            player.swingHand(hand);
            entity.remove();
        }
    }
}
