package svenhjol.charm.tools.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tools.item.BatBucketItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ClientHelper;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TOOLS, hasSubscriptions = true,
    description = "Right-click a bat with a bucket to capture it.\n" +
        "Right-click your Bat in a Bucket to release the bat and help locate entities around you.")
public class BatInABucket extends MesonModule {
    public static BatBucketItem item;

    @Config(name = "Glowing time", description = "Number of seconds that entities will receive the glowing effect.")
    public static int time = 10;

    @Config(name = "Viewing range", description = "Range (in blocks) in which entities will glow.")
    public static int range = 24;

    // client stuff
    public static int clientTicks;
    public static double clientRange;
    public static List<LivingEntity> clientEntities = new ArrayList<>();

    @Override
    public void init() {
        item = new BatBucketItem(this);
    }

    @SubscribeEvent
    public void onInteractWithBat(EntityInteract event) {
        if (!event.isCanceled()
            && !event.getWorld().isRemote
            && event.getTarget() instanceof BatEntity
            && ((BatEntity) event.getTarget()).getHealth() > 0
        ) {
            PlayerEntity player = event.getPlayer();
            Hand hand = event.getHand();
            BatEntity bat = (BatEntity) event.getTarget();
            ItemStack held = player.getHeldItem(hand);

            if (held.isEmpty() || held.getItem() != Items.BUCKET) return;
            ItemStack batBucket = new ItemStack(item);
            CompoundNBT tag = bat.serializeNBT();
            ItemNBTHelper.setCompound(batBucket, BatBucketItem.BAT_SIGNAL, tag);

            if (held.getCount() == 1) {
                player.setHeldItem(hand, batBucket);
            } else {
                held.shrink(1);
                PlayerHelper.addOrDropStack(player, batBucket);
            }
            player.swingArm(hand);
            event.getTarget().remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (clientTicks > 0 && clientRange > 0 && event.phase == Phase.START && ClientHelper.getClientPlayer() != null) {
            PlayerEntity player = ClientHelper.getClientPlayer();

            if (clientTicks % 10 == 0 || clientEntities.isEmpty()) {
                // sample nearby entities and set them glowing
                setGlowing(false);
                setNearbyEntities(player);
                setGlowing(true);
            }

            if (--clientTicks <= 0) setGlowing(false);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void setNearbyEntities(PlayerEntity player) {
        clientEntities.clear();
        AxisAlignedBB area = player.getBoundingBox().grow(clientRange, clientRange / 2.0, clientRange);
        Predicate<LivingEntity> selector = entity -> true;
        clientEntities = player.world.getEntitiesWithinAABB(LivingEntity.class, area, selector);
    }

    @OnlyIn(Dist.CLIENT)
    private void setGlowing(boolean glowing) {
        for (Entity entity : clientEntities) {
            entity.setGlowing(glowing);
        }
    }
}
