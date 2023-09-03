package svenhjol.charm.feature.bamboo_wood;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_barrels.VariantBarrels;
import svenhjol.charm.feature.variant_chests.VariantChests;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charm.feature.wood.Wood;
import svenhjol.charm_api.CharmApi;
import svenhjol.charm_api.iface.IRemovesRecipes;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.ArrayList;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Bamboo barrels, bookcases, chests and ladders.")
public class BambooWood extends CharmFeature implements IRemovesRecipes {
    @Override
    public void register() {
        var material = BambooMaterial.BAMBOO;
        var registry = Charm.instance().registry();

        Wood.registerBarrel(registry, material);
        Wood.registerBookshelf(registry, material);
        Wood.registerChiseledBookshelf(registry, material);
        Wood.registerChest(registry, material);
        Wood.registerTrappedChest(registry, material);
        Wood.registerLadder(registry, material);

        CharmApi.registerProvider(this);
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        var charm = Charm.instance();
        List<ResourceLocation> remove = new ArrayList<>();

        if (!charm.loader().isEnabled(VariantBarrels.class)) {
            remove.add(charm.makeId("bamboo_barrel"));
        }

        if (!charm.loader().isEnabled(VariantChests.class)) {
            remove.add(charm.makeId("bamboo_chest"));
            remove.add(charm.makeId("bamboo_trapped_chest"));
        }

        if (!charm.loader().isEnabled(VariantLadders.class)) {
            remove.add(charm.makeId("bamboo_ladder"));
        }

        if (!charm.loader().isEnabled("Woodcutters")) {
            remove.add(charm.makeId("bamboo_wood/woodcutting/"));
        }

        return remove;
    }
}
