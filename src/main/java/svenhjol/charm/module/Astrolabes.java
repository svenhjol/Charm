package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.poi.PointOfInterestType;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.WorldHelper;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.AstrolabeBlock;
import svenhjol.charm.blockentity.AstrolabeBlockEntity;
import svenhjol.charm.client.AstrolabesClient;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.item.AstrolabeBlockItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Module(mod = Charm.MOD_ID, client = AstrolabesClient.class)
public class Astrolabes extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "astrolabe");
    public static final Identifier MSG_CLIENT_SHOW_AXIS_PARTICLES = new Identifier(Charm.MOD_ID, "client_show_axis_particles");

    public static AstrolabeBlockItem ASTROLABE_OLD;
    public static AstrolabeBlock ASTROLABE;
    public static BlockEntityType<AstrolabeBlockEntity> BLOCK_ENTITY;
    public static PointOfInterestType POIT;

    @Override
    public void register() {
        ASTROLABE = new AstrolabeBlock(this);
//        ASTROLABE_OLD = new AstrolabeItem(this);
        BLOCK_ENTITY = RegistryHandler.blockEntity(ID, AstrolabeBlockEntity::new, ASTROLABE);

        POIT = WorldHelper.addPointOfInterestType(ID, ASTROLABE, 0);

        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    private void handlePlayerTick(PlayerEntity player) {
        if (player.world.isClient || player.world.getTime() % 100 != 0)
            return;

        List<BlockPos> positions = new ArrayList<>();

        for (Hand hand : Hand.values()) {
            ItemStack held = player.getStackInHand(hand);
            if (!(held.getItem() instanceof AstrolabeBlockItem))
                continue;

            Optional<RegistryKey<World>> dimension = AstrolabeBlockItem.getDimension(held);
            Optional<BlockPos> position = AstrolabeBlockItem.getPosition(held);

            if (!dimension.isPresent() || !position.isPresent())
                continue;

            RegistryKey<World> dim = dimension.get();
            BlockPos pos = position.get();
            ServerWorld world = ((ServerWorld) player.world).getServer().getWorld(dim);

            if (world == null || !world.getPointOfInterestStorage().hasTypeAt(Astrolabes.POIT, pos)) {
                held.getOrCreateTag().remove(AstrolabeBlockItem.POSITION_NBT);
                held.getOrCreateTag().remove(AstrolabeBlockItem.DIMENSION_NBT);
                continue;
            }

            positions.add(getDimensionPosition(player.world, pos, dim));
        }

        if (!positions.isEmpty()) {
            PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
            data.writeLongArray(positions.stream().map(BlockPos::asLong).mapToLong(Long::longValue).toArray());
            ServerPlayNetworking.send((ServerPlayerEntity) player, MSG_CLIENT_SHOW_AXIS_PARTICLES, data);
        }
    }

    public static BlockPos.Mutable getDimensionPosition(World currentWorld, BlockPos astrolabePos, RegistryKey<World> astrolabeDimension) {
        BlockPos.Mutable position = astrolabePos.mutableCopy();

        if (astrolabeDimension != null) {
            // if the astrolabe was set in the nether and the user is not in the nether, multiply X and Z
            if (astrolabeDimension.equals(World.NETHER) && currentWorld.getRegistryKey() != World.NETHER) {
                int x = position.getX();
                int y = position.getY();
                int z = position.getZ();

                position.set(x * 8, y, z * 8);
            }

            // if the astrolabe was set outside the nether and the user is in the nether, divide X and Z
            if (!astrolabeDimension.equals(World.NETHER) && currentWorld.getRegistryKey() == World.NETHER) {
                int x = position.getX();
                int y = position.getY();
                int z = position.getZ();

                position.set(x / 8, y, z / 8);
            }
        }

        return position;
    }
}
