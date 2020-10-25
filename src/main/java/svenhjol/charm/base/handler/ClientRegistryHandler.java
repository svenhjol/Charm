package svenhjol.charm.base.handler;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

@SuppressWarnings("UnusedReturnValue")
public class ClientRegistryHandler {
    public static <H extends ScreenHandler, S extends Screen & ScreenHandlerProvider<H>> void screenHandler(ScreenHandlerType<? extends H> screenHandler, ScreenRegistry.Factory<H, S> screen) {
        ScreenRegistry.register(screenHandler, screen);
    }

    public static void setRenderLayer(Block block, RenderLayer renderLayer) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, renderLayer);
    }
}
