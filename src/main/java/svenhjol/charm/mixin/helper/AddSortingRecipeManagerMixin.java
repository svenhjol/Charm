package svenhjol.charm.mixin.helper;

import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.core.SortingRecipeManager;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ReloadableServerResources.class)
public class AddSortingRecipeManagerMixin  {
    @Unique private SortingRecipeManager sortingRecipeManager;

    @Inject(
        method = "<init>",
        at = @At("RETURN")
    )
    private void hookInit(RegistryAccess.Frozen frozen, Commands.CommandSelection commandSelection, int i, CallbackInfo ci) {
        var manager = (ReloadableServerResources) (Object) this;
        sortingRecipeManager = new SortingRecipeManager(manager);
    }

    @Inject(
        method = "listeners",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookListeners(CallbackInfoReturnable<List<PreparableReloadListener>> cir) {
        var list = new ArrayList<>(cir.getReturnValue());
        list.add(sortingRecipeManager);
        cir.setReturnValue(list);
    }
}
