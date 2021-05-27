package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.PlayerHelper;
import svenhjol.charm.base.helper.RegistryHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.StorageCrateBlock;
import svenhjol.charm.blockentity.StorageCrateBlockEntity;
import svenhjol.charm.client.StorageCratesClient;
import svenhjol.charm.enums.CharmWoodMaterial;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, priority = 10, client = StorageCratesClient.class)
public class StorageCrates extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "storage_crate");
    public static final Identifier MSG_CLIENT_UPDATED_CRATE = new Identifier(Charm.MOD_ID, "client_interacted_with_crate");
    public static Map<IVariantMaterial, StorageCrateBlock> STORAGE_CRATE_BLOCKS = new HashMap<>();
    public static BlockEntityType<StorageCrateBlockEntity> BLOCK_ENTITY;

    @Config(name = "Maximum stacks", description = "Number of stacks of a single item or block that a storage crate will hold.")
    public static int maximumStacks = 54;

    @Override
    public void register() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
        BLOCK_ENTITY = RegistryHandler.blockEntity(ID, StorageCrateBlockEntity::new);

        VanillaVariantMaterial.getTypes().forEach(material -> {
            registerStorageCrate(this, material);
        });

        for (CharmWoodMaterial material : CharmWoodMaterial.values()) {
            registerStorageCrate(this, material);
        }
    }

    public static StorageCrateBlock registerStorageCrate(CharmModule module, IVariantMaterial material) {
        StorageCrateBlock crate = new StorageCrateBlock(module, material);
        STORAGE_CRATE_BLOCKS.put(material, crate);
        RegistryHelper.addBlocksToBlockEntity(BLOCK_ENTITY, crate);
        return crate;
    }

    private ActionResult handleUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        if (!(world.getBlockState(pos).getBlock() instanceof StorageCrateBlock))
            return ActionResult.PASS;

        ItemStack held = player.getStackInHand(hand);
        boolean isCreative = player.getAbilities().creativeMode;
        boolean isSneaking = player.isSneaking();

        BlockEntity blockEntity = world.getBlockEntity(hitResult.getBlockPos());
        if (blockEntity instanceof StorageCrateBlockEntity) {
            StorageCrateBlockEntity crate = (StorageCrateBlockEntity) blockEntity;

            if (!world.isClient) {

                if (!crate.isEmpty() && (isSneaking || held.isEmpty())) {
                    ItemStack stack = crate.takeStack();

                    if (!isCreative)
                        PlayerHelper.addOrDropStack(player, stack);

                } else if (crate.isEmpty() && isSneaking) {
                    return ActionResult.PASS;

                } else if (!held.isEmpty()) {

                    // slot doesn't matter here, we're just checking the item type
                    if (crate.isEmpty() || crate.canInsert(0, held, Direction.UP)) {
                        if (crate.isFull()) {
                            sendClientEffects(world, pos, ActionType.FILLED);

                        } else {

                            if (!isCreative)
                                player.setStackInHand(hand, crate.addStack(held));
//                            crate.setStack(0, held);
                        }
                    }
                }

                crate.sync();
            }

            return ActionResult.success(world.isClient);
        }

        return ActionResult.PASS;
    }

    public static void sendClientEffects(World world, BlockPos pos, ActionType actionType) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeEnumConstant(actionType);
        data.writeLong(pos.asLong());

        world.getNonSpectatingEntities(PlayerEntity.class, (new Box(pos)).expand(8.0D)).forEach(p
            -> ServerPlayNetworking.send((ServerPlayerEntity) p, MSG_CLIENT_UPDATED_CRATE, data));
    }

    public enum ActionType {
        ADDED,
        REMOVED,
        FILLED
    }
}
