package svenhjol.charm.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import svenhjol.charm.Charm;
import svenhjol.charm.world.message.ClientRunePortalAction;
import svenhjol.charm.world.module.EndPortalRunes;
import svenhjol.charm.world.tileentity.RunePortalTileEntity;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.iface.IMesonBlock;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RunePortalBlock extends EndPortalBlock implements IMesonBlock
{
    protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public RunePortalBlock(MesonModule module)
    {
        super(Block.Properties
            .create(Material.PORTAL, MaterialColor.BLACK)
            .doesNotBlockMovement()
            .lightValue(15)
            .hardnessAndResistance(-1.0F, 3600000.0F)
            .noDrops()
        );
        register(module, "rune_portal");
    }

    @Override
    public ItemGroup getItemGroup()
    {
        return ItemGroup.SEARCH;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn)
    {
        return new RunePortalTileEntity();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
    {
        return SHAPE;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        // no op
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity)
    {
        if (!world.isRemote
            && entity instanceof PlayerEntity
            && !entity.isPassenger()
            && !entity.isBeingRidden()
            && entity.isNonBoss()
            && VoxelShapes.compare(VoxelShapes.create(entity.getBoundingBox().offset((double) (-pos.getX()), (double) (-pos.getY()), (double) (-pos.getZ()))), state.getShape(world, pos), IBooleanFunction.AND)
        ) {
            BlockPos thisPortal = getPortal(world, pos);

            if (thisPortal != null) {
                BlockPos foundPortal = EndPortalRunes.findPortal((ServerWorld) world, thisPortal);
                PlayerEntity player = (PlayerEntity) entity;
                BlockPos teleportTo = foundPortal == null ? thisPortal : foundPortal;
                PlayerHelper.teleport(player, teleportTo.add(-2, 1, 0), 0);

                if (foundPortal != null) {
                    Meson.getInstance(Charm.MOD_ID).getPacketHandler().sendToAll(new ClientRunePortalAction(ClientRunePortalAction.TRAVELLED, foundPortal));
                } else {
                    player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 120, 4));
                    Meson.getInstance(Charm.MOD_ID).getPacketHandler().sendToAll(new ClientRunePortalAction(ClientRunePortalAction.UNLINKED, thisPortal));
                }
            }
        }
    }

    @Override
    public boolean canEntityDestroy(BlockState state, IBlockReader world, BlockPos pos, Entity entity)
    {
        return false;
    }

    @Nullable
    public RunePortalTileEntity getTileEntity(World world, BlockPos pos)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof RunePortalTileEntity) {
            return (RunePortalTileEntity) tile;
        }
        return null;
    }

    @Nullable
    public BlockPos getPortal(World world, BlockPos pos)
    {
        RunePortalTileEntity tile = getTileEntity(world, pos);
        if (tile == null) return null;
        return tile.portal;
    }

    @Nullable
    public List<Integer> getColors(World world, BlockPos pos)
    {
        RunePortalTileEntity tile = getTileEntity(world, pos);
        if (tile == null) return null;
        return tile.colors;
    }

    public void setPortal(World world, BlockPos pos, BlockPos portal, @Nullable List<Integer> colors)
    {
        RunePortalTileEntity tile = getTileEntity(world, pos);
        if (tile != null) {
            tile.portal = portal;
            tile.colors = colors == null ? new ArrayList<>() : colors;
            tile.markDirty();

            BlockState state = world.getBlockState(pos);
            world.notifyBlockUpdate(pos, state, state, 3);
        }
    }
}
