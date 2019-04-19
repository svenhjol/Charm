package svenhjol.charm.world.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonItem;
import svenhjol.meson.helper.ItemHelper;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemBoundCompass extends MesonItem implements IItemPropertyGetter
{
    private static final String POSX = "posX";
    private static final String POSY = "posY"; // the dimension
    private static final String POSZ = "posZ";

    @SideOnly(Side.CLIENT)
    double rotation;
    @SideOnly(Side.CLIENT)
    double rota;
    @SideOnly(Side.CLIENT)
    long lastUpdateTick;
//
//    @Override
//    public boolean hasEffect(ItemStack stack)
//    {
//        return getPos(stack).getY() < 255;
//    }
//
//    @Override
//    public int getEnchantEffectColor(ItemStack stack)
//    {
//        return ItemHelper.getInt(stack, COLOR, 0);
//    }

    public ItemBoundCompass(String name)
    {
        super(name);
        addPropertyOverride(new ResourceLocation("angle"), this);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    public static ItemStack setPos(ItemStack stack, BlockPos pos)
    {
        ItemHelper.setInt(stack, POSX, pos.getX());
        ItemHelper.setInt(stack, POSY, pos.getY()); // the dimension
        ItemHelper.setInt(stack, POSZ, pos.getZ());
        return stack;
    }

    public static ItemStack setColor(ItemStack stack, int meta)
    {
        if (meta > 0) {
            ItemHelper.setInt(stack, "Quark:ItemNameDye", meta);
        }
        return stack;
    }

    public BlockPos getPos(ItemStack stack)
    {
        if (stack.hasTagCompound()) {
            int x = ItemHelper.getInt(stack, POSX, 0);
            int y = ItemHelper.getInt(stack, POSY, 255);
            int z = ItemHelper.getInt(stack, POSZ, 0);
            return new BlockPos(x, y, z);
        }

        return new BlockPos(0, 255, 0);
    }

    @Override
    public float apply(ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entityIn)
    {
        if (entityIn == null && !stack.isOnItemFrame()) {
            return 0;
        }

        boolean hasEntity = entityIn != null;
        Entity entity = hasEntity ? entityIn : stack.getItemFrame();

        if (world == null) {
            world = Objects.requireNonNull(entity).world;
        }

        double angle;
        BlockPos pos = getPos(stack);

        if (pos.getY() == world.provider.getDimension()) {

            // the Y matches the dimension, point to the block pos
            double yaw = hasEntity ? entity.rotationYaw : getFrameRotation((EntityItemFrame) entity);
            yaw = MathHelper.positiveModulo(yaw / 360.0, 1.0);

            double relAngle = getPosToAngle(entity, pos) / (Math.PI * 2);
            angle = 0.5 - (yaw - 0.25 - relAngle);

        } else {

            // the dimension is wrong
            angle = Math.random();
        }

        if (hasEntity) {
            angle = wobble(world, angle);
        }

        return MathHelper.positiveModulo((float) angle, 1.0F);
    }

    @SideOnly(Side.CLIENT)
    private double wobble(World worldIn, double angle)
    {
        if (worldIn.getTotalWorldTime() != this.lastUpdateTick)
        {
            this.lastUpdateTick = worldIn.getTotalWorldTime();
            double d0 = angle - this.rotation;
            d0 = MathHelper.positiveModulo(d0 + 0.5D, 1.0D) - 0.5D;
            this.rota += d0 * 0.1D;
            this.rota *= 0.8D;
            this.rotation = MathHelper.positiveModulo(this.rotation + this.rota, 1.0D);
        }

        return this.rotation;
    }

    @SideOnly(Side.CLIENT)
    private double getFrameRotation(EntityItemFrame frame)
    {
        return MathHelper.wrapDegrees(180 + Objects.requireNonNull(frame.facingDirection).getHorizontalIndex() * 90);
    }

    @SideOnly(Side.CLIENT)
    private double getPosToAngle(Entity entity, BlockPos pos)
    {
        return Math.atan2(pos.getZ() - entity.posZ, pos.getX() - entity.posX);
    }
}
