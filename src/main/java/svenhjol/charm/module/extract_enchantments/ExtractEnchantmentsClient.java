package svenhjol.charm.module.extract_enchantments;

import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.GrindstoneScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.module.extract_enchantments.ExtractEnchantments;

public class ExtractEnchantmentsClient extends CharmClientModule {
    public ExtractEnchantmentsClient(CharmModule module) {
        super(module);
    }

    public static void updateGrindstoneCost(GrindstoneScreen screen, Player player, PoseStack matrices, Font textRenderer, int width) {
        GrindstoneMenu screenHandler = screen.getMenu();

        // add all slot stacks to list for checking
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(screenHandler.getSlot(0).getItem());
        stacks.add(screenHandler.getSlot(1).getItem());

        // if it's a disenchant operation
        if (svenhjol.charm.module.extract_enchantments.ExtractEnchantments.shouldExtract(stacks)) {

            // get the stack to disenchant
            Optional<ItemStack> enchanted = svenhjol.charm.module.extract_enchantments.ExtractEnchantments.getEnchantedItemFromStacks(stacks);
            if (!enchanted.isPresent())
                return;

            // get the stack cost and render it
            int cost = svenhjol.charm.module.extract_enchantments.ExtractEnchantments.getCost(enchanted.get());

            int color = 8453920;
            String string = I18n.get("container.repair.cost", cost);

            if (!ExtractEnchantments.hasEnoughXp(player, cost))
                color = 16736352;

            int k = width - 8 - textRenderer.width(string) - 2;
            GuiComponent.fill(matrices, k - 2, 67, width - 8, 79, 1325400064);
            textRenderer.drawShadow(matrices, string, (float)k, 69.0F, color);
        }
    }
}
