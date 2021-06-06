package svenhjol.charm.module.kilns;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charm.module.kilns.KilnRecipeBookScreen;
import svenhjol.charm.module.kilns.KilnScreenHandler;

@Environment(EnvType.CLIENT)
public class KilnScreen extends AbstractFurnaceScreen<svenhjol.charm.module.kilns.KilnScreenHandler> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/smoker.png");

    public KilnScreen(KilnScreenHandler container, Inventory inventory, Component title) {
        super(container, new KilnRecipeBookScreen(), inventory, title, TEXTURE);
    }
}
