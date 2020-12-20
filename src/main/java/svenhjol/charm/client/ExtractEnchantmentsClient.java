package svenhjol.charm.client;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.ExtractEnchantments;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExtractEnchantmentsClient extends CharmClientModule {
    public ExtractEnchantmentsClient(CharmModule module) {
        super(module);
    }

    public static void updateGrindstoneCost(GrindstoneScreen screen, PlayerEntity player, MatrixStack matrices, TextRenderer textRenderer, int width) {
        GrindstoneScreenHandler screenHandler = screen.getScreenHandler();

        // add all slot stacks to list for checking
        List<ItemStack> stacks = new ArrayList<>();
        stacks.add(screenHandler.getSlot(0).getStack());
        stacks.add(screenHandler.getSlot(1).getStack());

        // if it's a disenchant operation
        if (ExtractEnchantments.shouldExtract(stacks)) {

            // get the stack to disenchant
            Optional<ItemStack> enchanted = ExtractEnchantments.getEnchantedItemFromStacks(stacks);
            if (!enchanted.isPresent())
                return;

            // get the stack cost and render it
            int cost = ExtractEnchantments.getCost(enchanted.get());

            int color = 8453920;
            String string = I18n.translate("container.repair.cost", cost);

            if (!ExtractEnchantments.hasEnoughXp(player, cost))
                color = 16736352;

            int k = width - 8 - textRenderer.getWidth(string) - 2;
            DrawableHelper.fill(matrices, k - 2, 67, width - 8, 79, 1325400064);
            textRenderer.drawWithShadow(matrices, string, (float)k, 69.0F, color);
        }
    }
}
