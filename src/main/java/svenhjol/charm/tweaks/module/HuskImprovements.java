package svenhjol.charm.tweaks.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Husks spawn anywhere within their biome and drop sand, making them easier to farm.")
public class HuskImprovements extends MesonModule
{
    @Config(name = "Spawn anywhere in biome", description = "If true, husks can spawn anywhere within their biome rather than just the surface.")
    public static boolean spawnAnywhere = true;

    @Config(name = "Drop sand when killed", description = "If true, husks drop sand when killed.")
    public static boolean dropSand = true;

    @Config(name = "Drop chance", description = "Chance (out of 1.0) of a husk dropping sand when it is killed.")
    public static double chance = 0.33D;

    public static double lootingBoost = 0.1D;

    public static boolean canSeeSky(IWorld world, BlockPos pos)
    {
        return true; // don't care about skylight check for spawning husks
    }

    @SubscribeEvent
    public void onHuskDrops(LivingDropsEvent event)
    {
        if (!event.getEntityLiving().world.isRemote
            && dropSand
            && event.getEntityLiving() instanceof HuskEntity
            && event.getEntityLiving().world.rand.nextFloat() <= (chance + (lootingBoost * event.getLootingLevel()))
        ) {
            Entity entity = event.getEntity();
            BlockPos entityPos = entity.getPosition();
            ItemStack sand = new ItemStack(Items.SAND);
            event.getDrops().add( new ItemEntity(entity.getEntityWorld(), entityPos.getX(), entityPos.getY(), entityPos.getZ(), sand) );
        }
    }
}
