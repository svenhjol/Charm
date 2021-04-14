package svenhjol.charm.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.CharmParticles;
import svenhjol.charm.base.helper.DimensionHelper;
import svenhjol.charm.base.item.CharmItem;
import svenhjol.charm.module.Astrolabes;

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

            if (world.isClient)
                clientEffects((ClientWorld)world, user.getBlockPos(), user);

            return TypedActionResult.success(held);
        }

        BlockPos position = getPosition(held);
        Identifier dimension = AstrolabeItem.getDimension(held);

        if (world.isClient && position != null && dimension != null)
            clientEffects((ClientWorld) world, Astrolabes.getDimensionPosition(world, position, dimension), user);

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
    private void clientEffects(ClientWorld world, BlockPos pos, PlayerEntity player) {
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

        // maybe have three alignments sounds in future
        if (alignedX || alignedY || alignedZ)
            world.playSound(player, player.getBlockPos(), SoundEvents.ITEM_SPYGLASS_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);

        player.getItemCooldownManager().set(Astrolabes.ASTROLABE, 20);
    }

    private void createAxisParticles(ClientWorld world, BlockPos pos, DyeColor color) {
        DefaultParticleType particleType = CharmParticles.AXIS_PARTICLE;

        float[] col = color.getColorComponents();
        for (int i = 0; i < 9; i++) {
            double x = (double) pos.getX() + 0.5D;
            double y = (double) pos.getY() + 0.5D;
            double z = (double) pos.getZ() + 0.5D;

            world.addImportantParticle(particleType, x, y, z, col[0], col[1], col[2]);
        }
    }
}
