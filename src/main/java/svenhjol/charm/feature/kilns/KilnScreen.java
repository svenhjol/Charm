package svenhjol.charm.feature.kilns;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class KilnScreen extends AbstractFurnaceScreen<KilnMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/smoker.png");

    public KilnScreen(KilnMenu container, Inventory inventory, Component title) {
        super(container, new KilnRecipeBookScreen(), inventory, title, TEXTURE);
    }
}
