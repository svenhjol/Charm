package svenhjol.charm.tweaks.module;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Chickens randomly drop feathers.")
public class ChickensDropFeathers extends MesonModule
{
    @Config(name = "Interval", description = "Number of seconds before a chicken has a chance to drop a feather.")
    public static int interval = 20;

    @Config(name = "Chance", description = "Chance of a chicken dropping a feather after the configured interval.")
    public static double chance = 0.25D;

    @SubscribeEvent
    public void onTick(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntityLiving() instanceof ChickenEntity
            && !event.getEntityLiving().world.isRemote
            && event.getEntityLiving().isAlive()
            && !event.getEntityLiving().isChild()
            && !((ChickenEntity) event.getEntityLiving()).isChickenJockey()
        ) {
            ChickenEntity chicken = (ChickenEntity)event.getEntityLiving();
            if (chicken.ticksExisted % interval == 0
                && chicken.world.rand.nextFloat() < (float)chance
            ) {
                chicken.entityDropItem(Items.FEATHER);
            }
        }
    }
}
