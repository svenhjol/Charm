package svenhjol.charm.module.colored_bundles;

import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistryAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BundleItem;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = ColoredBundles.class)
public class ColoredBundlesClient extends CharmModule {

    @Override
    public void register() {
        ModelPredicateProviderRegistryAccessor.callRegister(new ResourceLocation("colored_bundle_filled"), (stack, level, entity, i)
            -> BundleItem.getFullnessDisplay(stack));
    }
}
