package svenhjol.charm.loot.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.brewing.feature.Corruption;
import svenhjol.meson.Feature;
import svenhjol.charm.brewing.potion.CorruptionPotion;

public class WitchesDropCorruption extends Feature
{
    public static double dropChance;
    public static double lootingBoost;

    @Override
    public String getDescription()
    {
        return "A witch has a chance to drop a Potion of Corruption when killed by a player.";
    }

    @Override
    public void setupConfig()
    {
        dropChance = propDouble(
            "Drop chance",
            "Chance (out of 1.0) of a witch dropping a Potion of Corruption when killed by the player.",
            0.2D
        );

        // internal
        lootingBoost = 0.05D;
    }

    @SubscribeEvent
    public void onDrops(LivingDropsEvent event)
    {
        if (!event.getEntityLiving().world.isRemote
            && Charm.hasFeature(Corruption.class)
            && event.getEntityLiving() instanceof EntityWitch
            && event.getSource().getTrueSource() instanceof EntityPlayer
            && event.getEntityLiving().world.rand.nextFloat() <= (dropChance + lootingBoost * event.getLootingLevel())
        ) {
            Entity entity = event.getEntity();
            ItemStack item = new ItemStack(Items.POTIONITEM);
            PotionType corruption = CorruptionPotion.type;
            PotionUtils.addPotionToItemStack(item, corruption);
            event.getDrops().add(new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, item));
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
