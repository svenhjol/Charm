package svenhjol.charm.mixin.feature.advancements;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.advancements.CommonCallbacks;

@Mixin(ServerPacksSource.class)
public class ServerPackSourceMixin {
    @Inject(
        method = "createPackRepository(Lnet/minecraft/world/level/storage/LevelStorageSource$LevelStorageAccess;)Lnet/minecraft/server/packs/repository/PackRepository;",
        at = @At("HEAD")
    )
    private static void hookCreatePackRepository(LevelStorageSource.LevelStorageAccess levelStorageAccess, CallbackInfoReturnable<PackRepository> cir) {
        CommonCallbacks.handlePackReload("createPackRepository");
    }

    @Inject(
        method = "createBuiltinPack",
        at = @At("HEAD")
    )
    private void hookCreateBuiltinPack(String string, Pack.ResourcesSupplier resourcesSupplier, Component component, CallbackInfoReturnable<Pack> cir) {
        CommonCallbacks.handlePackReload("createBuiltinPack");
    }
}
