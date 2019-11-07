package svenhjol.charm.brewing.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import svenhjol.meson.iface.IMesonMessage;

import java.util.function.Supplier;

public class ClientCakeAction implements IMesonMessage
{
    private BlockPos pos;

    public ClientCakeAction(BlockPos pos)
    {
        this.pos = pos;
    }

    public static void encode(ClientCakeAction msg, PacketBuffer buf)
    {
        buf.writeLong(msg.pos.toLong());
    }

    public static ClientCakeAction decode(PacketBuffer buf)
    {
        return new ClientCakeAction(BlockPos.fromLong(buf.readLong()));
    }

    public static class Handler
    {
        public static void handle(final ClientCakeAction msg, Supplier <NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                World world = Minecraft.getInstance().world;
                world.playSound(null, msg.pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.BLOCKS, 0.5F, 1.1F);
                for (int i = 0; i < 8; ++i) {
                    double d0 = world.rand.nextGaussian() * 0.02D;
                    double d1 = world.rand.nextGaussian() * 0.02D;
                    double d2 = world.rand.nextGaussian() * 0.02D;
                    double dx = (float)msg.pos.getX() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                    double dy = (float)msg.pos.getY() + 0.65f;
                    double dz = (float)msg.pos.getZ() + MathHelper.clamp(world.rand.nextFloat(), 0.25f, 0.75f);
                    world.addParticle(ParticleTypes.WITCH, dx, dy, dz, d0, d1, d2);
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
