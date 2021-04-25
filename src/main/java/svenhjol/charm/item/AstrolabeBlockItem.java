package svenhjol.charm.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import svenhjol.charm.base.block.CharmBlockItem;
import svenhjol.charm.base.block.ICharmBlock;
import svenhjol.charm.base.helper.DimensionHelper;
import svenhjol.charm.module.Astrolabes;

import java.util.Optional;

public class AstrolabeBlockItem extends CharmBlockItem {
    public static final String DIMENSION_NBT = "Dimension";
    public static final String POSITION_NBT = "Position";

    public AstrolabeBlockItem(ICharmBlock block) {
        super(block, new FabricItemSettings()
            .group(ItemGroup.MISC)
            .maxCount(1));
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return stack.hasTag();
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        ItemStack stack = context.getStack();
        BlockState state = world.getBlockState(pos);

        if (state.getBlock() == Astrolabes.ASTROLABE) {
            // trying to link to another astrolabe
            AstrolabeBlockItem.setPosition(stack, pos);
            AstrolabeBlockItem.setDimension(stack, world.getRegistryKey());
            return ActionResult.success(world.isClient);
        }

        return super.useOnBlock(context);
    }

    public static Optional<RegistryKey<World>> getDimension(ItemStack astrolabe) {
        if (!astrolabe.getOrCreateTag().contains(DIMENSION_NBT))
            return Optional.empty();

        NbtElement dimension = astrolabe.getOrCreateTag().get(DIMENSION_NBT);
        return DimensionHelper.decodeDimension(dimension);
    }

    public static Optional<BlockPos> getPosition(ItemStack astrolabe) {
        if (!astrolabe.getOrCreateTag().contains(POSITION_NBT))
            return Optional.empty();

        long position = astrolabe.getOrCreateTag().getLong(POSITION_NBT);
        return Optional.of(BlockPos.fromLong(position));
    }

    public static void setDimension(ItemStack astrolabe, RegistryKey<World> worldKey) {
        DimensionHelper.encodeDimension(worldKey, el
            -> astrolabe.getOrCreateTag().put(DIMENSION_NBT, el));
    }

    public static void setPosition(ItemStack astrolabe, BlockPos position) {
        astrolabe.getOrCreateTag().putLong(POSITION_NBT, position.asLong());
    }
}
