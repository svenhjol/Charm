package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;

public class RemoveNitwits extends Feature
{
    @Override
    public String getDescription()
    {
        return "Removes nitwits as spawnable villagers.";
    }

    @SubscribeEvent
    public void onVillagerSpawn(EntityJoinWorldEvent event)
    {
        if (!event.isCanceled()
            && !event.getWorld().isRemote
            && event.getEntity() instanceof EntityVillager
        ) {
            int i = 0;
            EntityVillager villager = (EntityVillager)event.getEntity();
            ResourceLocation res = villager.getProfessionForge().getRegistryName();
            DifficultyInstance difficulty = event.getWorld().getDifficultyForLocation(new BlockPos(villager));

            while (res != null && res.toString().equals("minecraft:nitwit")) {
                VillagerRegistry.setRandomProfession(villager, event.getWorld().rand);
                villager.finalizeMobSpawn(difficulty, null, false);
                res = villager.getProfessionForge().getRegistryName();
                if (++i > 20) break; // no infinite loops pls
            }

            if (i > 0) {
                Meson.debug("Changed nitwit to " + res);
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
