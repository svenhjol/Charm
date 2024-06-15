package svenhjol.charm.feature.kilns.client;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import svenhjol.charm.feature.kilns.common.Menu;

public class Screen extends AbstractFurnaceScreen<Menu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/smoker.png");

    public Screen(Menu container, Inventory inventory, Component title) {
        super(container, new RecipeBookScreen(), inventory, title, TEXTURE);
    }
}
