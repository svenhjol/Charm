package svenhjol.charm.feature.cooking_pots.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.cooking_pots.CookingPotsClient;
import svenhjol.charm.feature.cooking_pots.common.CookingPotBlock;
import svenhjol.charm.feature.cooking_pots.common.Networking;

public final class Handlers extends FeatureHolder<CookingPotsClient> {
    public Handlers(CookingPotsClient feature) {
        super(feature);
    }

    public int handleBlockColor(BlockState state, BlockAndTintGetter level, BlockPos pos, int tintIndex) {
        if (tintIndex == 0) {
            return switch (state.getValue(CookingPotBlock.COOKING_STATUS)) {
                case EMPTY -> -1;
                case FILLED_WITH_WATER -> 0x0088cc;
                case HAS_SOME_FOOD -> 0x806030;
                case COOKED -> 0x502800;
            };
        }
        return -1;
    }

    public void handleAddedToCookingPot(Player player, Networking.S2CAddedToCookingPot packet) {
        var minecraft = Minecraft.getInstance();
        var level = minecraft.level;

        if (level != null) {
            var pos = packet.pos();
            var state = level.getBlockState(pos);
            createParticles(level, pos);
            level.updateNeighborsAt(pos, state.getBlock());
            level.setBlocksDirty(pos, state, state);
            level.sendBlockUpdated(pos, state, state, 2);
        }
    }

    public void createParticles(Level level, BlockPos pos) {
        var random = level.getRandom();
        for(int i = 0; i < 10; ++i) {
            var offsetX = random.nextGaussian() * 0.02d;
            var offsetY = random.nextGaussian() * 0.02d;
            var offsetZ = random.nextGaussian() * 0.02d;

            level.addParticle(ParticleTypes.SMOKE,
                pos.getX() + 0.13 + (0.73d * random.nextFloat()),
                pos.getY() + 0.8d + random.nextFloat() * 0.3d,
                pos.getZ() + 0.13d + (0.73d * random.nextFloat()),
                offsetX, offsetY, offsetZ);
        }
    }
}
