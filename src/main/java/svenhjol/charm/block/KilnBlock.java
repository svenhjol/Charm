package svenhjol.charm.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractFurnaceBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.ICharmBlock;
import svenhjol.charm.blockentity.KilnBlockEntity;

import javax.annotation.Nullable;
import java.util.Random;

public class KilnBlock extends AbstractFurnaceBlock implements ICharmBlock {
    protected CharmModule module;

    public KilnBlock(CharmModule module) {
        super(AbstractBlock.Settings
            .of(Material.STONE)
            .requiresTool()
            .strength(3.5F)
            .luminance(l -> l.get(Properties.LIT) ? 13 : 0));

        this.module = module;
        this.register(module, "kiln");
    }

    @Override
    protected void openScreen(World world, BlockPos pos, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof KilnBlockEntity) {
            player.openHandledScreen((NamedScreenHandlerFactory)blockEntity);
        }
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new KilnBlockEntity(pos, state);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(LIT)) {
            double x = pos.getX() + 0.5D;
            double y = pos.getY();
            double z = pos.getZ() + 0.5D;
            if (random.nextDouble() < 0.1D)
                world.playSound(x, y, z, SoundEvents.BLOCK_SMOKER_SMOKE, SoundCategory.BLOCKS, 1.0F, 1.0F, false);

            world.addParticle(ParticleTypes.SMOKE, x, y + 1.1D, z, 0.0D, 0.0D, 0.0D);
        }
    }
}
