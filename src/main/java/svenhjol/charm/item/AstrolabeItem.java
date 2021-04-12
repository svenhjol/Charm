package svenhjol.charm.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
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

        BlockPos position = getPosition(held);
        if (world.isClient && position != null) {
            createAxisEffect((ClientWorld)world, position, user);
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

    @Environment(EnvType.CLIENT)
    private void createAxisEffect(ClientWorld world, BlockPos pos, PlayerEntity player) {
        int dist = 32;

        boolean alignedX = false;
        boolean alignedY = false;
        boolean alignedZ = false;

        double px = Math.abs(pos.getX() - player.getX());
        double py = Math.abs(pos.getY() - player.getY());
        double pz = Math.abs(pos.getZ() - player.getZ());

        if (py <= dist) {
            if (pz <= dist) {
                alignedX = true;
                for (int x = -dist; x < dist; x++) {
                    this.createAxisParticles(world, new BlockPos(player.getX() + x, pos.getY(), pos.getZ()), DyeColor.RED);
                }
            }

            if (px <= dist) {
                alignedZ = true;
                for (int z = -dist; z < dist; z++) {
                    this.createAxisParticles(world, new BlockPos(pos.getX(), pos.getY(), player.getZ() + z), DyeColor.BLUE);
                }
            }
        }

        if (px <= dist && pz <= dist) {
            alignedY = true;
            for (int y = -dist; y < dist; y++) {
                this.createAxisParticles(world, new BlockPos(pos.getX(), player.getY() + y, pos.getZ()), DyeColor.PURPLE);
            }
        }
    }

    private void createAxisParticles(ClientWorld world, BlockPos pos, DyeColor color) {
        float[] col = color.getColorComponents();
        for (int i = 0; i < 3; i++) {
            double d = (double) pos.getX() + 0.5D + (world.random.nextDouble() * 0.4D) - 0.2D;
            double e = (double) pos.getY() + 0.25D;
            double f = (double) pos.getZ() + 0.5D + (world.random.nextDouble() * 0.4D) - 0.2D;

            world.addImportantParticle(ParticleTypes.ENTITY_EFFECT, d, e, f, col[0], col[1], col[2]);
        }

        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 0, 0, 0);
    }
}
