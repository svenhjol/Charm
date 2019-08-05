package svenhjol.charm.world.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonItem;
import svenhjol.meson.helpers.ItemNBTHelper;

import javax.annotation.Nullable;
import java.util.Objects;

public class BoundCompassItem extends MesonItem
{
    private static final String POS = "pos";
    private static final String DIM = "dim";
    private static final String COLOR = "color";
    private static final String ROTA = "rota";
    private static final String ROTATION = "rotation";
    private static final String LASTUPDATE = "lastUpdateTick";

    public BoundCompassItem()
    {
        super("bound_compass", new Item.Properties()
            .group(ItemGroup.MISC)
            .maxStackSize(1)
        );

        addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter()
        {
            @Override
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entityIn)
            {
                if (entityIn == null && !stack.isOnItemFrame()) return 0;

                boolean hasEntity = entityIn != null;
                Entity entity = hasEntity ? entityIn : stack.getItemFrame();

                if (world == null) {
                    world = Objects.requireNonNull(entity).world;
                }

                double angle;
                boolean validDimension;
                BlockPos pos = getPos(stack);
                int dim = getDim(stack);

                if (pos != null) {
                    // check current dimension
                    validDimension = world.getDimension().getType().getId() == dim;
                } else {
                    // set to spawn point
                    pos = world.getSpawnPoint();
                    validDimension = world.getDimension().getType().getId() == 0;
                }

                if (validDimension) {
                    double yaw = hasEntity ? entity.rotationYaw : getFrameRotation((ItemFrameEntity) Objects.requireNonNull(entity));
                    yaw = MathHelper.positiveModulo(yaw / 360.0, 1.0);
                    double relAngle = getPosToAngle(entity, pos) / (Math.PI * 2);
                    angle = 0.5 - (yaw - 0.25 - relAngle);
                } else {
                    angle = Math.random();
                }

                if (hasEntity) {
                    angle = wobble(world, stack, angle);
                }

                return MathHelper.positiveModulo((float)angle, 1.0F);
            }

            @OnlyIn(Dist.CLIENT)
            private double wobble(World worldIn, ItemStack stack, double angle)
            {
                if (!stack.hasTag()) {
                    return 0.0f;
                }

                double rotation = ItemNBTHelper.getDouble(stack, ROTATION, 0);
                double rota = ItemNBTHelper.getDouble(stack, ROTA, 0);
                long lastUpdateTick = ItemNBTHelper.getLong(stack, LASTUPDATE, 0);

                if (worldIn.getGameTime() != lastUpdateTick)
                {
                    lastUpdateTick = worldIn.getGameTime();
                    double d0 = angle - rotation;
                    d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
                    rota += d0 * 0.1D;
                    rota *= 0.8D;
                    rotation = MathHelper.positiveModulo(rotation + rota, 1.0D);
                }

                ItemNBTHelper.setLong(stack, LASTUPDATE, lastUpdateTick);
                ItemNBTHelper.setDouble(stack, ROTATION, rotation);
                ItemNBTHelper.setDouble(stack, ROTA, rota);

                return rotation;
            }

            @OnlyIn(Dist.CLIENT)
            private double getFrameRotation(ItemFrameEntity frame)
            {
                return MathHelper.wrapDegrees(180 + Objects.requireNonNull(frame.getHorizontalFacing()).getHorizontalIndex() * 90);
            }

            @OnlyIn(Dist.CLIENT)
            private double getPosToAngle(Entity entity, BlockPos pos)
            {
                return Math.atan2(pos.getZ() - entity.posZ, pos.getX() - entity.posX);
            }
        });
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    public static int getColor(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, COLOR, 0);
    }

    public static int getDim(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, DIM, 0);
    }

    public static BlockPos getPos(ItemStack stack)
    {
        if (!stack.hasTag()) return null;
        long pos = ItemNBTHelper.getLong(stack, POS, 0);
        return BlockPos.fromLong(pos);
    }

    public static void setColor(ItemStack stack, int color)
    {
        ItemNBTHelper.setInt(stack, COLOR, color);
    }

    public static void setDim(ItemStack stack, int dim)
    {
        ItemNBTHelper.setInt(stack, DIM, dim);
    }

    public static void setPos(ItemStack stack, BlockPos pos)
    {
        ItemNBTHelper.setLong(stack, POS, pos.toLong());
    }
}
