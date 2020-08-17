package svenhjol.charm.module;

import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Chickens randomly drop feathers.")
public class ChickensDropFeathers extends MesonModule {
    public static void tryDropFeather(ChickenEntity chicken) {
        if (!Meson.enabled("charm:chickens_drop_feathers"))
            return;

        if (chicken.isAlive()
            && !chicken.isBaby()
            && !chicken.world.isClient
            && !chicken.hasJockey()
            && chicken.world.random.nextFloat() < 0.2F
        ) {
            chicken.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (chicken.world.random.nextFloat() - chicken.world.random.nextFloat()) * 0.2F + 1.0F);
            chicken.dropItem(Items.FEATHER);
            chicken.eggLayTime = chicken.world.random.nextInt(3000) + 3000;
        }
    }
}
