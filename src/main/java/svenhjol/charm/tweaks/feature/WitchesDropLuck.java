package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;

public class WitchesDropLuck extends Feature
{
    public static double dropChance; // chance of drop
    public static double lootingBoost; // amount that looting multiplies chance

    @Override
    public String getDescription()
    {
        return "A witch has a chance to drop a Potion of Luck when killed by a player.";
    }

    @Override
    public void configure()
    {
        super.configure();

        dropChance = propDouble(
            "Drop chance",
            "Chance (out of 1.0) of a witch dropping a Potion of Luck when killed by the player.",
            0.1D
        );

        // internal
        lootingBoost = 0.05D;
    }

    @SubscribeEvent
    public void onDrops(LivingDropsEvent event)
    {
        if (!event.getEntityLiving().world.isRemote
            && event.getEntityLiving() instanceof EntityWitch
            && event.getSource().getTrueSource() instanceof EntityPlayer
            && event.getEntityLiving().world.rand.nextFloat() <= (dropChance + lootingBoost * event.getLootingLevel())
        ) {
            Entity entity = event.getEntity();
            ItemStack item = new ItemStack(Items.POTIONITEM);
            PotionType luck = PotionType.REGISTRY.getObject(new ResourceLocation("luck"));
            PotionUtils.addPotionToItemStack(item, luck);
            event.getDrops().add( new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, item) );
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
