package svenhjol.charm.mixin;

import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.client.ExtractEnchantmentsClient;

@Mixin(GrindstoneScreen.class)
public abstract class GrindstoneScreenMixin<T extends ScreenHandler> extends HandledScreen<T> {
    public GrindstoneScreenMixin(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        GrindstoneScreen screen = (GrindstoneScreen)(Object)this;
        ExtractEnchantmentsClient.updateGrindstoneCost(screen, this.playerInventory.player, matrices, this.textRenderer, this.backgroundWidth);
        super.drawForeground(matrices, mouseX, mouseY);
    }
}
