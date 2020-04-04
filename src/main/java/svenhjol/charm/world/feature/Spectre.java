package svenhjol.charm.world.feature;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmEntityIDs;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.world.client.render.RenderSpectre;
import svenhjol.charm.world.entity.EntitySpectre;
import svenhjol.charm.world.message.MessageSpectreDespawn;
import svenhjol.meson.Feature;
import svenhjol.meson.handler.NetworkHandler;
import svenhjol.meson.helper.EntityHelper;
import svenhjol.meson.helper.SoundHelper;

public class Spectre extends Feature
{
    public static int weight, min, max; // weight: spawn probability, min: min spawn count, max: max spawn count
    public static int despawnLight; // light level at which spectres will despawn
    public static int trackingRange; // how far away spectres will start to path find
    public static int spawnDepth; // spectres spawn at this Y-level and deeper
    public static int weaknessDuration; // if spectres don't curse then this is how long their weakness effect lasts
    public static int weaknessAmplifier; // if spectres don't curse then this is how strong their weakness effect is
    public static float maxHealth; // max health of the spectre
    public static float attackDamage; // how much damage the spectre does when touching the player
    public static float movementSpeed; // how fast the spectre moves
    public static boolean applyCurse; // true for spectres to curse, false to apply weakness

    public static int eggColor1, eggColor2;

    @Override
    public String getDescription()
    {
        return "Spectres spawn in the darkness at quite a low depth, and are almost invisible to see.\n" +
                "When they touch the player they curse a weapon, tool or armor item and disappear.\n" +
                "They can be defeated by light and convert sand to soul sand on death.";
    }

    @Override
    public void configure()
    {
        super.configure();

        // configurable
        despawnLight = MathHelper.clamp(propInt(
                "Despawn light level",
                "Light level at which a Spectre disappears.",
                8
        ), 1, 16);
        spawnDepth = propInt(
                "Spawn depth",
                "Maximum depth at which Spectres can spawn.",
                24
        );
        weight = propInt(
                "Spawn weight",
                "The higher this value, the more Spectres will spawn.",
                100
        );
        applyCurse = propBoolean(
            "Apply curse",
            "If true, spectres will curse armor or items belonging to the player.\n" +
                "If false, spectres will apply a weakness effect.",
            false
        );

        // internal
        eggColor1 = 0xececec;
        eggColor2 = 0xffed7d;
        maxHealth = 0.5f;
        attackDamage = 0.01f;
        movementSpeed = 0.19f;
        min = 4;
        max = 8;
        trackingRange = 100;
        weaknessDuration = 2;
        weaknessAmplifier = 1;
        if (despawnLight == 0) despawnLight = 1;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        String name = Charm.MOD_ID + ":spectre";
        EntityRegistry.registerModEntity(new ResourceLocation(name), EntitySpectre.class, name, CharmEntityIDs.SPECTRE, Charm.instance, trackingRange, 3, true, eggColor1, eggColor2);
        LootTableList.register(EntitySpectre.LOOT_TABLE);
        NetworkHandler.register(MessageSpectreDespawn.class, Side.CLIENT);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        EntityRegistry.addSpawn(EntitySpectre.class, weight, min, max, EnumCreatureType.MONSTER, EntityHelper.getBiomesWithMob(EntityZombie.class));
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntitySpectre.class, RenderSpectre.FACTORY);
    }

    @SideOnly(Side.CLIENT)
    public static void effectDespawn(BlockPos pos)
    {
        World world = Minecraft.getMinecraft().world;

        for (int i = 0; i < 8; ++i) {
            double d0 = world.rand.nextGaussian() * 0.02D;
            double d1 = world.rand.nextGaussian() * 0.02D;
            double d2 = world.rand.nextGaussian() * 0.02D;
            double dx = (float) pos.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
            double dy = (float) pos.getY() + 0.65f + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 1f);
            double dz = (float) pos.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
            world.spawnParticle(EnumParticleTypes.CLOUD, dx, dy, dz, d0, d1, d2);
        }

        SoundHelper.playSoundAtPos(world, pos, CharmSounds.SPECTRE_HIT, 0.2f - (world.rand.nextFloat() / 4.0f), 1.25f - (world.rand.nextFloat() / 4.0f));
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
