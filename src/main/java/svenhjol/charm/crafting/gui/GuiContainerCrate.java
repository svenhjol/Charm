package svenhjol.charm.crafting.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonGuiContainer;
import vazkii.quark.api.IChestButtonCallback;
import vazkii.quark.api.IItemSearchBar;

public class GuiContainerCrate extends MesonGuiContainer implements IChestButtonCallback, IItemSearchBar
{
    public GuiContainerCrate(Container container, InventoryPlayer playerInv)
    {
        super("tile.charm:crate.name", container, playerInv, new ResourceLocation(Charm.MOD_ID, "textures/gui/generic9.png"));
    }

    @Override
    public boolean onAddChestButton(GuiButton button, int buttonType)
    {
        return true;
    }

    @Override
    public void onSearchBarAdded(GuiTextField bar)
    {
        bar.y = getGuiTop() + 20;
    }
}