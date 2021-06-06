package svenhjol.charm.module.ender_bundles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.item.CharmItem;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.ender_bundles.EnderBundles;
import svenhjol.charm.module.ender_bundles.EnderBundlesClient;

import java.util.Optional;

public class EnderBundleItem extends CharmItem {
    private static final int ITEM_BAR_COLOR = Mth.color(0.4F, 0.4F, 1.0F);

    public EnderBundleItem(CharmModule module) {
        super(module, "ender_bundle", (new Item.Properties())
            .stacksTo(1)
            .tab(CreativeModeTab.TAB_TOOLS));
    }

    @Environment(EnvType.CLIENT)
    public static float getAmountFilled() {
        return svenhjol.charm.module.ender_bundles.EnderBundlesClient.CACHED_AMOUNT_FILLED;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return svenhjol.charm.module.ender_bundles.EnderBundlesClient.CACHED_AMOUNT_FILLED > 0;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.min(13, (int)(12 * EnderBundlesClient.CACHED_AMOUNT_FILLED));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack bundle, Slot slot, ClickAction clickType, Player player) {
        if (player.containerMenu instanceof ChestMenu
            && !(slot.container instanceof Inventory)) {
            return false; // don't allow inside ender chest inventory
        } else if (player.containerMenu instanceof CreativeModeInventoryScreen.ItemPickerMenu) {
            return false; // TODO: why is creative container wack?
        } else if (clickType != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemStack = slot.getItem();
            if (itemStack.isEmpty()) {
                Optional<ItemStack> out = removeLastStack(player);
                out.ifPresent(slot::safeInsert);
            } else if (itemStack.getItem().canFitInsideContainerItems()) {
                ItemStack out = addToBundle(player, itemStack);
                itemStack.setCount(out.getCount());
            }
            player.containerMenu.broadcastChanges();
        }

        return true;
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickType, Player player, SlotAccess stackReference) {
        if (player.containerMenu instanceof ChestMenu
            && !(slot.container instanceof Inventory)
        ) {
            return false; // don't allow inside ender chest inventory
        } else if (player.containerMenu instanceof CreativeModeInventoryScreen.ItemPickerMenu) {
            return false; // TODO: why is creative contains wack?
        } else if (clickType == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (otherStack.isEmpty()) {
                removeLastStack(player).ifPresent(stackReference::set);
            } else {
                ItemStack out = addToBundle(player, otherStack);
                otherStack.setCount(out.getCount());
            }
            player.containerMenu.broadcastChanges();
            return true;
        }
        return false;
    }

    private static ItemStack addToBundle(Player player, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().canFitInsideContainerItems()) {
            PlayerEnderChestContainer inventory = player.getEnderChestInventory();
            ItemStack out = inventory.addItem(stack);
            inventory.setChanged();

            if (!player.level.isClientSide)
                EnderBundles.triggerUsedEnderBundle((ServerPlayer) player);

            return out;
        }

        return stack;
    }

    private static Optional<ItemStack> removeLastStack(Player player) {
        PlayerEnderChestContainer inventory = player.getEnderChestInventory();
        int index = 0;
        int size = inventory.getContainerSize();
        for (int i = 0; i < size; i++) {
            if (!inventory.getItem(i).isEmpty()) {
                index = i;
            }
        }
        ItemStack stack = inventory.getItem(index);
        if (stack.isEmpty())
            return Optional.empty();

        ItemStack out = stack.copy();
        stack.setCount(0);
        inventory.setChanged();

        return Optional.of(out);
    }
}
