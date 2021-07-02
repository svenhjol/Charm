package svenhjol.charm.helper;

import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ClientRegistryHelper {
    public static void signMaterial(WoodType woodType) {
        Sheets.SIGN_MATERIALS.put(woodType, new Material(Sheets.SIGN_SHEET, new ResourceLocation("entity/signs/" + woodType.name())));
    }

    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void screenHandler(MenuType<H> screenHandler, ScreenRegistry.Factory<H, S> screen) {
        ScreenRegistry.register(screenHandler, screen);
    }

    public static ModelLayerLocation entityModelLayer(ResourceLocation id, ModelPart modelPart) {
        ModelLayerLocation layer = new ModelLayerLocation(id, "main");
        ClientHelper.ENTITY_MODEL_LAYERS.put(layer, modelPart);
        return layer;
    }
}
