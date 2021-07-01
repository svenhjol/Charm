package svenhjol.charm.module.colored_bundles;

import net.fabricmc.fabric.mixin.object.builder.ModelPredicateProviderRegistryAccessor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BundleItem;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = ColoredBundles.class)
public class ColoredBundlesClient extends ClientModule {

    @Override
    public void register() {
        ModelPredicateProviderRegistryAccessor.callRegister(new ResourceLocation("colored_bundle_filled"), (itemStack, clientWorld, livingEntity, i)
            -> BundleItem.getFullnessDisplay(itemStack));
    }
}
