package svenhjol.charm.mixin.registry.recipes;

import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.world.flag.FeatureFlagSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.foundation.recipe.common.Handlers;

@SuppressWarnings("UnreachableCode")
@Mixin(ReloadableServerResources.class)
public class ReloadableServerResourcesMixin {
    /**
     * Capture reference to the recipe manager so that the SortingRecipeManager can process it when resources are reloaded.
     */
    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void hookInit(RegistryAccess.Frozen frozen, FeatureFlagSet featureFlagSet, Commands.CommandSelection commandSelection, int i, CallbackInfo ci) {
        var manager = (ReloadableServerResources) (Object) this;
        Handlers.managerHolder = manager.getRecipeManager();
    }
}
