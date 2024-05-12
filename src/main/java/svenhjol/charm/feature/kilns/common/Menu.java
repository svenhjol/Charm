package svenhjol.charm.feature.kilns.common;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.foundation.Resolve;

public class Menu extends AbstractFurnaceMenu {
    private static final Kilns KILNS = Resolve.feature(Kilns.class);
    private static final Firing FIRING = Resolve.feature(Firing.class);

    public Menu(int syncId, Inventory playerInventory) {
        super(KILNS.registers.menu.get(), FIRING.registers.recipeType.get(),
            RecipeBookType.valueOf("KILN"), syncId, playerInventory);
    }

    public Menu(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(KILNS.registers.menu.get(), FIRING.registers.recipeType.get(),
            RecipeBookType.valueOf("KILN"), syncId, playerInventory, inventory, propertyDelegate);
    }

    @Override
    public void clicked(int slot, int button, ClickType clickType, Player player) {
        if (!player.level().isClientSide
            && slot == 2
            && clickType == ClickType.PICKUP) {
            KILNS.advancements.firedItem(player);
        }
        super.clicked(slot, button, clickType, player);
    }
}
