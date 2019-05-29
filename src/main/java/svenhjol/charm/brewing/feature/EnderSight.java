package svenhjol.charm.brewing.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.brewing.client.EnderSightSound;
import svenhjol.charm.brewing.message.MessageSetStructure;
import svenhjol.charm.brewing.potion.EnderSightPotion;
import svenhjol.meson.handler.ClientHandler;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.NetworkHandler;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.helper.PlayerHelper;

public class EnderSight extends Feature
{
    public static EnderSightPotion potion;

    public static int duration; // length of effect in seconds
    public static double endermanSpawnChance; // chance of enderman spawn nearby
    public static boolean doShaderEffects; // true to enable the shader effect on the client

    private BlockPos nearestStructure = null;
    private boolean hasEnderSight = false;
    private boolean isCloseAndPlaying = false;
    private int ticks = 0;
    private int clientTicks = 0;
    private int endermanSpawnDistance;
    private double minDist; // minimum distance from a stronghold at which the proxy check starts

    @Override
    public String getDescription()
    {
        return "Brew an Eye of Ender with a Night Vision potion to make a Potion of Ender Sight.\n" +
                "Enderman are attracted to your location and you might hear a ringing sound coming from the direction of a stronghold.\n";
    }

    @Override
    public void setupConfig()
    {
        // configurable
        duration = propInt(
                "Ender Sight duration",
                "Duration (in seconds) of Ender Sight effect when consumed.",
                10
        );
        endermanSpawnChance = propDouble(
                "Enderman spawn chance",
                "Chance (out of 1.0) of an Enderman spawning close by when experiencing the Ender Sight effect.",
                0.05D
        );
        doShaderEffects = propBoolean(
                "Client shader effect",
                "Invert the colours when under the effect of Ender Sight.",
                true
        );

        // internal
        minDist = 2000D;
        endermanSpawnDistance = 5;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        potion = new EnderSightPotion();
        NetworkHandler.register(MessageSetStructure.class, Side.CLIENT);
    }

    /**
     * @param event Ticks on client
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.phase == TickEvent.Phase.START && mc.player != null && !mc.isGamePaused()) {
            EntityPlayer player = mc.player;
            World world = player.world;

            if (!hasEnderSight && mc.player.getActivePotionEffect(potion) != null) {

                // ender sight started
                hasEnderSight = true;
                mc.getSoundHandler().playSound(new EnderSightSound(CharmSounds.ENDER_WHISPERS, player, 0.25f, 0.8f));
                if (doShaderEffects) mc.entityRenderer.loadShader(new ResourceLocation(Charm.MOD_ID, "shaders/ender_sight.json"));
            } else if (hasEnderSight && player.getActivePotionEffect(potion) != null) {

                if (world.provider.getDimension() == 0) {
                    nearestStructure = ClientHandler.getNearestStronghold();
                } else if (world.provider.getDimension() == 1) {
                    nearestStructure = ClientHandler.getNearestEndCity();
                }

                if (nearestStructure != null) {
                    // distance to nearest stronghold
                    double dist = nearestStructure.distanceSq(player.posX, player.posY, player.posZ);

                    // angle from player to stronghold.  Seems to go nuts when you're close.
                    double d1 = MathHelper.positiveModulo(player.rotationYaw / 360.0D, 1.0D);
                    double d2 = PlayerHelper.getLocationAngleToPlayer(player, nearestStructure) / (Math.PI * 2D);
                    double d0 = (0.5D - (d1 - 0.25D - d2));

                    if (dist >= minDist) {

                        double d = MathHelper.positiveModulo(d0, 1.0D);

                        // far away, do the angle thing
                        if (d > 0.95D || d < 0.05D) {
                            if (clientTicks % 4 + (mc.world.rand.nextInt(4)) == 0) {
                                float pitch = (0.9f + (0.1f - (float)(d > 0.95 ? 1.0 - d : d)));
                                mc.getSoundHandler().playSound(new EnderSightSound(CharmSounds.ENDER_RESONANCE, player, 0.8f, pitch));
                            }
                        }
                    } else {

                        if (!isCloseAndPlaying) {
                            isCloseAndPlaying = true;
                            mc.getSoundHandler().playSound(new EnderSightSound(CharmSounds.ENDER_ABOVE_STRONGHOLD, player, 0.8f, 1.0f));
                        }
                    }
                    clientTicks++;
                }

            } else if (hasEnderSight && player.getActivePotionEffect(potion) == null) {

                // ender sight finished
                hasEnderSight = false;
                isCloseAndPlaying = false;
                nearestStructure = null;
                if (doShaderEffects) mc.entityRenderer.stopUseShader();
            }
        }
    }


    /**
     * @param event Ticks on server
     */
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && event.side.isServer() && event.player.getActivePotionEffect(potion) != null) {
            if (nearestStructure == null) {
                int dimension = event.player.world.provider.getDimension();
                String structureType = "";

                if (dimension == 0) {
                    structureType = "Stronghold";
                } else if (dimension == 1) {
                    structureType = "EndCity";
                }
                if (!structureType.isEmpty()) {
                    nearestStructure = event.player.getEntityWorld().findNearestStructure(structureType, event.player.getPosition(), false);
                    if (nearestStructure != null) { // can be null if structures turned off for this world
                        NetworkHandler.INSTANCE.sendTo(new MessageSetStructure(structureType, nearestStructure), (EntityPlayerMP) event.player);
                    }
                }
            }

            if (ticks++ % 20 == 0 && event.player.world.rand.nextFloat() < endermanSpawnChance) {
                EntityHelper.spawnEntityNearPlayer(event.player, endermanSpawnDistance, new ResourceLocation("enderman"));
                ticks = 0;
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
