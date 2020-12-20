package svenhjol.charm.mixin;

import net.minecraft.resource.DefaultResourcePack;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
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

@Mixin(DefaultResourcePack.class)
abstract class DefaultResourcePackMixin {
    @Shadow private static @Final Map<ResourceType, FileSystem> typeToFileSystem;

    @Inject(method = "findInputStream", at = @At("HEAD"), cancellable = true)
    private void onFindInputStream(ResourceType resourceType, Identifier identifier, CallbackInfoReturnable<InputStream> callback) {
        if (DefaultResourcePack.resourcePath != null) {
            // Fall through to Vanilla logic, they have a special case here.
            return;
        }

        FileSystem fs = typeToFileSystem.get(resourceType);
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

    @Inject(method = "contains", at = @At("HEAD"), cancellable = true)
    private void fixContains(ResourceType type, Identifier id, CallbackInfoReturnable<Boolean> callback) {
        if (DefaultResourcePack.resourcePath != null) return;

        FileSystem fs = typeToFileSystem.get(type);
        if (fs == null) return;

        Path path = fs.getPath(type.getDirectory(), id.getNamespace(), id.getPath());
        if (path != null) callback.setReturnValue(Files.isRegularFile(path));
    }
}
