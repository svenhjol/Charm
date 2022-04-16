package svenhjol.charm.module.weathering_iron;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SuppressWarnings({"unchecked", "rawtypes"})
@CommonModule(mod = Charm.MOD_ID, description = "Iron rusts when in contact with water.")
public class WeatheringIron extends CharmModule {
    public static WeatheringIronBlock EXPOSED_IRON;
    public static WeatheringIronBlock WEATHERED_IRON;
    public static WeatheringIronBlock OXIDIZED_IRON;

    public static WeatheringIronBlock CUT_IRON;
    public static WeatheringIronBlock EXPOSED_CUT_IRON;
    public static WeatheringIronBlock WEATHERED_CUT_IRON;
    public static WeatheringIronBlock OXIDIZED_CUT_IRON;

    public static WeatheringIronSlabBlock CUT_IRON_SLAB;
    public static WeatheringIronSlabBlock EXPOSED_CUT_IRON_SLAB;
    public static WeatheringIronSlabBlock WEATHERED_CUT_IRON_SLAB;
    public static WeatheringIronSlabBlock OXIDIZED_CUT_IRON_SLAB;

    public static WeatheringIronStairBlock CUT_IRON_STAIRS;
    public static WeatheringIronStairBlock EXPOSED_CUT_IRON_STAIRS;
    public static WeatheringIronStairBlock WEATHERED_CUT_IRON_STAIRS;
    public static WeatheringIronStairBlock OXIDIZED_CUT_IRON_STAIRS;

    public static WaxedIronBlock WAXED_IRON_BLOCK;
    public static WaxedIronBlock WAXED_EXPOSED_IRON;
    public static WaxedIronBlock WAXED_WEATHERED_IRON;
    public static WaxedIronBlock WAXED_OXIDIZED_IRON;

    public static WaxedIronBlock WAXED_CUT_IRON;
    public static WaxedIronBlock WAXED_EXPOSED_CUT_IRON;
    public static WaxedIronBlock WAXED_WEATHERED_CUT_IRON;
    public static WaxedIronBlock WAXED_OXIDIZED_CUT_IRON;

    public static WaxedIronSlabBlock WAXED_CUT_IRON_SLAB;
    public static WaxedIronSlabBlock WAXED_EXPOSED_CUT_IRON_SLAB;
    public static WaxedIronSlabBlock WAXED_WEATHERED_CUT_IRON_SLAB;
    public static WaxedIronSlabBlock WAXED_OXIDIZED_CUT_IRON_SLAB;

    public static WaxedIronStairBlock WAXED_CUT_IRON_STAIRS;
    public static WaxedIronStairBlock WAXED_EXPOSED_CUT_IRON_STAIRS;
    public static WaxedIronStairBlock WAXED_WEATHERED_CUT_IRON_STAIRS;
    public static WaxedIronStairBlock WAXED_OXIDIZED_CUT_IRON_STAIRS;

    public static List<Block> WEATHERING_ORDER = new ArrayList<>();
    public static BiMap<Block, Block> WAXABLES = HashBiMap.create();

    @Config(name = "Faces increase weathering", description = "The chance of weathering increases according to the number of block faces touching water.")
    public static boolean facesIncreaseWeathering = true;

    @Config(name = "Face multiplier", description = "When more than one block face is touching water, weathering chance increases by this amount per face.")
    public static double faceMultiplier = 0.008D;

    @Config(name = "Weathering chance", description = "Chance (out of 1.0) of a block being considered for weathering.")
    public static double chance = 0.006D;

    @Config(name = "Bubble column chance multiplier", description = "When a block is above a bubble column, weathering chance is multiplied by this value.")
    public static double bubbleColumnMultiplier = 4.0D;

