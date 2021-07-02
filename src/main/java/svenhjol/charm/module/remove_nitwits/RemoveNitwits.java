package svenhjol.charm.module.remove_nitwits;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import svenhjol.charm.Charm;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "When any action would cause a villager to become a nitwit, it becomes an unemployed villager instead.")
public class RemoveNitwits extends CharmModule {
    @Override
    public void runWhenEnabled() {
        AddEntityCallback.EVENT.register(this::changeNitwitProfession);
    }

    private InteractionResult changeNitwitProfession(Entity entity) {
        if (!entity.level.isClientSide
            && entity instanceof Villager
        ) {
            Villager villager = (Villager) entity;
            VillagerData data = villager.getVillagerData();

            if (data.getProfession() == VillagerProfession.NITWIT) {
                villager.setVillagerData(data.setProfession(VillagerProfession.NONE));
                Charm.LOG.debug("[RemoveNitwits] Made nitwit `" + villager.getStringUUID() + "` into a proper villager at pos: " + villager.blockPosition());
            }
        }

        return InteractionResult.PASS;
    }
}
