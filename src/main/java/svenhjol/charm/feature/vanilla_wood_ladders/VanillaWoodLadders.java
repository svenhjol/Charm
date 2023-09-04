package svenhjol.charm.feature.vanilla_wood_ladders;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_ladders.VariantLadders;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.iface.IRecipeRemoveProvider;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.enums.VanillaWood;

import java.util.ArrayList;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Ladders in all vanilla wood types.")
public class VanillaWoodLadders extends CharmFeature implements IRecipeRemoveProvider {
    @Override
    public void register() {
        var registry = Charm.instance().registry();

        for (var material : VanillaWood.getTypes()) {
            VariantLadders.registerLadder(registry, material);
        }

        CharmonyApi.registerProvider(this);
    }

    @Override
    public List<ResourceLocation> getRecipesToRemove() {
        List<ResourceLocation> remove = new ArrayList<>();

        if (!Charm.instance().loader().isEnabled("Woodcutters")) {
            remove.add(Charm.instance().makeId("vanilla_wood_ladders/woodcutting/"));
        }

        return remove;
    }
}
