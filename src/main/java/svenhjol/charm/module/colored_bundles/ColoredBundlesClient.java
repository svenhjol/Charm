package svenhjol.charm.module.colored_bundles;

import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistryAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BundleItem;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class ColoredBundlesClient extends CharmClientModule {
    public ColoredBundlesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ModelPredicateProviderRegistryAccessor.callRegister(new ResourceLocation("colored_bundle_filled"), (itemStack, clientWorld, livingEntity, i)
            -> BundleItem.getFullnessDisplay(itemStack));
    }
}
