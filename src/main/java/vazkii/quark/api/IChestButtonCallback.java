package vazkii.quark.api;

import java.util.function.Consumer;

import net.minecraft.client.gui.GuiButton;

/**
 * Implement on a GuiScreen to add a callback when a chest button is
 * added to the GUI.
 */
public interface IChestButtonCallback {

	/**
	 * Called when a chest button is added. Use this to potentially reposition the button.
	 * Note that this is called before the button is added to the buttonList.
	 * 
	 * @param buttonType The type of the button, aka the ordinal() of the button type enum.
	 * @return true to keep the button, false to not have it be added. 
	 */
	public boolean onAddChestButton(GuiButton button, int buttonType);
	
}
