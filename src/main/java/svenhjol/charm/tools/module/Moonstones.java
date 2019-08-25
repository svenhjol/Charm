package svenhjol.charm.tools.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tools.item.MoonstoneItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TOOLS, hasSubscriptions = true)
public class Moonstones extends MesonModule
{
    public static List<MoonstoneItem> items = new ArrayList<>();

    @Override
    public void init()
    {
        for (DyeColor value : DyeColor.values()) {
            items.add(new MoonstoneItem(this, value));
        }
    }

    @SubscribeEvent
    public void onMoonstoneUse(RightClickBlock event)
    {
        if (!event.getWorld().isRemote
            && event.getPlayer() != null
            && event.getPlayer().getHeldItem(event.getHand()).getItem() instanceof MoonstoneItem
        ) {
            World world = event.getWorld();
            PlayerEntity player = event.getPlayer();
            Hand hand = event.getHand();
            ItemStack held = player.getHeldItem(hand);
            BlockPos pos = event.getPos();

            if (player.isSneaking()) {
                MoonstoneItem.setStonePos(held, pos);
                MoonstoneItem.setStoneDim(held, WorldHelper.getDimensionId(world));
                player.swingArm(hand);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event)
    {
//        if (event.phase == TickEvent.Phase.END
//            && event.side.isServer()
//            && event.player.world.getGameTime() % 5 == 0
//            && (event.player.getHeldItemMainhand().getItem() instanceof MoonstoneItem
//                || event.player.getHeldItemOffhand().getItem() instanceof MoonstoneItem)
//        ) {
//
//            PlayerEntity player = event.player;
//            World world = player.world;
//
//            // try hands
//            for (Hand hand : new Hand[]{ Hand.MAIN_HAND, Hand.OFF_HAND }) {
//                if (!(player.getHeldItem(hand).getItem() instanceof MoonstoneItem)) continue;
//                ItemStack held = player.getHeldItem(hand);
//                MoonstoneItem stone = (MoonstoneItem)held.getItem();
//
//                BlockPos stonePos = MoonstoneItem.getStonePos(held);
//                if (stonePos == null) continue;
//
//                int stoneDim = MoonstoneItem.getStoneDim(held);
//                int playerDim = world.getDimension().getType().getId();
//
//                int stoneX = stonePos.getX();
//                int stoneZ = stonePos.getZ();
//                int playerX = MathHelper.floor(player.posX);
//                int playerZ = MathHelper.floor(player.posZ);
//
//                if (stoneDim == -1 && playerDim != -1) {
//                    stoneX *= 8.0f;
//                    stoneZ *= 8.0f;
//                } else if (playerDim == -1 && stoneDim != -1) {
//                    stoneX /= 8.0f;
//                    stoneZ /= 8.0f;
//                }
//
//                boolean alignedx = stoneX == playerX;
//                boolean alignedz = stoneZ == playerZ;
//
//                boolean origin = alignedx && alignedz;
//                boolean aligned = alignedx || alignedz;
//
//                if (aligned || origin) {
//                    float pitch = 0.5F + (stone.color.getId() / 16.0F);
//
//                    if (aligned && !ItemNBTHelper.getBoolean(held, MoonstoneItem.ALIGNED, false)) {
//                        SoundHelper.playSoundAtPos(player.getPosition(), CharmSounds.HOMING, SoundCategory.BLOCKS, 0.75F, pitch);
//                    }
//                    if (origin && !ItemNBTHelper.getBoolean(held, MoonstoneItem.ORIGIN, false)) {
//                        SoundHelper.playSoundAtPos(player.getPosition(), SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0F, pitch);
//                        SoundHelper.playSoundAtPos(player.getPosition(), CharmSounds.HOMING, SoundCategory.BLOCKS, 0.75F, pitch);
//                    }
//                }
//
//                ItemNBTHelper.setBoolean(held, MoonstoneItem.ALIGNED, aligned);
//                ItemNBTHelper.setBoolean(held, MoonstoneItem.ORIGIN, origin);
//            }
//        }
    }
}
