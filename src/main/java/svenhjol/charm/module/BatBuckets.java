package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ItemNBTHelper;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.item.BatBucketItem;

@Module(mod = Charm.MOD_ID, description = "Right-click a bat with a bucket to capture it. Right-click again to release it and locate entities around you.")
public class BatBuckets extends CharmModule {
    public static final Identifier TRIGGER_USED_BAT_BUCKET = new Identifier(Charm.MOD_ID, "used_bat_bucket");
    public static BatBucketItem BAT_BUCKET_ITEM;

    @Config(name = "Glowing time", description = "Number of seconds that entities will receive the glowing effect.")
    public static int glowingTime = 10;

    @Config(name = "Viewing range", description = "Range (in blocks) in which entities will glow.")
    public static int glowingRange = 24;

    @Config(name = "Damage bat", description = "If true, the bat will take half a heart of damage when released from the bucket.")
    public static boolean damageBat = true;

    @Override
    public void register() {
        BAT_BUCKET_ITEM = new BatBucketItem(this);
    }

    @Override
    public void init() {
        UseEntityCallback.EVENT.register(this::tryCapture);
    }

    private ActionResult tryCapture(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (!entity.world.isClient
            && entity instanceof BatEntity
            && ((BatEntity)entity).getHealth() > 0
        ) {
            BatEntity bat = (BatEntity)entity;
            ItemStack held = player.getStackInHand(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET)
                return ActionResult.PASS;

            ItemStack batBucket = new ItemStack(BAT_BUCKET_ITEM);
            NbtCompound nbt = new NbtCompound();
            ItemNBTHelper.setCompound(batBucket, BatBucketItem.STORED_BAT_NBT, bat.writeNbt(nbt));

            if (held.getCount() == 1) {
                player.setStackInHand(hand, batBucket);
            } else {
                held.decrement(1);
                PlayerHelper.addOrDropStack(player, batBucket);
            }

            player.getItemCooldownManager().set(BatBuckets.BAT_BUCKET_ITEM, 20);
            player.swingHand(hand);
            entity.discard();
            return ActionResult.CONSUME;
        }

        return ActionResult.PASS;
    }

    public static void triggerUsedBatBucket(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, BatBuckets.TRIGGER_USED_BAT_BUCKET);
    }
}
