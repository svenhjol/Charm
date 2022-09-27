package svenhjol.charm.mixin.colored_nether_portals;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.colored_nether_portals.ColoredNetherPortalBlock;

@Mixin(Gui.class)
public class ChangeOverlayColorMixin {
    @Redirect(
        method = "renderPortalOverlay",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/block/BlockModelShaper;getParticleIcon(Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/client/renderer/texture/TextureAtlasSprite;"
        )
    )
    private TextureAtlasSprite hookGetParticleIcon(BlockModelShaper blockModelShaper, BlockState targetState) {
        var client = Minecraft.getInstance();
        if (client != null && client.player != null && client.level != null) {
            var pos = client.player.blockPosition();
            var state = client.level.getBlockState(pos);
            var block = state.getBlock();

            if (block instanceof ColoredNetherPortalBlock portalBlock) {
                return blockModelShaper.getParticleIcon(portalBlock.defaultBlockState());
            }
        }

        return blockModelShaper.getParticleIcon(targetState);
    }
}
