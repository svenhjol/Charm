package svenhjol.charm.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.village.VillagerData;
import net.minecraft.village.VillagerProfession;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "When any action would cause a villager to become a nitwit, it becomes an unemployed villager instead.")
public class RemoveNitwits extends CharmModule {
    @Override
    public void init() {
        AddEntityCallback.EVENT.register(this::changeNitwitProfession);
    }

    private ActionResult changeNitwitProfession(Entity entity) {
        if (!entity.world.isClient
            && entity instanceof VillagerEntity
        ) {
            VillagerEntity villager = (VillagerEntity) entity;
            VillagerData data = villager.getVillagerData();

            if (data.getProfession() == VillagerProfession.NITWIT) {
                villager.setVillagerData(data.withProfession(VillagerProfession.NONE));
                Charm.LOG.debug("Changed nitwit's profession to NONE: " + villager.getUuidAsString());
            }
        }

        return ActionResult.PASS;
    }
}
