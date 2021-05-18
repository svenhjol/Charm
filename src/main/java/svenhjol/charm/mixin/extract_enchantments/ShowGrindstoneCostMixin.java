package svenhjol.charm.mixin.extract_enchantments;

import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GrindstoneScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.client.ExtractEnchantmentsClient;

@Mixin(GrindstoneScreen.class)
public abstract class ShowGrindstoneCostMixin<T extends ScreenHandler> extends HandledScreen<T> {
    private final ThreadLocal<PlayerEntity> player = new ThreadLocal<>();

    public ShowGrindstoneCostMixin(T handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        player.remove();
        player.set(inventory.player);
    }

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hookInit(GrindstoneScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        this.player.set(inventory.player);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        GrindstoneScreen screen = (GrindstoneScreen)(Object)this;
        ExtractEnchantmentsClient.updateGrindstoneCost(screen, this.player.get(), matrices, this.textRenderer, this.backgroundWidth);
        super.drawForeground(matrices, mouseX, mouseY);
    }
}
