package svenhjol.charm.world.item;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Optional;
import svenhjol.charm.Charm;
import svenhjol.charm.world.feature.Moonstone;
import svenhjol.meson.MesonItem;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.TextHelper;
import svenhjol.meson.iface.IMesonItem.IItemColorHandler;
import svenhjol.meson.iface.IMesonItem.IItemCustomModel;
import vazkii.quark.api.ICustomEnchantColor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Optional.Interface(iface = "vazkii.quark.api.ICustomEnchantColor", modid = "quark")
public class ItemMoonstone extends MesonItem implements ICustomEnchantColor, IItemColorHandler, IItemCustomModel
{
    private static final String SX = "charmPosX";
    private static final String SY = "charmPosY";
    private static final String SZ = "charmPosZ";
    private static final String SD = "charmDim";
    private static final String GLOW = "charmGlow";
    private static final String TICK = "charmTick";
    private static final String ORIGIN = "charmOrigin";

    public ItemMoonstone()
    {
        super("moonstone");
        setCreativeTab(CreativeTabs.TOOLS);
        setMaxStackSize(1);
        setHasSubtypes(true);

        addPropertyOverride(new ResourceLocation("align"), new IItemPropertyGetter()
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

                BlockPos sPos = getStonePos(stack);
                if (sPos == null) return 0;
                if (entity == null) return 0;

                int sDim = getStoneDim(stack);
                int eDim = world.provider.getDimension();

                int sx = sPos.getX();
                int sz = sPos.getZ();
                int ex = MathHelper.floor(entity.posX);
                int ez = MathHelper.floor(entity.posZ);

                if (sDim == -1 && eDim != -1) {
                    sx *= 8.0f;
                    sz *= 8.0f;
                } else if (eDim == -1 && sDim != -1) {
                    sx /= 8.0f;
                    sz /= 8.0f;
                }

                boolean alignedx = sx == ex;
                boolean alignedz = sz == ez;
                ItemNBTHelper.setBoolean(stack, ORIGIN, alignedx && alignedz);

                return glow(world, stack, alignedx || alignedz);
            }

            private int glow(World world, ItemStack stack, boolean aligned)
            {
                int glow = ItemNBTHelper.getInt(stack, GLOW, 0);
                long tick = ItemNBTHelper.getLong(stack, TICK, 0);

                if (world.getTotalWorldTime() != tick) {
                    tick = world.getTotalWorldTime();

                    if (aligned && glow <= 6) {
                        glow++;
                    } else if (!aligned && glow >= 0) {
                        glow--;
                    }
                }

                ItemNBTHelper.setLong(stack, TICK, tick);
                ItemNBTHelper.setInt(stack, GLOW, glow);

                return glow;
            }
        });
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return Moonstone.glowOnOrigin
            && ItemNBTHelper.getBoolean(stack, ORIGIN, false);
    }

    @Override
    public IItemColor getItemColor()
    {
        return (stack, tintIndex) -> {
            int meta = stack.getMetadata();
            return EnumDyeColor.byDyeDamage(15-meta).getColorValue();
        };
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        if (isInCreativeTab(tab)) {
            EnumDyeColor[] values = EnumDyeColor.values();

            for (int i = 0; i < values.length; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @Override
    public void registerModels(Item item)
    {
        for (int i = 0; i < 16; i++) {
            if (item != null && item.getRegistryName() != null) {
                ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
                ModelLoader.setCustomModelResourceLocation(item, i, loc);
            }
        }
    }

    @Override
    public int getEnchantEffectColor(ItemStack stack)
    {
        int col = 0xffffff;

        if (ItemNBTHelper.getBoolean(stack, ORIGIN, false)) {
            col = EnumDyeColor.byDyeDamage(15-stack.getMetadata()).getColorValue();
        }

        return col - 0x050505;
    }

    public static BlockPos getStonePos(ItemStack stack)
    {
        if (stack.hasTagCompound()) {
            int x = ItemNBTHelper.getInt(stack, SX, 0);
            int y = ItemNBTHelper.getInt(stack, SY, 0);
            int z = ItemNBTHelper.getInt(stack, SZ, 0);
            return new BlockPos(x, y, z);
        }
        return null;
    }

    public static int getStoneDim(ItemStack stack)
    {
        if (stack.hasTagCompound()) {
            return ItemNBTHelper.getInt(stack, SD, 0);
        }
        return 0;
    }

    public static ItemStack setStonePos(ItemStack stack, BlockPos pos)
    {
        ItemNBTHelper.setInt(stack, SX, pos.getX());
        ItemNBTHelper.setInt(stack, SY, pos.getY());
        ItemNBTHelper.setInt(stack, SZ, pos.getZ());
        return stack;
    }

    public static ItemStack setStoneDim(ItemStack stack, int dim)
    {
        ItemNBTHelper.setInt(stack, SD, dim);
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        if (!Moonstone.showInformation) return;

        int dim = getStoneDim(stack);
        BlockPos pos = getStonePos(stack);
        TextFormatting color = TextHelper.getTextFormattingByDyeDamage(15-EnumDyeColor.byDyeDamage(stack.getMetadata()).getDyeDamage());

        if (pos != null) {
            String x = String.valueOf(pos.getX());
            String y = String.valueOf(pos.getY());
            String z = String.valueOf(pos.getZ());

            tooltip.add(color + x + " " + y + " " + z + ". " + I18n.format("dimension") + " " + dim);
        }
    }
}
