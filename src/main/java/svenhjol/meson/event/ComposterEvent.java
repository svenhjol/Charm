package svenhjol.meson.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.eventbus.api.Event;

public class ComposterEvent extends Event {
    public static class Output extends ComposterEvent {
        private World world;
        private BlockPos pos;
        private PlayerEntity player;

        public Output(World world, BlockPos pos, PlayerEntity player) {
            this.world = world;
            this.pos = pos;
            this.player = player;
        }

        public World getWorld() {
            return world;
        }

        public BlockPos getPos() {
            return pos;
        }

        public PlayerEntity getPlayer() {
            return player;
        }
    }
}
