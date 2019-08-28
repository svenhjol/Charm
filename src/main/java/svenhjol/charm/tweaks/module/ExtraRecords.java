package svenhjol.charm.tweaks.module;

import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tweaks.item.CharmRecordItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Adds records (music discs) of all Minecraft background music.\n" +
        "These additional records will be dropped when a zombie is killed by a skeleton.")
public class ExtraRecords extends MesonModule
{
    public static String[] recordNames = new String[] {
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
    public static List<CharmRecordItem> records = new ArrayList<>();

    @Override
    public void init()
    {
        for (String name : recordNames) {
            SoundEvent sound = new SoundEvent(new ResourceLocation(Charm.MOD_ID, "record." + name));
            records.add(new CharmRecordItem(this, "record_" + name, sound, 0));
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
}
