package svenhjol.charm.module.quadrants;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.EntityEquipEvent;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

@CommonModule(mod = Charm.MOD_ID, description = "A tool that rotates blocks. When held in offhand it locks block orientation when placing.")
public class Quadrants extends CharmModule {
    public static QuadrantItem QUADRANT;
    public static Map<UUID, Direction> lockedDirection = new HashMap<>();
    public static final ResourceLocation TRIGGER_HELD_TO_ALIGN = new ResourceLocation(Charm.MOD_ID, "held_to_align");
    public static final ResourceLocation TRIGGER_ROTATED_BLOCK = new ResourceLocation(Charm.MOD_ID, "rotated_block");

    @Override
    public void register() {
        QUADRANT = new QuadrantItem(this);
    }

    @Override
    public void runWhenEnabled() {
        EntityEquipEvent.EVENT.register(this::handleEntityEquip);
        UseBlockCallback.EVENT.register(this::handleUseBlock);
    }

    public static BlockState getRotatedBlockState(BlockState state, BlockPlaceContext context) {
        if (!Charm.LOADER.isEnabled(Quadrants.class))
            return state;

        Player player = context.getPlayer();
        if (player == null || !lockedDirection.containsKey(player.getUUID()))
            return state;

        if (state == null || state.getProperties() == null)
            return null;

        Direction direction = lockedDirection.get(player.getUUID());
        Collection<Property<?>> properties = state.getProperties();
        Block block = state.getBlock();
        boolean heldRotate = false;

        if (properties.contains(BlockStateProperties.FACING)) {
            state = state.setValue(BlockStateProperties.FACING, direction);
            heldRotate = true;
        } else if (properties.contains(BlockStateProperties.HORIZONTAL_FACING)) {
            if (block instanceof StairBlock) {
                state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite());
            } else {
                state = state.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
            }
            heldRotate = true;
        } else if (properties.contains(BlockStateProperties.AXIS)) {
            state = state.setValue(BlockStateProperties.AXIS, direction.getAxis());
            heldRotate = true;
        }

        if (!player.level.isClientSide && heldRotate)
            triggerHeldToAlign((ServerPlayer) player);

        return state;
    }

    private InteractionResult handleUseBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack held = player.getItemInHand(hand);

        if (!held.is(QUADRANT))
            return InteractionResult.PASS;

        if (player.getCooldowns().isOnCooldown(QUADRANT))
            return InteractionResult.SUCCESS;

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Collection<Property<?>> properties = state.getProperties();
        Property<Direction> prop = null;

        if (properties.contains(BlockStateProperties.FACING)) {
            prop = BlockStateProperties.FACING;
        } else if (properties.contains(BlockStateProperties.HORIZONTAL_FACING)) {
            prop = BlockStateProperties.HORIZONTAL_FACING;
        }

        List<Predicate<BlockState>> exceptions = Arrays.asList(
            s -> s.getBlock() instanceof DoorBlock,
            s -> s.getBlock() instanceof DoublePlantBlock,
            s -> s.getBlock() instanceof TallFlowerBlock,
            s -> s.getBlock() instanceof ChestBlock && ChestBlock.getBlockType(s) != DoubleBlockCombiner.BlockType.SINGLE,
            s -> s.getBlock() instanceof PistonBaseBlock && state.getValue(PistonBaseBlock.EXTENDED),
            s -> s.getBlock() instanceof BedBlock
        );

        if (prop != null && exceptions.stream().noneMatch(exception -> exception.test(state))) {
            Direction d = state.getValue(prop);
            if (d.getAxis() == Direction.Axis.X || d.getAxis() == Direction.Axis.Z) {
                world.setBlock(pos, state.setValue(prop, d.getClockWise()), 3);
                world.getBlockTicks().scheduleTick(pos, state.getBlock(), 4);
                world.playSound(null, pos, CharmSounds.QUADRANT, SoundSource.BLOCKS, 0.35F + (0.25F * world.random.nextFloat()), 0.8F + (0.4F * world.random.nextFloat()));

                if (!world.isClientSide && state.getBlock() instanceof ChestBlock)
                    triggerRotatedBlock((ServerPlayer) player);

                // damage the quadrant a bit
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
                player.getCooldowns().addCooldown(QUADRANT, 5);
            }
        }

        return InteractionResult.SUCCESS;
    }

    private void handleEntityEquip(LivingEntity entity, EquipmentSlot slot, @Nullable ItemStack from, @Nullable ItemStack to) {
        if (entity.level.isClientSide)
            return;

        if (slot == EquipmentSlot.OFFHAND
            && to != null
            && to.is(QUADRANT)
        ) {
            Direction direction = entity.getDirection();
            lockedDirection.put(entity.getUUID(), direction);
            if (entity instanceof Player) {
                Player player = (Player) entity;
                player.level.playSound(null, player.blockPosition(), SoundEvents.SPYGLASS_USE, SoundSource.PLAYERS, 1.15F, 0.9F);
                player.displayClientMessage(new TranslatableComponent("gui.charm.quadrants.locked", direction.getName()), true);
            }
        }
        if (slot == EquipmentSlot.OFFHAND
            && from != null && from.is(QUADRANT)
            && to != null && !to.is(QUADRANT)
        ) {
            lockedDirection.remove(entity.getUUID());
            if (entity instanceof Player) {
                Player player = (Player) entity;
                player.level.playSound(null, player.blockPosition(), SoundEvents.SPYGLASS_STOP_USING, SoundSource.PLAYERS, 1.2F, 0.9F);
                player.displayClientMessage(new TranslatableComponent("gui.charm.quadrants.unlocked"), true);
            }
        }
    }

    public static void triggerHeldToAlign(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_HELD_TO_ALIGN);
    }

    public static void triggerRotatedBlock(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_ROTATED_BLOCK);
    }
}
