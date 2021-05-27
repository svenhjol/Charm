package svenhjol.charm.module.kilns;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractFurnaceScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class KilnScreen extends AbstractFurnaceScreen<KilnScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("textures/gui/container/smoker.png");

    public KilnScreen(KilnScreenHandler container, PlayerInventory inventory, Text title) {
        super(container, new KilnRecipeBookScreen(), inventory, title, TEXTURE);
    }
}
