package svenhjol.charm.mobs.module;

import net.minecraft.entity.passive.ParrotEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.mobs.goal.LandOnEndRodGoal;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.MOBS)
public class ParrotsOnEndRods extends MesonModule {
    public static void addGoals(ParrotEntity parrot) {
        if (Meson.isModuleEnabled("charm:parrots_on_end_rods"))
            parrot.goalSelector.addGoal(2, new LandOnEndRodGoal(parrot, 0.8D));
    }
}
