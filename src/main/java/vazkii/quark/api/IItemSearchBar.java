package vazkii.quark.api;

import net.minecraft.client.gui.GuiTextField;

/**
 * Implement on a GuiScreen to add search bar.
 */
public interface IItemSearchBar {

	/**
	 * Called when the search bar is added. You should move it to the correct position
	 * in this method.
	 */
	public void onSearchBarAdded(GuiTextField bar);
		
	/**
	 * Called to render the text field background.
	 * @return false to render the default one, true to not. If you return true you should 
	 * probably render your own here.
	 */
	public default boolean renderBackground(int x, int y) {
		return false;
	}
	
}
