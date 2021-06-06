package svenhjol.charm.module.chickens_drop_feathers;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.annotation.Module;

@Module(mod = Charm.MOD_ID, description = "Chickens randomly drop feathers.",
    requiresMixins = {"chickens_drop_feathers.*"})
public class ChickensDropFeathers extends CharmModule {
    public static void tryDropFeather(Chicken chicken) {
        if (!ModuleHandler.enabled("charm:chickens_drop_feathers"))
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
