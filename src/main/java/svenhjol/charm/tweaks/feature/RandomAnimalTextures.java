package svenhjol.charm.tweaks.feature;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.tweaks.render.RenderOcelotTextures;
import svenhjol.charm.tweaks.render.RenderWolfTextures;
import svenhjol.meson.Feature;
import svenhjol.meson.iface.IMesonEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RandomAnimalTextures extends Feature
{
    @Override
    public String getDescription()
    {
        return "Like Quark's random animal textures, this feature lets wolves and cats spawn with additional skin textures.";
    }

    public enum MOB implements IMesonEnum
    {
        CAT,
        WOLF
    }

    public enum SET
    {
        OCELOTS,
        REDCATS,
        BLACKCATS,
        OTHERCATS,
        WOLVES
    }

    public static Map<SET, String[]> sets = new HashMap<>();
    public static boolean enableCats;
    public static boolean enableWolves;

    @Override
    public void configure()
    {
        super.configure();

        enableCats = propBoolean(
            "Random cat textures",
            "Enable random textures depending on the cat type, borrowed lovingly from Minecraft 1.14.",
            true
        );

        enableWolves = propBoolean(
            "Random wolf textures",
            "Enable random wolf textures with brown, grey and black variants.",
            true
        );

        String[] ocelots = propStringList(
            "Ocelot textures",
            "Textures to use when rendering Ocelots.",
            new String[] {
                "minecraft:ocelot"
            }
        );

        String[] redCats = propStringList(
            "Red cat textures",
            "Textures to use when rendering Red cats (Cat Type 1).",
            new String[] {
                "minecraft:red",
                "charm:persian",
                "charm:tabby",
            }
        );

        String[] blackCats = propStringList(
            "Black cat textures",
            "Textures to use when rendering Black cats (Cat Type 2).",
            new String[] {
                "minecraft:black",
                "charm:all_black",
            }
        );

        String[] otherCats = propStringList(
            "Siamese / other cat textures",
            "Textures to use when rendering Siamese and other cats (Cat Type 3).",
            new String[] {
                "minecraft:siamese",
                "charm:calico",
                "charm:white",
                "charm:ragdoll",
                "charm:british_shorthair",
                "charm:jellie"
            }
        );

        String[] wolves = propStringList(
            "Wolf textures",
            "Textures to use when rendering wolves.",
            new String[] {
                "minecraft:wolf",
                "charm:brown",
                "charm:grey",
                "charm:black"
            }
        );

        sets.put(SET.OCELOTS, ocelots);
        sets.put(SET.REDCATS, redCats);
        sets.put(SET.BLACKCATS, blackCats);
        sets.put(SET.OTHERCATS, otherCats);
        sets.put(SET.WOLVES, wolves);
    }

    @Override
    public void preInitClient(FMLPreInitializationEvent event)
    {
        if (enableWolves) {
            //noinspection unchecked
            RenderingRegistry.registerEntityRenderingHandler(EntityWolf.class, RenderWolfTextures.factory());
        }
        if (enableCats) {
            //noinspection unchecked
            RenderingRegistry.registerEntityRenderingHandler(EntityOcelot.class, RenderOcelotTextures.factory());
        }
    }

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getWolfTexture(EntityWolf entity)
    {
        String[] set = sets.get(SET.WOLVES);
        String tex;

        if (set.length > 0) {
            tex = getRandomTexture(entity, set);
        } else {
            tex = "minecraft:wolf";
        }

        if (entity.isTamed()) {
            tex += "_tame";
        } else if (entity.isAngry()) {
            tex += "_angry";
        }

        return getTextureFromString(MOB.WOLF, tex);
    }

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getOcelotTexture(EntityOcelot entity)
    {
        String[] set;
        String defTex;

        switch (entity.getTameSkin()) {
            case 0: set = sets.get(SET.OCELOTS); defTex = "minecraft:ocelot"; break;
            case 1: set = sets.get(SET.REDCATS); defTex = "minecraft:red"; break;
            case 2: set = sets.get(SET.BLACKCATS); defTex = "minecraft:black"; break;
            default: set = sets.get(SET.OTHERCATS); defTex = "minecraft:siamese"; break;
        }

        String tex;

        if (set.length > 0) {
            tex = getRandomTexture(entity, set);
        } else {
            tex = defTex;
        }

        return getTextureFromString(MOB.CAT, tex);
    }

    public static String getRandomTexture(Entity entity, String[] set)
    {
        UUID id = entity.getUniqueID();
        int choice = Math.abs((int)(id.getMostSignificantBits() % set.length));
        return set[choice];
    }

    public static ResourceLocation getTextureFromString(MOB mob, String texture)
    {
        String prefix = "textures/entity/" + mob.getName() + "/";
        String[] a = texture.split(":");
        return new ResourceLocation(a[0], prefix + a[1] + ".png");
    }
}
