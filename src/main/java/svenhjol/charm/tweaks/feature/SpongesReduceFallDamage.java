package svenhjol.charm.tweaks.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSponge;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;

public class SpongesReduceFallDamage extends Feature
{
    public static float amountAbsorbed; // percentage of damage to absorb

    @Override
    public String getDescription()
    {
        return "Landing on sponge absorbs some of the player's fall damage.";
    }

    @Override
    public void configure()
    {
        super.configure();

        // internal
        amountAbsorbed = 0.5f;
    }

    @SubscribeEvent
    public void onLanding(LivingHurtEvent event)
    {
        if (!event.isCanceled()
            && !event.getEntityLiving().world.isRemote
            && event.getSource() == DamageSource.FALL
        ) {
            World world = event.getEntityLiving().getEntityWorld();
            Block down = world.getBlockState(event.getEntityLiving().getPosition().down()).getBlock();

            if (down instanceof BlockSponge) {
                event.setAmount(event.getAmount() - (event.getAmount() * amountAbsorbed));
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
