package svenhjol.charm.module.bat_buckets;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.loader.CommonModule;
import svenhjol.charm.helper.ItemNBTHelper;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.init.CharmAdvancements;

@Module(mod = Charm.MOD_ID, description = "Right-click a bat with a bucket to capture it. Right-click again to release it and locate entities around you.")
public class BatBuckets extends CommonModule {
    public static final ResourceLocation TRIGGER_CAPTURED_BAT = new ResourceLocation(Charm.MOD_ID, "captured_bat");
    public static final ResourceLocation TRIGGER_USED_BAT_BUCKET = new ResourceLocation(Charm.MOD_ID, "used_bat_bucket");
    public static svenhjol.charm.module.bat_buckets.BatBucketItem BAT_BUCKET_ITEM;

    @Config(name = "Glowing time", description = "Number of seconds that entities will receive the glowing effect.")
    public static int glowingTime = 10;

    @Config(name = "Viewing range", description = "Range (in blocks) in which entities will glow.")
    public static int glowingRange = 24;

    @Config(name = "Damage bat", description = "If true, the bat will take half a heart of damage when released from the bucket.")
    public static boolean damageBat = true;

    @Override
    public void register() {
        BAT_BUCKET_ITEM = new svenhjol.charm.module.bat_buckets.BatBucketItem(this);
    }

    @Override
    public void init() {
        UseEntityCallback.EVENT.register(this::tryCapture);
    }

    private InteractionResult tryCapture(Player player, Level world, InteractionHand hand, Entity entity, EntityHitResult hitResult) {
        if (!entity.level.isClientSide
            && entity instanceof Bat
            && ((Bat)entity).getHealth() > 0
        ) {
            Bat bat = (Bat)entity;
            ItemStack held = player.getItemInHand(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET)
                return InteractionResult.PASS;

            ItemStack batBucket = new ItemStack(BAT_BUCKET_ITEM);
            CompoundTag nbt = new CompoundTag();
            ItemNBTHelper.setCompound(batBucket, BatBucketItem.STORED_BAT_NBT, bat.saveWithoutId(nbt));

            if (held.getCount() == 1) {
                player.setItemInHand(hand, batBucket);
            } else {
                held.shrink(1);
                PlayerHelper.addOrDropStack(player, batBucket);
            }

            player.getCooldowns().addCooldown(BatBuckets.BAT_BUCKET_ITEM, 20);
            player.swing(hand);
            entity.discard();

            triggerCapturedBat((ServerPlayer) player);

            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    public static void triggerCapturedBat(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, BatBuckets.TRIGGER_CAPTURED_BAT);
    }

    public static void triggerUsedBatBucket(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, BatBuckets.TRIGGER_USED_BAT_BUCKET);
    }
}
