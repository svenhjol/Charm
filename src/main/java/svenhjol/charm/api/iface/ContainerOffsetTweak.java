package svenhjol.charm.api.iface;

import com.mojang.datafixers.util.Pair;
import net.minecraft.client.gui.screens.Screen;

/**
 * Definition for a screen and its container offset.
 */
@SuppressWarnings("unused")
public interface ContainerOffsetTweak {
    Class<? extends Screen> getScreen();

    Pair<Integer, Integer> getOffset();
}
