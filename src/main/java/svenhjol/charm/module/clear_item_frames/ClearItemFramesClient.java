package svenhjol.charm.module.clear_item_frames;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.init.CharmParticles;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.clear_item_frames.network.ClientReceiveAddAmethyst;
import svenhjol.charm.module.clear_item_frames.network.ClientReceiveRemoveAmethyst;

@ClientModule(module = ClearItemFrames.class)
public class ClearItemFramesClient extends CharmModule {
    public static ClientReceiveAddAmethyst CLIENT_RECEIVE_ADD_AMETHYST;
    public static ClientReceiveRemoveAmethyst CLIENT_RECEIVE_REMOVE_AMETHYST;

    @Override
    public void register() {
        CLIENT_RECEIVE_ADD_AMETHYST = new ClientReceiveAddAmethyst();
        CLIENT_RECEIVE_REMOVE_AMETHYST = new ClientReceiveRemoveAmethyst();
    }

    public static void createParticle(ClientLevel level, BlockPos pos) {
        SimpleParticleType particleType = CharmParticles.APPLY_PARTICLE;

        float[] col = DyeColor.PURPLE.getTextureDiffuseColors();
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + 0.5D;
        double z = (double) pos.getZ() + 0.5D;

        level.addAlwaysVisibleParticle(particleType, x, y, z, col[0], col[1], col[2]);
    }
}
