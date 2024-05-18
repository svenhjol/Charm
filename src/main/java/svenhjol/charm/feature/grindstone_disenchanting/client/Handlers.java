package svenhjol.charm.feature.grindstone_disenchanting.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.feature.grindstone_disenchanting.GrindstoneDisenchantingClient;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.ArrayList;
import java.util.List;

public final class Handlers extends FeatureHolder<GrindstoneDisenchantingClient> {
    public Handlers(GrindstoneDisenchantingClient feature) {
        super(feature);
    }

    public void updateGrindstoneCost(GrindstoneScreen screen, Player player, GuiGraphics guiGraphics, Font font, int width) {
        var menu = screen.getMenu();

        // Add all slot stacks to list for checking.
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(menu.getSlot(0).getItem());
        stacks.add(menu.getSlot(1).getItem());

        // If it's a disenchant operation.
        if (feature().common().handlers.shouldExtract(stacks)) {

            // Get the stack to disenchant.
            var enchanted = feature().common().handlers.getEnchantedItemFromStacks(stacks);
            if (enchanted.isEmpty()) return;

            // Get the stack cost and render it.
            var cost = feature().common().handlers.getCost(enchanted.get());

            var color = 8453920;
            var string = I18n.get("container.repair.cost", cost);

            if (!feature().common().handlers.hasEnoughXp(player, cost)) {
                color = 16736352;
            }

            var k = width - 8 - font.width(string) - 2;
            guiGraphics.fill(k - 2, 67, width - 8, 79, 1325400064);
            guiGraphics.drawString(font, string, k, 69, color, true);
        }
    }
}
