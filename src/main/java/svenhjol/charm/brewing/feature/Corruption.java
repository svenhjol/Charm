package svenhjol.charm.brewing.feature;

import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.brewing.potion.CorruptionPotion;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.EnchantmentHelper;

public class Corruption extends Feature
{
    public static CorruptionPotion corruption;

    public static int duration; // how long the corruption lasts in seconds
    public static int strength; // strength of wither effect
    public static double curseChance; // chance of player item curse every second
    public static int decayDistance; // distance (in blocks) from epicentre of corruption splash.  Disable decay with 0
    public static String[] undecayableBlocks = new String[] {}; // blocks that are not allowed to be decayed (turned to gravel or air)

    private int ticks; // for tracking effects

    @Override
    public String getDescription()
    {
        return "A nasty potion that curses player items, decays the environment, withers creatures, and turns villagers and horses bad.";
    }

    @Override
    public void setupConfig()
    {
        // configurable
        duration = propInt(
                "Corruption duration",
                "Duration (in seconds) of corruption effect when consumed.",
                5
        );
        strength = propInt(
                "Wither strength",
                "Strength of the Wither effect that accompanies the corruption effect.",
                3
        );
        curseChance = propDouble(
                "Curse chance",
                "Chance (out of 1.0) of an item becoming cursed every second when experiencing corruption.",
                0.25D
        );
        decayDistance = propInt(
                "Decay distance",
                "Distance (in blocks) around the area of splash corruption are at risk of decay.\n" +
                        "Set to 0 to disable this effect.",
                2
        );
        undecayableBlocks = propStringList(
                "Undecayable blocks",
                "List of blocks that cannot decay due to splash corruption effect.",
                new String[] {
                        "air", "bedrock", "obsidian", "end_portal_frame", "end_portal"
                }
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        corruption = new CorruptionPotion();
    }

    /**
     * @param event Ticks on server
     */
    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public void onPlayerTick(PlayerTickEvent event)
    {
        if (event.phase == Phase.END && event.player.getActivePotionEffect(corruption) != null) {
            if (ticks++ % 20 == 0 && event.player.world.rand.nextFloat() < curseChance) {
                EnchantmentHelper.applyRandomCurse(event.player);
                ticks = 0;
            }
        }
    }

    @SubscribeEvent
    public void onHit(ProjectileImpactEvent.Throwable event)
    {
        if (!event.isCanceled() && !event.getEntity().world.isRemote) {
            if (event.getThrowable() instanceof EntityPotion) {
                EntityPotion entityPotion = (EntityPotion)event.getThrowable();
                if (PotionUtils.getPotionFromItem(entityPotion.getPotion()) == CorruptionPotion.type) {
                    RayTraceResult ray = event.getRayTraceResult();
                    corruption.onHit(event.getRayTraceResult().entityHit, event.getEntity().world, ray.getBlockPos());
                }
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
