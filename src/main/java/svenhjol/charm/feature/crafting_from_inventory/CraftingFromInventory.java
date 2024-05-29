package svenhjol.charm.feature.crafting_from_inventory;

import svenhjol.charm.feature.crafting_from_inventory.common.*;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Allows crafting if the player has a crafting table in their inventory.")
public final class CraftingFromInventory extends CommonFeature {
    public final Registers registers;
    public final Providers providers;
    public final Networking networking;
    public final Handlers handlers;
    public final Advancements advancements;

    public CraftingFromInventory(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
        networking = new Networking(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
}
