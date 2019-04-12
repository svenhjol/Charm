package svenhjol.charm.world.feature;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import svenhjol.charm.Charm;
import svenhjol.charm.world.client.render.RenderSpectre;
import svenhjol.charm.world.entity.EntitySpectre;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.EntityHelper;

public class Spectre extends Feature
{
    public static int weight, min, max; // weight: spawn probability, min: min spawn count, max: max spawn count
    public static int despawnLight; // light level at which spectres will despawn
    public static int trackingRange; // how far away spectres will start to path find
    public static int spawnDepth; // spectres spawn at this Y-level and deeper
    public static float maxHealth; // max health of the spectre
    public static float attackDamage; // how much damage the spectre does when touching the player
    public static float movementSpeed; // how fast the spectre moves
    public static boolean applyCurse; // true to apply random curse to player on touch

    public static int eggColor1, eggColor2;

    @Override
    public String getDescription()
    {
        return "Spectres spawn in the darkness at quite a low depth, and are almost invisible to see.\n" +
                "When they touch the player they curse a weapon, tool or armor item and disappear.\n" +
                "They can be defeated by light - or by the lightest of hits.";
    }

    @Override
    public void setupConfig()
    {
        // configurable
        despawnLight = propInt(
                "Despawn light level",
                "Light level at which a Spectre disappears.",
                8
        );
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

        // internal
        applyCurse = true;
        eggColor1 = 0xececec;
        eggColor2 = 0xffed7d;
        maxHealth = 0.5f;
        attackDamage = 0.01f;
        movementSpeed = 0.2f;
        min = 4;
        max = 8;
        trackingRange = 100;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        String spectreName = Charm.MOD_ID + ":spectre";

        EntityRegistry.registerModEntity(new ResourceLocation(spectreName), EntitySpectre.class, spectreName, 0, Charm.instance, trackingRange, 3, true, eggColor1, eggColor2);
        LootTableList.register(EntitySpectre.LOOT_TABLE);
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

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
