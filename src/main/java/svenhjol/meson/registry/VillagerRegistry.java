package svenhjol.meson.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class VillagerRegistry
{
    public static VillagerProfession farmerProfession, librarianProfession, priestProfession, smithProfession, butcherProfession, nitwitProfession;
    public static VillagerCareer farmer, fisherman, shepherd, fletcher, librarian, cartographer, cleric, smith, weapon, tool, butcher, leatherWorker, nitwit;

    @SubscribeEvent
    public static void onRegisterVillagers(RegistryEvent.Register<VillagerProfession> event)
    {
        butcherProfession = event.getRegistry().getValue(new ResourceLocation("minecraft:butcher"));
        farmerProfession = event.getRegistry().getValue(new ResourceLocation("minecraft:farmer"));
        priestProfession = event.getRegistry().getValue(new ResourceLocation("minecraft:priest"));
        smithProfession = event.getRegistry().getValue(new ResourceLocation("minecraft:smith"));
        librarianProfession = event.getRegistry().getValue(new ResourceLocation("minecraft:librarian"));
        nitwitProfession = event.getRegistry().getValue(new ResourceLocation("minecraft:nitwit"));

        if (farmerProfession != null) {
            farmer = farmerProfession.getCareer(0);
            fisherman = farmerProfession.getCareer(1);
            shepherd = farmerProfession.getCareer(2);
            fletcher = farmerProfession.getCareer(3);
        }

        if (librarianProfession != null) {
            librarian = librarianProfession.getCareer(0);
            cartographer = librarianProfession.getCareer(1);
        }

        if (priestProfession != null) {
            cleric = priestProfession.getCareer(0);
        }

        if (butcherProfession != null) {
            butcher = butcherProfession.getCareer(0);
            leatherWorker = butcherProfession.getCareer(1);
        }

        if (smithProfession != null) {
            smith = smithProfession.getCareer(0);
            weapon = smithProfession.getCareer(1);
            tool = smithProfession.getCareer(2);
        }

        if (nitwitProfession != null) {
            nitwit = nitwitProfession.getCareer(0);
        }
    }
}
