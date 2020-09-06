package svenhjol.charm.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.block.VariantLadderBlock;
import svenhjol.meson.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IVariantMaterial;
import svenhjol.meson.enums.VanillaVariantMaterial;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(description = "Ladders available in all types of vanilla wood.")
public class VariantLadders extends MesonModule {
    public static final Map<IVariantMaterial, VariantLadderBlock> LADDER_BLOCKS = new HashMap<>();

    public static boolean isEnabled;

    @Override
    public void init() {
        VanillaVariantMaterial.getTypes().forEach(type -> {
            LADDER_BLOCKS.put(type, new VariantLadderBlock(this, type));
        });

        isEnabled = this.enabled;
    }

    @Override
    public void afterInitClient() {
        LADDER_BLOCKS.values().forEach(ladder -> {
            RenderLayersAccessor.getBlocks().put(ladder, RenderLayer.getCutout());
        });
    }

    public static boolean canEnterTrapdoor(World world, BlockPos pos, BlockState state) {
        if (isEnabled && state.get(TrapdoorBlock.OPEN)) {
            BlockState down = world.getBlockState(pos.down());
            return LADDER_BLOCKS.values().stream().anyMatch(b -> b == down.getBlock()) && down.get(LadderBlock.FACING) == state.get(TrapdoorBlock.FACING);
        }

        return false;
    }
}
