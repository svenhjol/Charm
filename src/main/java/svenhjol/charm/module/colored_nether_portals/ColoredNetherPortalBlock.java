package svenhjol.charm.module.colored_nether_portals;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.block.ICharmBlock;
import svenhjol.charm.init.CharmParticles;
import svenhjol.charm.loader.CharmModule;

import java.util.Random;

public class ColoredNetherPortalBlock extends NetherPortalBlock implements ICharmBlock {
    private final CharmModule module;
    private final DyeColor color;

    public ColoredNetherPortalBlock(CharmModule module, DyeColor color) {
        super(Properties.copy(Blocks.NETHER_PORTAL));
        this.module = module;
        this.color = color;
        this.register(module, color.getSerializedName() + "_nether_portal");
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }

    @Override
    public void createBlockItem(ResourceLocation id, Item.Properties properties) {
        // don't
    }

    public DyeColor getColor() {
        return color;
    }

    /**
     * Override the default nether portal block particles so that
     * we can add our own colored particles.
     * 
     * Some copypasta from {@link NetherPortalBlock#animateTick}
     */
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, Random random) {
        if (random.nextInt(100) == 0) {
            level.playLocalSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.PORTAL_AMBIENT, SoundSource.BLOCKS, 0.5f, random.nextFloat() * 0.4f + 0.8f, false);
        }

        for (int i = 0; i < 4; ++i) {
            var d = (double)pos.getX() + random.nextDouble();
            var e = (double)pos.getY() + random.nextDouble();
            var f = (double)pos.getZ() + random.nextDouble();
            var g = ((double)random.nextFloat() - 0.5) * 0.5;
            var j = ((double)random.nextFloat() - 0.5) * 0.5;
            var k = random.nextInt(2) * 2 - 1;
            var color = getColor().getFireworkColor();

            if (level.getBlockState(pos.west()).is(this) || level.getBlockState(pos.east()).is(this)) {
                f = (double)pos.getZ() + 0.5 + 0.25 * (double)k;
                j = random.nextFloat() * 2.0f * (float)k;
            } else {
                d = (double)pos.getX() + 0.5 + 0.25 * (double)k;
                g = random.nextFloat() * 2.0f * (float)k;
            }

            level.addParticle(CharmParticles.COLORED_PORTAL_PARTICLE, d, e, f, g, j, color);
        }
    }
}
