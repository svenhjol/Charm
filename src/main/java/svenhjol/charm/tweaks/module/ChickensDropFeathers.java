package svenhjol.charm.tweaks.module;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Chickens randomly drop feathers.")
public class ChickensDropFeathers extends MesonModule {
    @SubscribeEvent
    public void onTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof ChickenEntity
            && !event.getEntityLiving().world.isRemote
            && event.getEntityLiving().isAlive()
            && !event.getEntityLiving().isChild()
            && !((ChickenEntity) event.getEntityLiving()).isChickenJockey()
        ) {
            ChickenEntity chicken = (ChickenEntity) event.getEntityLiving();
            if (chicken.timeUntilNextEgg <= 1) {
                if (chicken.world.rand.nextFloat() < 0.2F) {
                    chicken.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (chicken.world.rand.nextFloat() - chicken.world.rand.nextFloat()) * 0.2F + 1.0F);
                    chicken.entityDropItem(Items.FEATHER);
                    chicken.timeUntilNextEgg = chicken.world.rand.nextInt(3000) + 3000;
                }
            }
        }
    }
}
