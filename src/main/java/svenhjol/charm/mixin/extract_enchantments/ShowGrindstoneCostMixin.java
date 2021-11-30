package svenhjol.charm.mixin.extract_enchantments;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.GrindstoneMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.annotation.CharmMixin;
import svenhjol.charm.module.extract_enchantments.ExtractEnchantmentsClient;

@CharmMixin(disableIfModsPresent = {"grindenchantments"})
@Mixin(GrindstoneScreen.class)
public abstract class ShowGrindstoneCostMixin<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    private final ThreadLocal<Player> player = new ThreadLocal<>();

    public ShowGrindstoneCostMixin(T menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        player.remove();
        player.set(inventory.player);
    }

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hookInit(GrindstoneMenu menu, Inventory inventory, Component title, CallbackInfo ci) {
        this.player.set(inventory.player);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
        GrindstoneScreen screen = (GrindstoneScreen)(Object)this;
        ExtractEnchantmentsClient.updateGrindstoneCost(screen, player.get(), poseStack, font, imageWidth);
        super.renderLabels(poseStack, mouseX, mouseY);
    }
}
