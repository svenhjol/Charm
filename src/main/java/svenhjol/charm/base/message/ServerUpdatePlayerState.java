package svenhjol.charm.base.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent.Context;
import svenhjol.charm.Charm;
import svenhjol.meson.Meson;
import svenhjol.meson.iface.IMesonMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * Server assembles list of state for client, like inside structures, is day or night...
 */
@SuppressWarnings("unused")
public class ServerUpdatePlayerState implements IMesonMessage {
    public static List<BiConsumer<Context, CompoundNBT>> runOnUpdate = new ArrayList<>();

    public ServerUpdatePlayerState() {
    }

    @SuppressWarnings("EmptyMethod")
    public static void encode(ServerUpdatePlayerState msg, PacketBuffer buf) {
    }

    public static ServerUpdatePlayerState decode(PacketBuffer buf) {
        return new ServerUpdatePlayerState();
    }

    public static class Handler {
        public static void handle(final ServerUpdatePlayerState msg, Supplier<Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Context context = ctx.get();
                ServerPlayerEntity player = context.getSender();
                if (player == null) return;

                ServerWorld world = player.getServerWorld();
                BlockPos pos = player.getPosition();

                final long dayTime = world.getDayTime() % 24000;

                CompoundNBT nbt = new CompoundNBT();
                nbt.putBoolean("mineshaft", Feature.MINESHAFT.isPositionInsideStructure(world, pos));
                nbt.putBoolean("stronghold", Feature.STRONGHOLD.isPositionInsideStructure(world, pos));
                nbt.putBoolean("fortress", Feature.NETHER_BRIDGE.isPositionInsideStructure(world, pos));
                nbt.putBoolean("shipwreck", Feature.SHIPWRECK.isPositionInsideStructure(world, pos));
                nbt.putBoolean("village", Feature.VILLAGE.isPositionInsideStructure(world, pos));
                nbt.putBoolean("day", dayTime > 0 && dayTime < 12700);

                if (Charm.quarkCompat != null && Meson.isModuleEnabled(new ResourceLocation("quark:big_dungeons"))) {
                    nbt.putBoolean("big_dungeon", Charm.quarkCompat.isInsideBigDungeon(world, pos));
                }

                // update subscribed mods
                runOnUpdate.forEach(action -> action.accept(context, nbt));

                Meson.getInstance(Charm.MOD_ID).getPacketHandler().sendTo(new ClientUpdatePlayerState(nbt), player);
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
