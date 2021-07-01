package svenhjol.charm.mixin.core;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * Fix for resources not being loaded properly in dev environments.
 * You can safely blacklist this mixin if it causes trouble in a production environment.
 * @link {https://github.com/FabricMC/fabric/issues/66#issuecomment-614970964}
 */
@Mixin(VanillaPackResources.class)
public class FixFabricResourcePacks { // TODO: blacklisted for now, it's b0rk in pre3
    @Shadow
    private static @Final
    Map<PackType, FileSystem> ROOT_DIR_BY_TYPE;

    @Inject(method = "getResourceAsStream(Lnet/minecraft/server/packs/PackType;Lnet/minecraft/resources/ResourceLocation;)Ljava/io/InputStream;", at = @At("HEAD"), cancellable = true)
    private void onFindInputStream(PackType resourceType, ResourceLocation identifier, CallbackInfoReturnable<InputStream> callback) {
        if (VanillaPackResources.generatedDir != null) {
            // Fall through to Vanilla logic, they have a special case here.
            return;
        }

        FileSystem fs = ROOT_DIR_BY_TYPE.get(resourceType);
        if (fs == null) return; // Apparently Minecraft couldn't find its own resources, they'll be an error in the log for this

        Path path = fs.getPath(resourceType.getDirectory(), identifier.getNamespace(), identifier.getPath());
        if (path != null && Files.isRegularFile(path)) {
            try {
                callback.setReturnValue(Files.newInputStream(path));
            } catch (IOException e) {
                // Something went wrong, vanilla doesn't log these errors though
            }
        }
    }

    @Inject(method = "hasResource", at = @At("HEAD"), cancellable = true)
    private void fixHasResource(PackType type, ResourceLocation id, CallbackInfoReturnable<Boolean> callback) {
        if (VanillaPackResources.generatedDir != null) return;

        FileSystem fs = ROOT_DIR_BY_TYPE.get(type);
        if (fs == null) return;

        Path path = fs.getPath(type.getDirectory(), id.getNamespace(), id.getPath());
        if (path != null) callback.setReturnValue(Files.isRegularFile(path));
    }
}
