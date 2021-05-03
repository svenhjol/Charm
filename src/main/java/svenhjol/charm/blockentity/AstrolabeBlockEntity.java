package svenhjol.charm.blockentity;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.Vibration;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import svenhjol.charm.base.helper.DimensionHelper;
import svenhjol.charm.module.Astrolabes;

import java.util.List;
import java.util.stream.Stream;

public class AstrolabeBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    public static final String DIMENSION_NBT = "Dimension";
    public static final String POSITION_NBT = "Position";

    public RegistryKey<World> dimension = World.OVERWORLD;
    public BlockPos position = BlockPos.ORIGIN;

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        this.dimension = DimensionHelper.decodeDimension(nbt.get(DIMENSION_NBT)).orElse(World.OVERWORLD);
        this.position = BlockPos.fromLong(nbt.getLong(POSITION_NBT));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        DimensionHelper.encodeDimension(this.dimension, el -> {
            nbt.put(DIMENSION_NBT, el);
        });

        nbt.putLong(POSITION_NBT, this.position.asLong());
        return nbt;
    }

    public AstrolabeBlockEntity(BlockPos position, BlockState state) {
        super(Astrolabes.BLOCK_ENTITY, position, state);
    }

    @Override
    public void fromClientTag(NbtCompound nbt) {
        readNbt(nbt);
    }

    @Override
    public NbtCompound toClientTag(NbtCompound nbt) {
        return writeNbt(nbt);
    }

    public static <T extends AstrolabeBlockEntity> void tick(World world, BlockPos pos, BlockState state, T astrolabe) {
        if (world == null || world.getTime() % 100 != 0)
            return;

        if (!world.isClient && astrolabe.position != BlockPos.ORIGIN) {
            BlockPos position = Astrolabes.getDimensionPosition(world, astrolabe.position, astrolabe.dimension);
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            data.writeBoolean(false); // don't play sound
            data.writeLongArray(Stream.of(position).map(BlockPos::asLong).mapToLong(Long::longValue).toArray());

            Box bb = (new Box(pos)).expand(16);
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, bb);
            list.forEach(player -> {
                ((ServerWorld)world).sendVibrationPacket(new Vibration(pos, new BlockPositionSource(position), 10));
                ServerPlayNetworking.send((ServerPlayerEntity)player, Astrolabes.MSG_CLIENT_SHOW_AXIS_PARTICLES, data);
            });
        }
    }
}
