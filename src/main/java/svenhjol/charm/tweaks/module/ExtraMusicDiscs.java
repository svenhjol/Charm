package svenhjol.charm.tweaks.module;

import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.base.CharmSounds;
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
    private static final String NAME = "svenhjol";

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

    private static CharmMusicDiscItem cold;

    @Override
    public void init()
    {
        Item.Properties props = new Item.Properties().maxStackSize(1).rarity(Rarity.RARE);

        if (isEnabled()) {
            props.group(ItemGroup.MISC);
        }

        for (String name : discNames) {
            SoundEvent sound = new SoundEvent(new ResourceLocation(Charm.MOD_ID, "music_disc." + name));
            discs.add(new CharmMusicDiscItem(this, "music_disc_" + name, sound, props,0));
        }

        cold = new CharmMusicDiscItem(this, "music_disc_charm_cold", CharmSounds.MUSIC_COLD, props,0);
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

    @SubscribeEvent
    public void onName(PlayerInteractEvent.EntityInteract event)
    {
        if (event.getTarget() instanceof FoxEntity
            && event.getPlayer() != null
            && ((FoxEntity)event.getTarget()).getVariantType() == FoxEntity.Type.SNOW
        ) {
            FoxEntity fox = (FoxEntity)event.getTarget();
            ItemStack held = event.getPlayer().getHeldItem(event.getHand());
            if (!(held.getItem() instanceof NameTagItem)) return;
            if (fox.getDisplayName().getUnformattedComponentText().equals(NAME)) return;

            String name = held.getDisplayName().getUnformattedComponentText();
            if (name.equals(NAME)) {
                fox.setHeldItem(Hand.MAIN_HAND, new ItemStack(cold));
            }
        }
    }
}
