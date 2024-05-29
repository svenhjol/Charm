package svenhjol.charm.feature.crafting_from_inventory;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.crafting_from_inventory.client.Handlers;
import svenhjol.charm.feature.crafting_from_inventory.client.Registers;

@Feature
public final class CraftingFromInventoryClient extends ClientFeature implements LinkedFeature<CraftingFromInventory> {
    public final Registers registers;
    public final Handlers handlers;

    public CraftingFromInventoryClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<CraftingFromInventory> typeForLinked() {
        return CraftingFromInventory.class;
    }
}
