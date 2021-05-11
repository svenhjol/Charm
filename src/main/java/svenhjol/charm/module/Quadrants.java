package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.event.EntityEquipCallback;
import svenhjol.charm.item.QuadrantItem;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

@Module(mod = Charm.MOD_ID, description = "A tool that rotates blocks. When held in offhand it locks block orientation when placing.")
public class Quadrants extends CharmModule {
    public static QuadrantItem QUADRANT;
    public static Map<UUID, Direction> lockedDirection = new HashMap<>();
    public static final Identifier TRIGGER_HELD_TO_ALIGN = new Identifier(Charm.MOD_ID, "held_to_align");
    public static final Identifier TRIGGER_ROTATED_BLOCK = new Identifier(Charm.MOD_ID, "rotated_block");

    @Override
    public void register() {
        QUADRANT = new QuadrantItem(this);
    }

    @Override
    public void init() {
        EntityEquipCallback.EVENT.register(this::handleEntityEquip);
        UseBlockCallback.EVENT.register(this::handleUseBlock);
    }

    public static BlockState getRotatedBlockState(BlockState state, ItemPlacementContext context) {
        if (!ModuleHandler.enabled(Quadrants.class))
            return state;

        PlayerEntity player = context.getPlayer();
        if (player == null || !lockedDirection.containsKey(player.getUuid()))
            return state;

        if (state == null || state.getProperties() == null)
            return null;

        Direction direction = lockedDirection.get(player.getUuid());
        Collection<Property<?>> properties = state.getProperties();
        Block block = state.getBlock();

        if (properties.contains(Properties.FACING))
            return state.with(Properties.FACING, direction);

        if (properties.contains(Properties.HORIZONTAL_FACING)) {
            if (block instanceof StairsBlock) {
                return state.with(Properties.HORIZONTAL_FACING, direction.getOpposite());
            } else {
                return state.with(Properties.HORIZONTAL_FACING, direction);
            }
        }

        if (properties.contains(Properties.AXIS))
            return state.with(Properties.AXIS, direction.getAxis());

        return state;
    }

    private ActionResult handleUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        ItemStack held = player.getStackInHand(hand);

        if (!held.isOf(QUADRANT))
            return ActionResult.PASS;

        if (player.getItemCooldownManager().isCoolingDown(QUADRANT))
            return ActionResult.SUCCESS;

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = world.getBlockState(pos);
        Collection<Property<?>> properties = state.getProperties();
        Property<Direction> prop = null;

        if (properties.contains(Properties.FACING)) {
            prop = Properties.FACING;
        } else if (properties.contains(Properties.HORIZONTAL_FACING)) {
            prop = Properties.HORIZONTAL_FACING;
        }

        List<Predicate<BlockState>> exceptions = Arrays.asList(
            s -> s.getBlock() instanceof DoorBlock,
            s -> s.getBlock() instanceof TallPlantBlock,
            s -> s.getBlock() instanceof TallFlowerBlock,
            s -> s.getBlock() instanceof ChestBlock && ChestBlock.getDoubleBlockType(s) != DoubleBlockProperties.Type.SINGLE,
            s -> s.getBlock() instanceof PistonBlock && state.get(PistonBlock.EXTENDED)
        );

        if (prop != null && exceptions.stream().noneMatch(exception -> exception.test(state))) {
            Direction d = state.get(prop);
            if (d.getAxis() == Direction.Axis.X || d.getAxis() == Direction.Axis.Z) {
                world.setBlockState(pos, state.with(prop, d.rotateYClockwise()), 3);
                world.getBlockTickScheduler().schedule(pos, state.getBlock(), 4);
                world.playSound(null, pos, CharmSounds.QUADRANT, SoundCategory.BLOCKS, 0.35F + (0.25F * world.random.nextFloat()), 0.8F + (0.4F * world.random.nextFloat()));

                if (!world.isClient && state.getBlock() instanceof AbstractChestBlock)
                    triggerRotatedBlock((ServerPlayerEntity) player);

                // damage the quadrant a bit
                held.damage(1, player, p -> p.sendToolBreakStatus(hand));
                player.getItemCooldownManager().set(QUADRANT, 5);
            }
        }

        return ActionResult.SUCCESS;
    }

    private void handleEntityEquip(LivingEntity entity, EquipmentSlot slot, @Nullable ItemStack from, @Nullable ItemStack to) {
        if (entity.world.isClient)
            return;

        if (slot == EquipmentSlot.OFFHAND
            && to != null
            && to.isOf(QUADRANT)
        ) {
            Direction direction = entity.getHorizontalFacing();
            lockedDirection.put(entity.getUuid(), direction);
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                player.world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.PLAYERS, 1.15F, 0.9F);
                player.sendMessage(new TranslatableText("gui.charm.quadrants.locked", direction.getName()), true);
                triggerHeldToAlign((ServerPlayerEntity) player);
            }
        }
        if (slot == EquipmentSlot.OFFHAND
            && from != null && from.isOf(QUADRANT)
            && to != null && !to.isOf(QUADRANT)
        ) {
            lockedDirection.remove(entity.getUuid());
            if (entity instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) entity;
                player.world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_SPYGLASS_STOP_USING, SoundCategory.PLAYERS, 1.2F, 0.9F);
                player.sendMessage(new TranslatableText("gui.charm.quadrants.unlocked"), true);
            }
        }
    }

    public static void triggerHeldToAlign(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_HELD_TO_ALIGN);
    }

    public static void triggerRotatedBlock(ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_ROTATED_BLOCK);
    }
}
