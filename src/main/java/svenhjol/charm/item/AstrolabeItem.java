package svenhjol.charm.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.DimensionHelper;
import svenhjol.charm.base.item.CharmItem;

import javax.annotation.Nullable;

public class AstrolabeItem extends CharmItem {
    public static final String DIMENSION_NBT = "Dimension";
    public static final String POSITION_NBT = "Position";

    public AstrolabeItem(CharmModule module) {
        super(module, "astrolabe", new FabricItemSettings()
            .group(ItemGroup.MISC)
            .maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack held = user.getStackInHand(hand);

        if (user.isSneaking()) {
            // write the dimension and position
            setDimension(held, DimensionHelper.getDimension(world));
            setPosition(held, user.getBlockPos());

            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
            return TypedActionResult.success(held);
        }

        return TypedActionResult.pass(held);
    }

    @Nullable
    public static Identifier getDimension(ItemStack astrolabe) {
        String dimension = astrolabe.getOrCreateTag().getString(DIMENSION_NBT);
        if (dimension.isEmpty())
            return null;

        return Identifier.tryParse(dimension);
    }

    @Nullable
    public static BlockPos getPosition(ItemStack astrolabe) {
        if (!astrolabe.getOrCreateTag().contains(POSITION_NBT))
            return null;

        long position = astrolabe.getOrCreateTag().getLong(POSITION_NBT);
        return BlockPos.fromLong(position);
    }

    public static void setDimension(ItemStack astrolabe, Identifier dimension) {
        astrolabe.getOrCreateTag().putString(DIMENSION_NBT, dimension.toString());
    }

    public static void setPosition(ItemStack astrolabe, BlockPos position) {
        astrolabe.getOrCreateTag().putLong(POSITION_NBT, position.asLong());
    }
}
