package svenhjol.charm.world.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.charm.Charm;
import svenhjol.charm.world.feature.CompassBinding;
import svenhjol.meson.MesonItem;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.TextHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemBoundCompass extends MesonItem
{
    private static final String POSX = "posX";
    private static final String POSY = "posY"; // the dimension
    private static final String POSZ = "posZ";
    private static final String COLOR = "Quark:ItemNameDye";
    private static final String ROTA = "rota";
    private static final String ROTATION = "rotation";
    private static final String LASTUPDATE = "lastUpdateTick";

    public ItemBoundCompass()
    {
        super("bound_compass");
        setMaxStackSize(1);
        setCreativeTab(CreativeTabs.MISC);

        addPropertyOverride(new ResourceLocation("angle"), new IItemPropertyGetter()
        {
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
                boolean validDimension;
                BlockPos pos = getPos(stack);

                if (pos != null) {
                    // check current dimension
                    validDimension = world.provider.getDimension() == pos.getY();
                } else {
                    // set to spawn point
                    pos = world.provider.getSpawnPoint();
                    validDimension = world.provider.getDimension() == 0;
                }

                if (validDimension) {
                    double yaw = hasEntity ? entity.rotationYaw : getFrameRotation((EntityItemFrame) entity);
                    yaw = MathHelper.positiveModulo(yaw / 360.0, 1.0);

                    double relAngle = getPosToAngle(entity, pos) / (Math.PI * 2);
                    angle = 0.5 - (yaw - 0.25 - relAngle);
                } else {
                    angle = Math.random();
                }

                if (hasEntity) {
                    angle = wobble(world, stack, angle);
                }

                return MathHelper.positiveModulo((float) angle, 1.0F);
            }

            @SideOnly(Side.CLIENT)
            private double wobble(World worldIn, ItemStack stack, double angle)
            {
                if (!stack.hasTagCompound()) {
                    return 0.0f;
                }

                double rotation = ItemNBTHelper.getDouble(stack, ROTATION, 0);
                double rota = ItemNBTHelper.getDouble(stack, ROTA, 0);
                long lastUpdateTick = ItemNBTHelper.getLong(stack, LASTUPDATE, 0);

                if (worldIn.getTotalWorldTime() != lastUpdateTick)
                {
                    lastUpdateTick = worldIn.getTotalWorldTime();
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
        });
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (!CompassBinding.showInformation) return;

        BlockPos pos = getPos(stack);
        TextFormatting color = TextHelper.getTextFormattingByDyeDamage(getColor(stack));

        if (pos != null) {
            String x = String.valueOf(pos.getX());
            String z = String.valueOf(pos.getZ());
            String dim = String.valueOf(pos.getY());
            tooltip.add(color + x + " " + z + ", " + I18n.format("dimension") + " " + dim);
        }
    }

    public static ItemStack setPos(ItemStack stack, BlockPos pos)
    {
        ItemNBTHelper.setInt(stack, POSX, pos.getX());
        ItemNBTHelper.setInt(stack, POSY, pos.getY()); // the dimension
        ItemNBTHelper.setInt(stack, POSZ, pos.getZ());
        return stack;
    }

    public static ItemStack setColor(ItemStack stack, int meta)
    {
        if (meta > 0) {
            ItemNBTHelper.setInt(stack, COLOR, meta);
        }
        return stack;
    }

    public static int getColor(ItemStack stack)
    {
        return ItemNBTHelper.getInt(stack, COLOR, 0);
    }

    public BlockPos getPos(ItemStack stack)
    {
        if (stack.hasTagCompound()) {
            int x = ItemNBTHelper.getInt(stack, POSX, 0);
            int y = ItemNBTHelper.getInt(stack, POSY, 0);
            int z = ItemNBTHelper.getInt(stack, POSZ, 0);
            return new BlockPos(x, y, z);
        }
        return null;
    }
}