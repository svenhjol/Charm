package svenhjol.charm.mixin.extractable_enchantments;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.extractable_enchantments.ExtractableEnchantmentsClient;

@Mixin(GrindstoneScreen.class)
public abstract class GrindstoneScreenMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    @Unique
    private final ThreadLocal<Player> player = new ThreadLocal<>();

    public GrindstoneScreenMixin(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        player.remove();
        player.set(inventory.player);
    }

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hookInit(GrindstoneMenu menu, Inventory inventory, Component title, CallbackInfo ci) {
        player.set(inventory.player);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        var screen = (GrindstoneScreen)(Object)this;
        ExtractableEnchantmentsClient.updateGrindstoneCost(screen, player.get(), guiGraphics, font, imageWidth);
        super.renderLabels(guiGraphics, mouseX, mouseY);
    }
}