    @Override
    public void register() {

        // Register weathering blocks.
        EXPOSED_IRON = new WeatheringIronBlock(this, "exposed_iron");
        WEATHERED_IRON = new WeatheringIronBlock(this, "weathered_iron");
        OXIDIZED_IRON = new WeatheringIronBlock(this, "oxidized_iron").fullyOxidised();

        CUT_IRON = new WeatheringIronBlock(this, "cut_iron").noOxidisation();
        EXPOSED_CUT_IRON = new WeatheringIronBlock(this, "exposed_cut_iron");
        WEATHERED_CUT_IRON = new WeatheringIronBlock(this, "weathered_cut_iron");
        OXIDIZED_CUT_IRON = new WeatheringIronBlock(this, "oxidized_cut_iron").fullyOxidised();

        CUT_IRON_SLAB = new WeatheringIronSlabBlock(this, "cut_iron_slab", CUT_IRON).noOxidisation();
        EXPOSED_CUT_IRON_SLAB = new WeatheringIronSlabBlock(this, "exposed_cut_iron_slab", EXPOSED_CUT_IRON);
        WEATHERED_CUT_IRON_SLAB = new WeatheringIronSlabBlock(this, "weathered_cut_iron_slab", WEATHERED_CUT_IRON);
        OXIDIZED_CUT_IRON_SLAB = new WeatheringIronSlabBlock(this, "oxidized_cut_iron_slab", OXIDIZED_CUT_IRON).fullyOxidised();

        CUT_IRON_STAIRS = new WeatheringIronStairBlock(this, "cut_iron_stairs", CUT_IRON).noOxidisation();
        EXPOSED_CUT_IRON_STAIRS = new WeatheringIronStairBlock(this, "exposed_cut_iron_stairs", EXPOSED_IRON);
        WEATHERED_CUT_IRON_STAIRS = new WeatheringIronStairBlock(this, "weathered_cut_iron_stairs", WEATHERED_CUT_IRON);
        OXIDIZED_CUT_IRON_STAIRS = new WeatheringIronStairBlock(this, "oxidized_cut_iron_stairs", OXIDIZED_CUT_IRON).fullyOxidised();


        // Register waxed blocks.
        WAXED_IRON_BLOCK = new WaxedIronBlock(this, "waxed_iron_block", Blocks.IRON_BLOCK);
        WAXED_EXPOSED_IRON = new WaxedIronBlock(this, "waxed_exposed_iron", EXPOSED_IRON);
        WAXED_WEATHERED_IRON = new WaxedIronBlock(this, "waxed_weathered_iron", WEATHERED_IRON);
        WAXED_OXIDIZED_IRON = new WaxedIronBlock(this, "waxed_oxidized_iron", OXIDIZED_IRON);

        WAXED_CUT_IRON = new WaxedIronBlock(this, "waxed_cut_iron", Blocks.IRON_BLOCK);
        WAXED_EXPOSED_CUT_IRON = new WaxedIronBlock(this, "waxed_exposed_cut_iron", EXPOSED_IRON);
        WAXED_WEATHERED_CUT_IRON = new WaxedIronBlock(this, "waxed_weathered_cut_iron", WEATHERED_IRON);
        WAXED_OXIDIZED_CUT_IRON = new WaxedIronBlock(this, "waxed_oxidized_cut_iron", OXIDIZED_IRON);

        WAXED_CUT_IRON_SLAB = new WaxedIronSlabBlock(this, "waxed_cut_iron_slab", Blocks.IRON_BLOCK);
        WAXED_EXPOSED_CUT_IRON_SLAB = new WaxedIronSlabBlock(this, "waxed_exposed_cut_iron_slab", EXPOSED_IRON);
        WAXED_WEATHERED_CUT_IRON_SLAB = new WaxedIronSlabBlock(this, "waxed_weathered_cut_iron_slab", WEATHERED_IRON);
        WAXED_OXIDIZED_CUT_IRON_SLAB = new WaxedIronSlabBlock(this, "waxed_oxidized_cut_iron_slab", OXIDIZED_IRON);

        WAXED_CUT_IRON_STAIRS = new WaxedIronStairBlock(this, "waxed_cut_iron_stairs", Blocks.IRON_BLOCK);
        WAXED_EXPOSED_CUT_IRON_STAIRS = new WaxedIronStairBlock(this, "waxed_exposed_cut_iron_stairs", EXPOSED_IRON);
        WAXED_WEATHERED_CUT_IRON_STAIRS = new WaxedIronStairBlock(this, "waxed_weathered_cut_iron_stairs", WEATHERED_IRON);
        WAXED_OXIDIZED_CUT_IRON_STAIRS = new WaxedIronStairBlock(this, "waxed_oxidized_cut_iron_stairs", OXIDIZED_IRON);


        // Add weathering orders for different block families.
        WEATHERING_ORDER.add(Blocks.IRON_BLOCK);
        WEATHERING_ORDER.add(EXPOSED_IRON);
        WEATHERING_ORDER.add(WEATHERED_IRON);
        WEATHERING_ORDER.add(OXIDIZED_IRON);

        WEATHERING_ORDER.add(CUT_IRON);
        WEATHERING_ORDER.add(EXPOSED_CUT_IRON);
        WEATHERING_ORDER.add(WEATHERED_CUT_IRON);
        WEATHERING_ORDER.add(OXIDIZED_CUT_IRON);

        WEATHERING_ORDER.add(CUT_IRON_SLAB);
        WEATHERING_ORDER.add(EXPOSED_CUT_IRON_SLAB);
        WEATHERING_ORDER.add(WEATHERED_CUT_IRON_SLAB);
        WEATHERING_ORDER.add(OXIDIZED_CUT_IRON_SLAB);

        WEATHERING_ORDER.add(CUT_IRON_STAIRS);
        WEATHERING_ORDER.add(EXPOSED_CUT_IRON_STAIRS);
        WEATHERING_ORDER.add(WEATHERED_CUT_IRON_STAIRS);
        WEATHERING_ORDER.add(OXIDIZED_CUT_IRON_STAIRS);


        // Add waxable block maps.
        WAXABLES.put(Blocks.IRON_BLOCK, WAXED_IRON_BLOCK);
        WAXABLES.put(EXPOSED_IRON, WAXED_EXPOSED_IRON);
        WAXABLES.put(WEATHERED_IRON, WAXED_WEATHERED_IRON);
        WAXABLES.put(OXIDIZED_IRON, WAXED_OXIDIZED_IRON);

        WAXABLES.put(CUT_IRON, WAXED_CUT_IRON);
        WAXABLES.put(EXPOSED_CUT_IRON, WAXED_EXPOSED_CUT_IRON);
        WAXABLES.put(WEATHERED_CUT_IRON, WAXED_WEATHERED_CUT_IRON);
        WAXABLES.put(OXIDIZED_CUT_IRON, WAXED_OXIDIZED_CUT_IRON);

        WAXABLES.put(CUT_IRON_SLAB, WAXED_CUT_IRON_SLAB);
        WAXABLES.put(EXPOSED_CUT_IRON_SLAB, WAXED_EXPOSED_CUT_IRON_SLAB);
        WAXABLES.put(WEATHERED_CUT_IRON_SLAB, WAXED_WEATHERED_CUT_IRON_SLAB);
        WAXABLES.put(OXIDIZED_CUT_IRON_SLAB, WAXED_OXIDIZED_CUT_IRON_SLAB);

        WAXABLES.put(CUT_IRON_STAIRS, WAXED_CUT_IRON_STAIRS);
        WAXABLES.put(EXPOSED_CUT_IRON_STAIRS, WAXED_EXPOSED_CUT_IRON_STAIRS);
        WAXABLES.put(WEATHERED_CUT_IRON_STAIRS, WAXED_WEATHERED_CUT_IRON_STAIRS);
        WAXABLES.put(OXIDIZED_CUT_IRON_STAIRS, WAXED_OXIDIZED_CUT_IRON_STAIRS);
    }

