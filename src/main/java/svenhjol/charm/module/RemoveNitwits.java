package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "When any action would cause a villager to become a nitwit, it becomes an unemployed villager instead.")
public class RemoveNitwits extends MesonModule {
    @Override
    public void init() {
        if (!enabled)
            return;

        AddEntityCallback.EVENT.register((entity -> {
            changeNitwitProfession(entity);
            return ActionResult.PASS;
        }));
    }

    private void changeNitwitProfession(Entity entity) {
        if (!entity.world.isClient
            && entity instanceof VillagerEntity
        ) {
            VillagerEntity villager = (VillagerEntity) entity;
            VillagerData data = villager.getVillagerData();

            if (data.getProfession() == VillagerProfession.NITWIT) {
                villager.setVillagerData(data.withProfession(VillagerProfession.NONE));
                Meson.LOG.debug("Changed nitwit's profession to NONE: " + villager.getUuidAsString());
            }
        }
    }
}
