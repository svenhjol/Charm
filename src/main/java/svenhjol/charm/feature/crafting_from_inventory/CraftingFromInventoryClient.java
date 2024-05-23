package svenhjol.charm.feature.crafting_from_inventory;

import svenhjol.charm.feature.crafting_from_inventory.client.Handlers;
import svenhjol.charm.feature.crafting_from_inventory.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class CraftingFromInventoryClient extends ClientFeature implements CommonResolver<CraftingFromInventory> {
    public final Registers registers;
    public final Handlers handlers;

    public CraftingFromInventoryClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<CraftingFromInventory> typeForCommon() {
        return CraftingFromInventory.class;
    }
}
