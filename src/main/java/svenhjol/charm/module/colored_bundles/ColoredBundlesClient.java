package svenhjol.charm.module.colored_bundles;

import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistryAccessor;
import net.minecraft.item.BundleItem;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class ColoredBundlesClient extends CharmClientModule {
    public ColoredBundlesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ModelPredicateProviderRegistryAccessor.callRegister(new Identifier("colored_bundle_filled"), (itemStack, clientWorld, livingEntity, i)
            -> BundleItem.getAmountFilled(itemStack));
    }
}
