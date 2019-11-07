package svenhjol.charm.tools.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import svenhjol.charm.base.CharmSounds;
import svenhjol.meson.MesonItem;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.ItemNBTHelper;
import svenhjol.meson.helper.SoundHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class MoonstoneItem extends MesonItem
{
    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String DIM = "dim";
    private static final String ALIGNED = "aligned";
    private static final String ORIGIN = "origin";

    public final DyeColor color;

    public MoonstoneItem(MesonModule module, DyeColor color)
    {
        super(module, "moonstone_" + color.getName(), new Item.Properties()
            .group(ItemGroup.TOOLS)
            .maxStackSize(1)
        );

        this.color = color;

        addPropertyOverride(new ResourceLocation("aligned"), new IItemPropertyGetter()
        {
            @Override
            public float call(ItemStack stack, @Nullable World world, @Nullable LivingEntity entityIn)
            {
                if (entityIn == null && !stack.isOnItemFrame()) {
                    return 0;
                }

                boolean hasEntity = entityIn != null;
                Entity entity = hasEntity ? entityIn : stack.getItemFrame();

                if (world == null) {
                    world = Objects.requireNonNull(entity).world;
                }

                BlockPos stonePos = getStonePos(stack);
                if (stonePos == null) return 0;
                if (entity == null) return 0;

                int stoneDim = getStoneDim(stack);
                int entityDim = world.getDimension().getType().getId();

                int stoneX = stonePos.getX();
                int stoneZ = stonePos.getZ();
                int entityX = MathHelper.floor(entity.posX);
                int entityZ = MathHelper.floor(entity.posZ);

                if (stoneDim == -1 && entityDim != -1) {
                    stoneX *= 8.0f;
                    stoneZ *= 8.0f;
                } else if (entityDim == -1 && stoneDim != -1) {
                    stoneX /= 8.0f;
                    stoneZ /= 8.0f;
                }

                boolean alignedx = stoneX == entityX;
                boolean alignedz = stoneZ == entityZ;

                boolean origin = alignedx && alignedz;
                boolean aligned = alignedx || alignedz;

                if (world.isRemote && aligned || origin) {
                    float pitch = 0.5F + (color.getId() / 16.0F);

                    if (aligned && !ItemNBTHelper.getBoolean(stack, MoonstoneItem.ALIGNED, false)) {
                        SoundHelper.playSoundAtPos(entity.getPosition(), CharmSounds.HOMING, SoundCategory.BLOCKS, 0.55F, pitch);
                    }
                    if (origin && !ItemNBTHelper.getBoolean(stack, MoonstoneItem.ORIGIN, false)) {
                        effectAtOrigin(world, stonePos);
                        SoundHelper.playSoundAtPos(entity.getPosition(), SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0F, pitch);
                        SoundHelper.playSoundAtPos(entity.getPosition(), CharmSounds.HOMING, SoundCategory.BLOCKS, 0.55F, pitch);
                    }
                }

                ItemNBTHelper.setBoolean(stack, MoonstoneItem.ALIGNED, aligned);
                ItemNBTHelper.setBoolean(stack, MoonstoneItem.ORIGIN, origin);

                return 0;
            }
        });
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        if (context.getPlayer() == null) return ActionResultType.FAIL;

        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = player.getPosition();
        Hand hand = context.getHand();
        ItemStack item = player.getHeldItem(hand);
        BlockPos stonePos = getStonePos(item);
        int stoneDim = getStoneDim(item);

        if (world.isRemote
            && player.dimension.getId() == stoneDim
            && stonePos != null
        ) {
            int x = stonePos.getX() - pos.getX();
            int z = stonePos.getZ() - pos.getZ();
            int i = MathHelper.floor(MathHelper.sqrt((float) (x * x + z * z)));

            if (i == 0) {
                effectAtOrigin(world, stonePos);
            } else {
                String key = i > 1 ? "gui.charm.moonstone_distance" : "gui.charm.moonstone_distance_1";
                player.sendStatusMessage(new TranslationTextComponent(key, i), true);
            }

        }

        return ActionResultType.PASS;
    }

    @Override
    public boolean hasEffect(ItemStack stack)
    {
        return ItemNBTHelper.getBoolean(stack, ALIGNED, false) || ItemNBTHelper.getBoolean(stack, ORIGIN, false);
    }

    @Nullable
    public static BlockPos getStonePos(ItemStack stack)
    {
        if (stack.hasTag()) {
            int x = ItemNBTHelper.getInt(stack, X, 0);
            int y = ItemNBTHelper.getInt(stack, Y, 0);
            int z = ItemNBTHelper.getInt(stack, Z, 0);
            return new BlockPos(x, y, z);
        }
        return null;
    }

    public static int getStoneDim(ItemStack stack)
    {
        if (stack.hasTag()) {
            return ItemNBTHelper.getInt(stack, DIM, 0);
        }
        return 0;
    }

    public static ItemStack setStonePos(ItemStack stack, BlockPos pos)
    {
        ItemNBTHelper.setInt(stack, X, pos.getX());
        ItemNBTHelper.setInt(stack, Y, pos.getY());
        ItemNBTHelper.setInt(stack, Z, pos.getZ());
        return stack;
    }

    public static ItemStack setStoneDim(ItemStack stack, int dim)
    {
        ItemNBTHelper.setInt(stack, DIM, dim);
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> textComponents, ITooltipFlag flag)
    {
        int dim = getStoneDim(stack);
        BlockPos pos = getStonePos(stack);

        if (pos != null) {
            String x = String.valueOf(pos.getX());
            String y = String.valueOf(pos.getY());
            String z = String.valueOf(pos.getZ());

            textComponents.add(new TranslationTextComponent("gui.charm.moonstone_location", x, y, z, dim));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void effectAtOrigin(World world, BlockPos pos)
    {
        for (int ii = 0; ii < 10; ii++) {
            world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + world.rand.nextFloat(), pos.getY() + 1.75D, pos.getZ() + world.rand.nextFloat(), 0.0D, 0.1D, 0.0D);
        }
    }
}
