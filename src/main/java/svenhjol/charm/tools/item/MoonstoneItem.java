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
            .maxStackSize(16)
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
                BlockPos entityPos = entity.getPosition();

                int stoneDim = getStoneDim(stack);
                int entityDim = world.getDimension().getType().getId();

                BlockPos adjusted = getAdjusted(stonePos, stoneDim, entityDim);

                boolean alignedx = adjusted.getX() == entityPos.getX();
                boolean alignedz = adjusted.getZ() == entityPos.getZ();

                boolean origin = alignedx && alignedz;
                boolean aligned = alignedx || alignedz;

                if ((world.isRemote && aligned || origin) && entity instanceof PlayerEntity) {
                    float pitch = 0.5F + (color.getId() / 16.0F);
                    PlayerEntity player = (PlayerEntity)entity;

                    if (aligned && !ItemNBTHelper.getBoolean(stack, MoonstoneItem.ALIGNED, false)) {
                        world.playSound(player, entity.getPosition(), CharmSounds.HOMING, SoundCategory.BLOCKS, 0.55F, pitch);
                    }
                    if (origin && !ItemNBTHelper.getBoolean(stack, MoonstoneItem.ORIGIN, false)) {
                        effectAtOrigin(world, stonePos);
                        world.playSound(player, entity.getPosition(), SoundEvents.BLOCK_NOTE_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0F, pitch);
                        world.playSound(player, entity.getPosition(), CharmSounds.HOMING, SoundCategory.BLOCKS, 0.55F, pitch);
                    }
                }

                ItemNBTHelper.setBoolean(stack, MoonstoneItem.ALIGNED, aligned);
                ItemNBTHelper.setBoolean(stack, MoonstoneItem.ORIGIN, origin);

                return 0;
            }
        });
    }

    private BlockPos getAdjusted(BlockPos stone, int stoneDim, int entityDim)
    {
        int stoneX = stone.getX();
        int stoneZ = stone.getZ();

        if (stoneDim == -1 && entityDim != -1) {
            stoneX *= 8.0f;
            stoneZ *= 8.0f;
        } else if (entityDim == -1 && stoneDim != -1) {
            stoneX /= 8.0f;
            stoneZ /= 8.0f;
        }

        return new BlockPos(stoneX, 0, stoneZ);
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context)
    {
        if (context.getPlayer() == null) return ActionResultType.FAIL;

        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        Hand hand = context.getHand();
        ItemStack item = player.getHeldItem(hand);
        BlockPos stonePos = getStonePos(item);
        int stoneDim = getStoneDim(item);

        if (player.isSneaking() || stonePos == null) return ActionResultType.FAIL;

        BlockPos adjusted = getAdjusted(stonePos, stoneDim, player.dimension.getId());

        if (world.isRemote) {
            int x = adjusted.getX() - pos.getX();
            int z = adjusted.getZ() - pos.getZ();
            int i = MathHelper.floor(MathHelper.sqrt((float) (x * x + z * z)));

            String key;
            if (i == 0) {
                key = "gui.charm.moonstone_distance_0";
                effectAtOrigin(world, adjusted.add(0, stonePos.getY(), 0));
            } else {
                key = i > 1 ? "gui.charm.moonstone_distance" : "gui.charm.moonstone_distance_1";
            }
            player.sendStatusMessage(new TranslationTextComponent(key, i), true);
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
            world.addParticle(ParticleTypes.HAPPY_VILLAGER, pos.getX() + world.rand.nextFloat(), pos.getY() + 1.5D, pos.getZ() + world.rand.nextFloat(), 0.0D, 0.2D, 0.0D);
        }
    }
}