    @Override
    public void runWhenEnabled() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
    }

    private InteractionResult handleUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        var held = player.getItemInHand(hand);
        if (held.isEmpty()) return InteractionResult.PASS;

        var item = held.getItem();
        var pos = hitResult.getBlockPos();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        Optional<BlockState> newState = Optional.empty();

        if (item instanceof AxeItem) {
            if (block instanceof IWeatherableIron iron) {
                // Scrape rust
                if (WEATHERING_ORDER.contains(iron)) {
                    if (iron.hasAnyOxidation()) {
                        var i = WEATHERING_ORDER.indexOf(block);
                        level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
                        level.levelEvent(player, 3005, pos, 0);
                        newState = Optional.of(WEATHERING_ORDER.get(i - 1).defaultBlockState());
                    }
                }
            } else if (block instanceof IWaxableIron iron) {
                // Wax off
                var inverse = WAXABLES.inverse();
                if (inverse.containsKey(iron)) {
                    level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.levelEvent(player, 3004, pos, 0);
                    newState = Optional.of(inverse.get(iron).defaultBlockState());
                }
            }
        } else if (item instanceof HoneycombItem) {
            // Wax on
            if (WAXABLES.containsKey(block)) {
                level.levelEvent(player, 3003, pos, 0);
                newState = Optional.of(WAXABLES.get(block).defaultBlockState());
            }
        }

        if (newState.isPresent()) {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, held);
            }
            var getState = newState.get();
            var properties = state.getProperties();
            for (Property property : properties) {
                getState = getState.setValue(property, state.getValue(property));
            }

            level.setBlock(pos, getState, 11);
            if (held.isDamageableItem()) {
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return InteractionResult.PASS;
    }

    /**
     * @see ChangeOverTimeBlock#onRandomTick
     */
    public static void handleRandomTick(ServerLevel level, BlockPos pos, BlockState state, Random random) {
        var d = random.nextDouble();
        if (!(state.getBlock() instanceof IWeatherableIron iron)) {
            return;
        }

        if (WEATHERING_ORDER.contains(iron)) {
            var chance = multiplyByTouchingFaces(level, pos);
            if (chance == 0) return; // no point continuing if no faces touching

            if (hasBubbleColumn(level, pos)) {
                chance *= bubbleColumnMultiplier;
            }

            if (d <= chance) {
                var i = WEATHERING_ORDER.indexOf(iron);
                if (!iron.isFullyOxidised()) {
                    var properties = state.getProperties();
                    var nextBlock = WEATHERING_ORDER.get(i + 1);
                    var nextState = nextBlock.defaultBlockState();
                    for (Property property : properties) {
                        nextState = nextState.setValue(property, state.getValue(property));
                    }

                    LogHelper.debug(WeatheringIron.class, "Weathering block to " + nextBlock + ", d = " + d + ", chance = " + chance, ", pos = " + pos);
                    level.setBlockAndUpdate(pos, nextState);
                }
            }
        }
    }

    public static int facesTouchingWater(ServerLevel level, BlockPos pos) {
        int faces = 0;

        for (Direction direction : Direction.values()) {
            if (level.isWaterAt(pos.relative(direction))) {
                faces++;
            }
        }

        return faces;
    }

    public static double multiplyByTouchingFaces(ServerLevel level, BlockPos pos) {
        int faces = facesTouchingWater(level, pos);

        if (facesIncreaseWeathering && faces > 1) {
            double newChance = chance + (faceMultiplier * faces);
            LogHelper.debug(WeatheringIron.class, "Face weathering, faces = " + faces + ", chance now = " + newChance + ", pos = " + pos);
            return newChance;
        } else if (faces > 0) {
            return chance;
        }

        return 0;
    }

    public static boolean hasBubbleColumn(ServerLevel level, BlockPos pos) {
        boolean has = level.getBlockState(pos.below()).getBlock() == Blocks.BUBBLE_COLUMN;
        if (has) {
            LogHelper.debug(WeatheringIron.class, "Bubble column weathering, pos = " + pos);
        }
        return has;
    }

    @Nullable
    public static ItemStack tryDispense(BlockSource source, ItemStack stack) {
        var pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        var level = source.getLevel();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (WeatheringIron.WAXABLES.containsKey(block)) {
            var newState = WeatheringIron.WAXABLES.get(block).defaultBlockState();
            level.setBlockAndUpdate(pos, newState);
            level.levelEvent(3003, pos, 0);
            stack.shrink(1);
            return stack;
        }

        return null;
    }
}
