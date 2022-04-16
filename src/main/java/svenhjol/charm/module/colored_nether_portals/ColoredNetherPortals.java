package svenhjol.charm.module.colored_nether_portals;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.WorldHelper;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, description = "Throw dye into a nether portal to change its color.")
public class ColoredNetherPortals extends CharmModule {
    public static Map<DyeColor, ColoredNetherPortalBlock> BLOCKS = new HashMap<>();
    public static SoundEvent PORTAL_CHANGE_COLOR_SOUND;

    @Override
    public void register() {
        for (DyeColor color : DyeColor.values()) {
            BLOCKS.put(color, new ColoredNetherPortalBlock(this, color));
        }

        PORTAL_CHANGE_COLOR_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "portal_change_color"));
    }

    @Override
    public void runWhenEnabled() {
        List<BlockState> states = new ArrayList<>();
        BLOCKS.values().forEach(b -> states.addAll(b.getStateDefinition().getPossibleStates()));
        WorldHelper.addBlockStatesToPointOfInterest(PoiType.NETHER_PORTAL, states);
    }

    public static void replacePortal(Level level, BlockPos pos, BlockState state, DyeColor color) {
        BlockPos newPos;
        BlockState newState;
        Direction.Axis axis;
        Direction dir1, dir2, opp1, opp2;
        HashSet<BlockPos> set = Sets.newHashSet();
        ArrayDeque<BlockPos> queue = Queues.newArrayDeque();

        axis = state.getValue(NetherPortalBlock.AXIS);
        newState = BLOCKS.get(color).defaultBlockState().setValue(NetherPortalBlock.AXIS, axis);

        switch (axis) {
            case X -> {
                dir1 = Direction.UP;
                dir2 = Direction.EAST;
            }
            case Y -> {
                dir1 = Direction.SOUTH;
                dir2 = Direction.EAST;
            }
            default -> {
                dir1 = Direction.UP;
                dir2 = Direction.SOUTH;
            }
        }

        opp1 = dir1.getOpposite();
        opp2 = dir2.getOpposite();
        queue.add(pos);

        while ((newPos = queue.poll()) != null) {
            BlockPos pos2;

            set.add(newPos);
            var state2 = level.getBlockState(newPos);
            if (state2 != state) continue;

            level.setBlock(newPos, newState, 18);

            if (!set.contains(pos2 = newPos.relative(dir1))) {
                queue.add(pos2);
            }
            if (!set.contains(pos2 = newPos.relative(opp1))) {
                queue.add(pos2);
            }
            if (!set.contains(pos2 = newPos.relative(dir2))) {
                queue.add(pos2);
            }
            if (!set.contains(pos2 = newPos.relative(opp2))) {
                queue.add(pos2);
            }
        }

        if (!level.isClientSide) {
            level.playSound(null, pos, ColoredNetherPortals.PORTAL_CHANGE_COLOR_SOUND, SoundSource.BLOCKS, 0.44F, new Random().nextFloat() * 0.4F + 0.8F);
        }
    }
}
