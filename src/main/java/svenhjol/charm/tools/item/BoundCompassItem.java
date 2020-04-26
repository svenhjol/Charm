package svenhjol.charm.tools.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemNBTHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class BoundCompassItem extends MesonItem {
    private static final String POS = "pos";
    private static final String DIM = "dim";
    private static final String COLOR = "color";
    private static final String DIMENSIONAL = "dimensional";

    public BoundCompassItem(MesonModule module) {
        super(module, "bound_compass", new Item.Properties()
            .group(ItemGroup.MISC)
            .maxStackSize(1)
        );

        addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter() {
            @Override
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entityIn) {
                if (entityIn == null && !stack.isOnItemFrame()) return 0;

                boolean hasEntity = entityIn != null;
                Entity entity = hasEntity ? entityIn : stack.getItemFrame();

                if (world == null) {
                    world = Objects.requireNonNull(entity).world;
                }

                double angle;
                boolean validDimension;
                boolean isDimensional = isDimensional(stack);
                final int currentDim = world.getDimension().getType().getId();
                BlockPos pos = getPos(stack);
                int dim = getDim(stack);

                if (pos != null) {
                    // check current dimension
                    validDimension = currentDim == dim;
                } else {
                    // set to spawn point
                    pos = world.getSpawnPoint();
                    validDimension = currentDim == 0;
                }

                if (isDimensional)
                    pos = translatePosForDimension(world, pos, stack);

                if (validDimension || isDimensional) {
                    double yaw = hasEntity ? entity.rotationYaw : getFrameRotation((ItemFrameEntity) Objects.requireNonNull(entity));
                    yaw = MathHelper.positiveModulo(yaw / 360.0, 1.0);
                    double relAngle = getPosToAngle(entity, pos) / (Math.PI * 2);
                    angle = 0.5 - (yaw - 0.25 - relAngle);
                } else {
                    angle = 0;
                }

//                if (hasEntity && !validDimension) {
//                    angle = wobble(world, stack, angle);
//                }

                return MathHelper.positiveModulo((float) angle, 1.0F);
            }
        });
    }

//    @OnlyIn(Dist.CLIENT)
//    private double wobble(World worldIn, ItemStack stack, double angle) {
//        if (!stack.hasTag()) {
//            return 0.0f;
//        }
//
//        double rotation = ItemNBTHelper.getDouble(stack, ROTATION, 0);
//        double rota = ItemNBTHelper.getDouble(stack, ROTA, 0);
//        long lastUpdateTick = ItemNBTHelper.getLong(stack, LASTUPDATE, 0);
//
//        if (worldIn.getGameTime() != lastUpdateTick) {
//            lastUpdateTick = worldIn.getGameTime();
//            double d0 = angle - rotation;
//            d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
//            rota += d0 * 0.1D;
//            rota *= 0.8D;
//            rotation = MathHelper.positiveModulo(rotation + rota, 1.0D);
//        }
//
//        ItemNBTHelper.setLong(stack, LASTUPDATE, lastUpdateTick);
//        ItemNBTHelper.setDouble(stack, ROTATION, rotation);
//        ItemNBTHelper.setDouble(stack, ROTA, rota);
//
//        return rotation;
//    }

    @OnlyIn(Dist.CLIENT)
    private double getFrameRotation(ItemFrameEntity frame) {
        return MathHelper.wrapDegrees(180 + Objects.requireNonNull(frame.getHorizontalFacing()).getHorizontalIndex() * 90);
    }

    @OnlyIn(Dist.CLIENT)
    private double getPosToAngle(Entity entity, BlockPos pos) {
        BlockPos entityPos = entity.getPosition();
        return Math.atan2(pos.getZ() - entityPos.getZ(), pos.getX() - entityPos.getX());
    }

    public static int getColor(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, COLOR, 0);
    }

    public static int getDim(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, DIM, 0);
    }

    public static boolean isDimensional(ItemStack stack) {
        return ItemNBTHelper.getBoolean(stack, DIMENSIONAL, false);
    }

    @Nullable
    public static BlockPos getPos(ItemStack stack) {
        if (!stack.hasTag()) return null;
        long pos = ItemNBTHelper.getLong(stack, POS, 0);
        return BlockPos.fromLong(pos);
    }

    public static void setColor(ItemStack stack, int color) {
        ItemNBTHelper.setInt(stack, COLOR, color);

        // adjust glint color
        if (Charm.quarkCompat != null && Charm.quarkCompat.hasColorRuneModule())
            Charm.quarkCompat.applyColor(stack, DyeColor.byId(color));
    }

    public static void setDim(ItemStack stack, int dim) {
        ItemNBTHelper.setInt(stack, DIM, dim);
    }

    public static void setPos(ItemStack stack, BlockPos pos) {
        ItemNBTHelper.setLong(stack, POS, pos.toLong());
    }

    public static void setDimensional(ItemStack stack, boolean isDimensional) {
        ItemNBTHelper.setBoolean(stack, DIMENSIONAL, isDimensional);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag) {
        BlockPos pos = translatePosForDimension(world, getPos(stack), stack);

        if (pos != null) {
            String x = String.valueOf(pos.getX());
            String z = String.valueOf(pos.getZ());
            int dim = getDim(stack);

            textComponents.add(new TranslationTextComponent("gui.charm.bound_compass_location", x, z, dim));
        }
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return isDimensional(stack);
    }

    public BlockPos translatePosForDimension(IWorld world, BlockPos pos, ItemStack stack) {
        if (isDimensional(stack) && world != null && pos != null) {
            if (getDim(stack) == 0 && getCurrentDimensionId(world) == -1) {
                pos = translateOverworldToNether(pos);
            } else if (getDim(stack) == -1 && getCurrentDimensionId(world) == 0) {
                pos = translateNetherToOverworld(pos);
            }
        }
        return pos;
    }

    public BlockPos translateOverworldToNether(BlockPos pos) {
        return new BlockPos(pos.getX() / 8, pos.getY(), pos.getZ() / 8);
    }

    public BlockPos translateNetherToOverworld(BlockPos pos) {
        return new BlockPos(pos.getX() * 8, pos.getY(), pos.getZ() * 8);
    }

    public int getCurrentDimensionId(IWorld world) {
        return world.getDimension().getType().getId();
    }
}
