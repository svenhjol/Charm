package svenhjol.charm.tweaks.module;

import net.minecraft.block.Block;
import net.minecraft.block.SpongeBlock;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Landing on sponge absorbs some of the player's fall damage.")
public class SpongesReduceFallDamage extends MesonModule
{
    @Config(name = "Damage absorbed", description = "Percentage (where 1.0 = 100%) of damage absorbed by sponge blocks.")
    public static double percentageAbsorbed = 0.75D;

    @SubscribeEvent
    public void onLanding(LivingHurtEvent event)
    {
        if (!event.isCanceled()
            && !event.getEntityLiving().world.isRemote
            && event.getSource() == DamageSource.FALL
        ) {
            World world = event.getEntityLiving().getEntityWorld();
            Block down = world.getBlockState(event.getEntityLiving().getPosition().down()).getBlock();

            if (down instanceof SpongeBlock) {
                event.setAmount(event.getAmount() - (float)(event.getAmount() * percentageAbsorbed));
            }
        }
    }
}
