package svenhjol.charm.module.block_of_ender_pearls;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import svenhjol.charm.block.CharmBlock;
import svenhjol.charm.loader.CharmModule;

import java.util.Random;

public class EnderPearlBlock extends CharmBlock {
    public EnderPearlBlock(CharmModule module) {
        super(module, "ender_pearl_block", FabricBlockSettings
            .of(Material.GLASS)
            .sounds(SoundType.GLASS)
            .strength(2.0F)
        );
    }

    @Override
    public CreativeModeTab getItemGroup() {
        return CreativeModeTab.TAB_BUILDING_BLOCKS;
    }

    /**
     * Copypasta from MyceliumBlock
     */
    @Override
    @Environment(EnvType.CLIENT)
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        super.animateTick(state, level, pos, random);
        if (random.nextInt(10) == 0) {
            level.addParticle(ParticleTypes.PORTAL, (double)pos.getX() + random.nextDouble(), (double)pos.getY() + 1.1D, (double)pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }
}
