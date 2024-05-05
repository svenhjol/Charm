package svenhjol.charm.feature.kilns;

import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class KilnScreen extends AbstractFurnaceScreen<KilnMenu> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/gui/container/smoker.png");
    private static final ResourceLocation LIT_PROGRESS_SPRITE = new ResourceLocation("container/smoker/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = new ResourceLocation("container/smoker/burn_progress");

    public KilnScreen(KilnMenu container, Inventory inventory, Component title) {
        super(container, new KilnRecipeBookScreen(), inventory, title, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE);
    }
}
