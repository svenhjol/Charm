package svenhjol.charm.mixin.helper;

import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.annotation.CharmMixin;
import svenhjol.charm.module.core.SortingRecipeManager;

@Mixin(value = ServerResources.class)
@CharmMixin(required = true)
public class AddSortingRecipeManagerMixin {
    @Shadow @Final private ReloadableResourceManager resources;
    @Unique private SortingRecipeManager sortingRecipeManager;

    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void hookInit(RegistryAccess registryAccess, Commands.CommandSelection commandSelection, int i, CallbackInfo ci) {
        sortingRecipeManager = new SortingRecipeManager((ServerResources) (Object) this);
        this.resources.registerReloadListener(sortingRecipeManager);
    }
}
