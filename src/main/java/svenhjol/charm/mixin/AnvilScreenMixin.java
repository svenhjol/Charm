package svenhjol.charm.mixin;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.AnvilImprovements;

import java.util.List;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {
    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Redirect(
        method = "drawForeground",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z"
        )
    )
    private boolean hookMaximumCostCheck(PlayerAbilities abilities, MatrixStack matrix, int x, int y) {
        return AnvilImprovements.allowTooExpensive() || abilities.creativeMode;
    }

    @Override
    public List<Text> getTooltipFromItem(ItemStack stack) {
        List<Text> tooltip = super.getTooltipFromItem(stack);

        if (!AnvilImprovements.showRepairCost)
            return tooltip;

        return AnvilImprovements.addRepairCostToTooltip(stack, tooltip);
    }
}
