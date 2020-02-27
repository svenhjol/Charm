package svenhjol.charm.tweaks.module;

import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tweaks.item.CharmMusicDiscItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Adds music discs of all Minecraft ambient music.\n" +
        "These additional discs will be dropped when a zombie is killed by a skeleton.")
public class ExtraMusicDiscs extends MesonModule
{
    public static String[] discNames = new String[] {
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
        "shuniji",
        "dragon",
        "end"
    };
    public static List<CharmMusicDiscItem> discs = new ArrayList<>();

    @Override
    public void init()
    {
        Item.Properties props = new Item.Properties().maxStackSize(1).rarity(Rarity.RARE);

        if (enabled)
            props.group(ItemGroup.MISC);

        for (String name : discNames) {
            SoundEvent sound = new SoundEvent(new ResourceLocation(Charm.MOD_ID, "music_disc." + name));
            discs.add(new CharmMusicDiscItem(this, "music_disc_" + name, sound, props,0));
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event)
    {
        if (event.getEntityLiving() instanceof ZombieEntity
            && event.getSource().getTrueSource() instanceof SkeletonEntity
        ) {
            CharmMusicDiscItem item = discs.get(new Random().nextInt(discs.size()));
            event.getEntityLiving().entityDropItem(item, 1);
        }
    }
}
