package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.tweaks.item.CharmItemRecord;
import svenhjol.meson.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExtraRecords extends Feature
{
    public static String[] recordNames;
    public static List<CharmItemRecord> records = new ArrayList<>();

    @Override
    public String getDescription()
    {
        return "Adds records of all Minecraft background music.";
    }

    @Override
    public void configure()
    {
        super.configure();

        // internal
        recordNames = new String[] {
            "calm1",
            "calm2",
            "calm3",
            "hal1",
            "hal2",
            "hal3",
            "hal4",
            "nuance1",
            "nuance2",
            "piano1",
            "piano2",
            "piano3",
            "creative1",
            "creative2",
            "creative3",
            "creative4",
            "creative5",
            "creative6",
            "nether1",
            "nether2",
            "nether3",
            "nether4",
            "menu1",
            "menu2",
            "menu3",
            "menu4"
        };
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        for (String name : recordNames) {
            SoundEvent sound = new SoundEvent(new ResourceLocation(Charm.MOD_ID, "record." + name));
            records.add(new CharmItemRecord(Charm.MOD_ID, name, sound));
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event)
    {
        if (event.getEntityLiving() instanceof EntityZombie
            && event.getSource().getTrueSource() instanceof EntitySkeleton
        ) {
            event.getEntityLiving().dropItem(records.get(new Random().nextInt(records.size())), 1);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
