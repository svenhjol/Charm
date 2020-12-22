package svenhjol.charm.module;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.Arrays;
import java.util.List;

@Module(mod = Charm.MOD_ID, description = "Configure how often mob sounds are played.")
public class MobSoundCulling extends CharmModule {

    @Config(name = "Custom mob types", description = "List of mobs to apply the custom sound delay to.")
    public static List<String> customMobs = Arrays.asList(
        "minecraft:cow",
        "minecraft:sheep"
    );

    @Config(name = "Custom mob sound delay", description = "This delay applies to all mobs in the custom mob types list. As a guide, sheep and cows are 120 in vanilla.")
    public static int customDelay = 400;

    public static int getMinAmbientSoundDelay(MobEntity entity) {
        String id = Registry.ENTITY_TYPE.getId(entity.getType()).toString();

        if (ModuleHandler.enabled(MobSoundCulling.class) && customMobs.contains(id)) {
            return customDelay;
        } else {
            return -1;
        }
    }
}
