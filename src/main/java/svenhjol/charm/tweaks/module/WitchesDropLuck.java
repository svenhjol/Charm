package svenhjol.charm.tweaks.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potions;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PotionHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true)
public class WitchesDropLuck extends MesonModule
{
    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a witch dropping a Potion of Luck when killed by the player.")
    public static double chance = 0.05D;

    public static double lootingBoost = 0.025D;

    @SubscribeEvent
    public void onWitchDrops(LivingDropsEvent event)
    {
        if (!event.getEntityLiving().world.isRemote
            && event.getEntityLiving() instanceof WitchEntity
            && event.getSource().getTrueSource() instanceof PlayerEntity
            && event.getEntityLiving().world.rand.nextFloat() <= (chance + lootingBoost * event.getLootingLevel())
        ) {
            Entity entity = event.getEntity();
            ItemStack potion = PotionHelper.getPotionItemStack(Potions.LUCK, 1);
            event.getDrops().add( new ItemEntity(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, potion) );
        }
    }
}
