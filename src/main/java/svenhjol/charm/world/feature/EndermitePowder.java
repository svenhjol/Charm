package svenhjol.charm.world.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmEntityIDs;
import svenhjol.charm.world.entity.EntityEndermitePowder;
import svenhjol.charm.world.item.ItemEndermitePowder;
import svenhjol.meson.Feature;

public class EndermitePowder extends Feature
{
    public static ItemEndermitePowder endermitePowder;
    public static double dropChance;
    public static double lootingBoost;

    @Override
    public String getDescription()
    {
        return "Endermite Powder has a chance of being dropped from an Endermite.\n" +
            "Use it in the End to help locate an End City.";
    }

    @Override
    public void setupConfig()
    {
        dropChance = propDouble(
            "Drop chance",
            "Chance (out of 1.0) of an endermite dropping Endermite Powder when killed by the player.",
            0.5D
        );

        // internal
        lootingBoost = 0.1D;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        endermitePowder = new ItemEndermitePowder();
        String name = Charm.MOD_ID + ":endermite_powder";
        EntityRegistry.registerModEntity(new ResourceLocation(name), EntityEndermitePowder.class, name, CharmEntityIDs.ENDERMITE_POWDER, Charm.instance, 80, 10, false);
    }

    @SubscribeEvent
    public void onDrops(LivingDropsEvent event)
    {
        if (!event.getEntityLiving().world.isRemote
            && event.getEntityLiving() instanceof EntityEndermite
            && event.getSource().getTrueSource() instanceof EntityPlayer
            && event.getEntityLiving().world.rand.nextFloat() <= (dropChance + lootingBoost * event.getLootingLevel())
        ) {
            Entity entity = event.getEntity();
            ItemStack item = new ItemStack(endermitePowder);
            event.getDrops().add(new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, item));
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
