package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import svenhjol.charm.Charm;
import svenhjol.charm.tweaks.item.CharmRecordItem;
import svenhjol.meson.Feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExtraRecords extends Feature
{
    public static String[] recordNames;
    public static List<CharmRecordItem> records = new ArrayList<>();

    @Override
    public void configure()
    {
        super.configure();

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
            "menu4",
            "axolotl",
            "dragon_fish",
            "shuniji"
        };
    }

    @Override
    public void init()
    {
        super.init();
        for (String name : recordNames) {
            SoundEvent sound = new SoundEvent(new ResourceLocation(Charm.MOD_ID, "record." + name));
            records.add(new CharmRecordItem("record_" + name, sound, 0));
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event)
    {
        if (event.getEntityLiving() instanceof ZombieEntity
            && event.getSource().getTrueSource() instanceof SkeletonEntity
        ) {
            CharmRecordItem item = records.get(new Random().nextInt(records.size()));
            event.getEntityLiving().entityDropItem(item, 1);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }

    @Override
    public void onRegisterItems(IForgeRegistry<Item> registry)
    {
        records.forEach(registry::register);
    }
}
