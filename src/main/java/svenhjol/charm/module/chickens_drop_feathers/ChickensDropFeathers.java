package svenhjol.charm.module.chickens_drop_feathers;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Chickens randomly drop feathers.")
public class ChickensDropFeathers extends svenhjol.charm.loader.CommonModule {
    public static boolean isEnabled;

    @Override
    public void run() {
        isEnabled = true;
    }

    public static void tryDropFeather(Chicken chicken) {
        if (!isEnabled)
            return;

        if (chicken.isAlive()
            && !chicken.isBaby()
            && !chicken.level.isClientSide
            && !chicken.isChickenJockey()
            && chicken.level.random.nextFloat() < 0.2F
        ) {
            chicken.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (chicken.level.random.nextFloat() - chicken.level.random.nextFloat()) * 0.2F + 1.0F);
            chicken.spawnAtLocation(Items.FEATHER);
            chicken.eggTime = chicken.level.random.nextInt(3000) + 3000;
        }
    }
}
