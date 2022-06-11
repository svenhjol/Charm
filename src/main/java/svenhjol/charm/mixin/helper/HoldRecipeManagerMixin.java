package svenhjol.charm.mixin.helper;

import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.init.CharmResources;

@Mixin(ReloadableServerResources.class)
public class HoldRecipeManagerMixin {
    /**
     * Capture reference to the recipe manager so that the
     * SortingRecipeManager can process it when resources are reloaded.
     *
     * TODO: check threads? Don't know if the reload will always be able to access the holder.
     */
    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void hookInit(RegistryAccess.Frozen frozen, Commands.CommandSelection commandSelection, int i, CallbackInfo ci) {
        var manager = (ReloadableServerResources) (Object) this;
        CharmResources.recipeManagerHolder = manager.getRecipeManager();
    }
}
