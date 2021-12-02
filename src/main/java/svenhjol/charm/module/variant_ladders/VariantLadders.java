package svenhjol.charm.module.variant_ladders;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.IWoodMaterial;
import svenhjol.charm.enums.VanillaWoodMaterial;
import svenhjol.charm.helper.RecipeHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Ladders available in all types of vanilla wood.")
public class VariantLadders extends CharmModule {
    public static final Map<IWoodMaterial, VariantLadderBlock> LADDER_BLOCKS = new HashMap<>();

    @Override
    public void register() {
        VanillaWoodMaterial.getTypes().forEach(type -> registerLadder(this, type));
    }

    public static void registerLadder(CharmModule module, IWoodMaterial material) {
        VariantLadderBlock ladder = new VariantLadderBlock(module, material);
        LADDER_BLOCKS.put(material, ladder);
    }

    public static boolean canEnterTrapdoor(Level level, BlockPos pos, BlockState state) {
        if (Charm.LOADER.isEnabled(VariantLadders.class) && state.getValue(TrapDoorBlock.OPEN)) {
            BlockState below = level.getBlockState(pos.below());
            return LADDER_BLOCKS.values().stream().anyMatch(b -> b == below.getBlock()) && below.getValue(LadderBlock.FACING) == state.getValue(TrapDoorBlock.FACING);
        }

        return false;
    }

    @Override
    public void runWhenDisabled() {
        RecipeHelper.removeRecipe(new ResourceLocation(Charm.MOD_ID, "woodcutters/vanilla_ladder_from_planks"));
    }
}
