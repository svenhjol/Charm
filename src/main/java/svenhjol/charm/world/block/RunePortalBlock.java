package svenhjol.charm.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemGroup;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import svenhjol.charm.world.tileentity.RunePortalTileEntity;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.IMesonBlock;

import javax.annotation.Nullable;
import java.util.Random;

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
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn)
    {
        // TODO teleport
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
    {
        BasicParticleType particle;
        float f = rand.nextFloat();
        if (f < 0.5f) {
            particle = ParticleTypes.ENCHANT;
        } else {
            particle = ParticleTypes.SMOKE;
        }

        double d0 = (float)pos.getX() + rand.nextFloat();
        double d1 = (float)pos.getY() + 1.0F;
        double d2 = (float)pos.getZ() + rand.nextFloat();
        worldIn.addParticle(particle, d0, d1, d2, 0.0D, 0.0D, 0.0D);
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
            return (RunePortalTileEntity)tile;
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

    public void setPortal(World world, BlockPos pos, BlockPos portal)
    {
        RunePortalTileEntity tile = getTileEntity(world, pos);
        if (tile != null) {
            tile.portal = portal;
        }
    }
}
