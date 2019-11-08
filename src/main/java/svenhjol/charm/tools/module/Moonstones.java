package svenhjol.charm.tools.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tools.item.MoonstoneItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TOOLS, hasSubscriptions = true,
    description = "A moonstone helps align builds. Right-click while sneaking to bind a moonstone to any location.\n" +
        "When you are aligned with the location on the X or Z axis, the stone will glow and the homing sound will play." +
        "When standing on the original location, a chime sound will play.\n" +
        "This works across dimensions, helping you prceisely align portals between the Overworld and the Nether.")
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
                MoonstoneItem.setStoneDim(held, player.dimension.getId());
                player.swingArm(hand);
            }
        }
    }
}
