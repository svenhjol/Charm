package svenhjol.charm.client;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.item.AstrolabeItem;
import svenhjol.charm.module.Astrolabes;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class AstrolabesClient extends CharmClientModule {
    public AstrolabesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        FabricModelPredicateProviderRegistry.register(Astrolabes.ASTROLABE, new Identifier(Charm.MOD_ID, "aligned_x"), this::handleAlignX);
        FabricModelPredicateProviderRegistry.register(Astrolabes.ASTROLABE, new Identifier(Charm.MOD_ID, "aligned_y"), this::handleAlignY);
        FabricModelPredicateProviderRegistry.register(Astrolabes.ASTROLABE, new Identifier(Charm.MOD_ID, "aligned_z"), this::handleAlignZ);
    }

    private float handleAlignX(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
        return isAligned(stack, world, entity, pos
            -> pos.getZ() == entity.getBlockZ()) ? 1 : 0;
    }

    private float handleAlignY(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
        return isAligned(stack, world, entity, pos
            -> pos.getY() == entity.getBlockY()) ? 1 : 0;
    }

    private float handleAlignZ(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
        return isAligned(stack, world, entity, pos ->
            pos.getX() == entity.getBlockX()) ? 1 : 0;
    }

    private boolean isAligned(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, Predicate<BlockPos.Mutable> on) {
        if (entity == null || entity.world == null)
            return false;

        Identifier dimension = AstrolabeItem.getDimension(stack);
        BlockPos pos = AstrolabeItem.getPosition(stack);
        if (pos == null)
            return false;

        BlockPos.Mutable dimensionPos = Astrolabes.getDimensionPosition(entity.world, pos, dimension);
        return on.test(dimensionPos);
    }
}
