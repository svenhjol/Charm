package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.Feature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WaterInHotBiomes extends Feature
{
    public static List<String> validBiomeTypes = new ArrayList<>();
    public static int duration;

    @Override
    public String getDescription()
    {
        return "Drink a bottle of water in a hot and dry biome to provide a regeneration boost.";
    }

    @Override
    public void setupConfig()
    {
        duration = 5; // in seconds
        validBiomeTypes = Arrays.asList("HOT", "DRY"); // the biome types that this effect works in
    }

    @SubscribeEvent
    public void onUseItemFinish(LivingEntityUseItemEvent.Finish event)
    {
        ItemStack item = event.getItem();

        if (event.getEntity() instanceof EntityPlayer
            && !event.getEntity().world.isRemote
            && item.getItem() == Items.POTIONITEM
            && PotionUtils.getPotionFromItem(item) == PotionTypes.WATER
        ) {
            EntityPlayer player = (EntityPlayer)event.getEntity();

            if (PlayerHelper.getBiomeTypes(player).containsAll(validBiomeTypes)) {
                player.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, duration * 20));
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
