package svenhjol.charm.module.variant_ladders;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.helper.RecipeHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Ladders available in all types of vanilla wood.")
public class VariantLadders extends CharmModule {
    public static final Map<IVariantMaterial, VariantLadderBlock> LADDER_BLOCKS = new HashMap<>();

    public static boolean isEnabled = false;

    @Override
    public void register() {
        VanillaVariantMaterial.getTypes().forEach(type -> registerLadder(this, type));
        isEnabled = this.isEnabled();
    }

    public static VariantLadderBlock registerLadder(CharmModule module, IVariantMaterial material) {
        VariantLadderBlock ladder = new VariantLadderBlock(module, material);
        LADDER_BLOCKS.put(material, ladder);
        return ladder;
    }

    public static boolean canEnterTrapdoor(Level world, BlockPos pos, BlockState state) {
        if (isEnabled && state.getValue(TrapDoorBlock.OPEN)) {
            BlockState down = world.getBlockState(pos.below());
            return LADDER_BLOCKS.values().stream().anyMatch(b -> b == down.getBlock()) && down.getValue(LadderBlock.FACING) == state.getValue(TrapDoorBlock.FACING);
        }

        return false;
    }

    @Override
    public void runWhenDisabled() {
        RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "woodcutters/vanilla_ladder_from_planks"));
    }
}
