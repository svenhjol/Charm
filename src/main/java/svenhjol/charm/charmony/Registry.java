package svenhjol.charm.charmony;

import net.minecraft.resources.ResourceLocation;

public interface Registry {
    /**
     * Get the modId associated with this registry instance.
     */
    String id();

    /**
     * Create a resourcelocation using the mod associated with this registry instance.
     */
    ResourceLocation id(String path);
}
