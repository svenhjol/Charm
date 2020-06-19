package svenhjol.charm.decoration.module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.render.CustomWolfRenderer;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.IMesonEnum;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION,
    description = "Wolves will spawn with texture variants.")
public class RandomAnimalTextures extends MesonModule {
    public static List<String> wolves = new ArrayList<>();

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        // standard textures
        wolves.addAll(Arrays.asList(
            "minecraft:wolf",
            "charm:brownwolf",
            "charm:greywolf",
            "charm:blackwolf",
            "charm:amotwolf",
            "charm:jupiter1390"
        ));

        // add NeverLoseGuy textures
        for (int i = 1; i <= 25; i++) {
            wolves.add("charm:wolf" + i);
        }
    }

    public enum MobType implements IMesonEnum {
        WOLF
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void onClientSetup(FMLClientSetupEvent event) {
        if (enabled)
            //noinspection unchecked
            RenderingRegistry.registerEntityRenderingHandler(EntityType.WOLF, CustomWolfRenderer.factory());
    }

    @OnlyIn(Dist.CLIENT)
    public static ResourceLocation getWolfTexture(WolfEntity entity) {
        String texture = getRandomTexture(entity, wolves);
        if (entity.isTamed()) {
            texture += "_tame";
        } else if (entity.isAngry()) {
            texture += "_angry";
        }

        return getTextureFromString(MobType.WOLF, texture);
    }

    public static String getRandomTexture(Entity entity, List<String> set) {
        UUID id = entity.getUniqueID();
        int choice = Math.abs((int) (id.getMostSignificantBits() % set.size()));
        return set.get(choice);
    }

    public static ResourceLocation getTextureFromString(MobType type, String texture) {
        String prefix = "textures/entity/" + type.getName() + "/";
        String[] a = texture.split(":");
        return new ResourceLocation(a[0], prefix + a[1] + ".png");
    }
}
