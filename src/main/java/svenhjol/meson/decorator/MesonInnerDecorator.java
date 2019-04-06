package svenhjol.meson.decorator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import svenhjol.meson.Meson;

import java.util.List;
import java.util.Random;

public abstract class MesonInnerDecorator
{
    protected Random rand;
    protected World world;
    protected StructureComponent structure;
    protected StructureBoundingBox box;
    protected MesonDecoratorTheme theme;
    protected MesonDecoratorItems items;

    public MesonInnerDecorator(StructureComponent structure, World world, StructureBoundingBox box)
    {
        this.structure = structure;
        this.world = world;
        this.box = box;
        this.rand = new Random();
        this.rand.setSeed(structure.hashCode());

        try {
            this.theme = getThemeClass()
                    .getConstructor(MesonInnerDecorator.class)
                    .newInstance(this);

        } catch (Exception e) {
            Meson.runtimeException("Could not instantiate required decorator classes");
        }

        this.items = new MesonDecoratorItems(this);
    }

    protected abstract Class<? extends MesonDecoratorTheme> getThemeClass();

    public abstract void generate();

    public StructureComponent getStructure()
    {
        return structure;
    }

    public World getWorld()
    {
        return world;
    }

    public StructureBoundingBox getBox()
    {
        return box;
    }

    public Random getRand()
    {
        return this.rand;
    }

    public MesonDecoratorTheme getTheme()
    {
        return this.theme;
    }

    public void add(Block block, int x, int y, int z)
    {
        add(block.getDefaultState(), x, y, z, EnumFacing.NORTH);
    }

    public void add(Block block, int x, int y, int z, EnumFacing facing)
    {
        add(block.getDefaultState(), x, y, z, facing);
    }

    public void add(IBlockState state, int x, int y, int z)
    {
        add(state, x, y, z, EnumFacing.NORTH);
    }

    public void add(IBlockState state, int x, int y, int z, EnumFacing facing)
    {
        BlockPos place = new BlockPos(x, y, z);
        setBlockState(state, place, facing);
    }

    public void addRow(List<IBlockState> states, int x, int y, int z, EnumFacing direction, EnumFacing facing)
    {
        BlockPos pos = new BlockPos(x, y, z);
        int i = 0;

        for (IBlockState state : states) {
            BlockPos offset = pos.offset(direction, i++);
            add(state, offset.getX(), offset.getY(), offset.getZ(), facing);
        }
    }

    public void fill(Block block, int x0, int y0, int z0, int x1, int y1, int z1, boolean overwrite)
    {
        fill(block.getDefaultState(), x0, y0, z0, x1, y1, z1, EnumFacing.NORTH, overwrite);
    }

    public void fill(IBlockState state, int x0, int y0, int z0, int x1, int y1, int z1, EnumFacing facing, boolean overwrite)
    {
        fillWithBlocks(state, x0, y0, z0, x1, y1, z1, facing, overwrite);
    }

    public TileEntity addTileEntity(BlockPos pos, IBlockState state, EnumFacing facing)
    {
        BlockPos place = getRelativePos(pos);
        if (isNotInBox(place)) return null;

        TileEntity tile = world.getTileEntity(place);
        if (tile != null) return null;

        setBlockState(state, pos, facing);
        return world.getTileEntity(place);
    }

    public boolean chance(float chance)
    {
        return this.rand.nextFloat() <= chance;
    }

    protected boolean common() { return chance(Meson.DEBUG ? 1.0f : 0.85f); }

    protected boolean uncommon() { return chance(Meson.DEBUG ? 1.0f : 0.25f); }

    protected boolean valuable() { return chance(Meson.DEBUG ? 1.0f : 0.05f); }

    protected boolean rare() { return chance(Meson.DEBUG ? 1.0f : 0.005f); }

    public void fillWithBlocks(IBlockState state, int x0, int y0, int z0, int x1, int y1, int z1, EnumFacing facing, boolean overwrite)
    {
        for (int y = y0; y <= y1; y++) {
            for (int x = x0; x <= x1; x++) {
                for (int z = z0; z <= z1; z++) {
                    BlockPos place = new BlockPos(x, y, z);
                    if (overwrite || getBlockStateFromPos(place).getMaterial() == Material.AIR) {
                        setBlockState(state, place, facing);
                    }
                }
            }
        }
    }

    public void setBlockState(IBlockState state, BlockPos pos, EnumFacing facing)
    {
        BlockPos place = getRelativePos(pos);
        if (!box.isVecInside(place)) {
            return;
        }

        // apply custom rotation first
        state = getRotatedState(facing, state);
        world.setBlockState(place, getRelativeState(state), 2);
    }

    public IBlockState getBlockStateFromPos(BlockPos pos)
    {
        int xo = getXWithOffset(pos.getX(), pos.getZ());
        int yo = getYWithOffset(pos.getY());
        int zo = getZWithOffset(pos.getX(), pos.getZ());
        BlockPos newpos = new BlockPos(xo, yo, zo);
        return world.getBlockState(newpos);
    }

    public int getXWithOffset(int x, int z)
    {
        EnumFacing enumfacing = structure.getCoordBaseMode();

        if (enumfacing == null) {
            return x;
        } else {
            switch (enumfacing) {
                case NORTH:
                case SOUTH:
                    return structure.getBoundingBox().minX + x;
                case WEST:
                    return structure.getBoundingBox().maxX - z;
                case EAST:
                    return structure.getBoundingBox().minX + z;
                default:
                    return x;
            }
        }
    }

    public int getYWithOffset(int y)
    {
        return structure.getCoordBaseMode() == null ? y : y + structure.getBoundingBox().minY;
    }

    public int getZWithOffset(int x, int z)
    {
        EnumFacing enumfacing = structure.getCoordBaseMode();

        if (enumfacing == null) {
            return z;
        } else {
            switch (enumfacing) {
                case NORTH:
                    return structure.getBoundingBox().maxZ - z;
                case SOUTH:
                    return structure.getBoundingBox().minZ + z;
                case WEST:
                case EAST:
                    return structure.getBoundingBox().minZ + x;
                default:
                    return z;
            }
        }
    }

    public BlockPos getRelativePos(BlockPos pos)
    {
        return new BlockPos(
                getXWithOffset(pos.getX(), pos.getZ()),
                getYWithOffset(pos.getY()),
                getZWithOffset(pos.getX(), pos.getZ())
        );
    }

    public IBlockState getRelativeState(IBlockState original)
    {
        return getRotatedState(structure.getCoordBaseMode(), original);
    }

    public EnumFacing getRelativeFacing(EnumFacing facing)
    {
        return getRotatedFacing(structure.getCoordBaseMode(), facing);
    }

    public IBlockState getRotatedState(EnumFacing axis, IBlockState original)
    {
        Mirror mirror;
        Rotation rotation;

        if (axis == null) {
            rotation = Rotation.NONE;
            mirror = Mirror.NONE;
        } else {
            switch (axis) {
                case SOUTH:
                    mirror = Mirror.LEFT_RIGHT;
                    rotation = Rotation.NONE;
                    break;
                case WEST:
                    mirror = Mirror.LEFT_RIGHT;
                    rotation = Rotation.CLOCKWISE_90;
                    break;
                case EAST:
                    mirror = Mirror.NONE;
                    rotation = Rotation.CLOCKWISE_90;
                    break;
                default:
                    mirror = Mirror.NONE;
                    rotation = Rotation.NONE;
            }
        }

        return original
                .withMirror(mirror)
                .withRotation(rotation);
    }

    public EnumFacing getRotatedFacing(EnumFacing axis, EnumFacing facing)
    {
        Rotation rotation;
        Mirror mirror;

        if (axis == null) {
            rotation = Rotation.NONE;
            mirror = Mirror.NONE;
        } else {
            switch (axis) {
                case SOUTH:
                    mirror = Mirror.LEFT_RIGHT;
                    rotation = Rotation.NONE;
                    break;
                case WEST:
                    mirror = Mirror.LEFT_RIGHT;
                    rotation = Rotation.CLOCKWISE_90;
                    break;
                case EAST:
                    mirror = Mirror.NONE;
                    rotation = Rotation.CLOCKWISE_90;
                    break;
                default:
                    mirror = Mirror.NONE;
                    rotation = Rotation.NONE;
            }
        }

        return rotation.add( mirror.toRotation(facing) ).rotate(facing);
    }

    public float getRotatedAngle(EnumFacing axis, float angle)
    {
        float newAngle = angle;

        if (axis != null) {
            switch (axis) {
                case SOUTH:
                    newAngle += 270.0f;
                    break;

                case WEST:
                    newAngle += 180.0f;
                    break;

                case EAST:
                    newAngle += 90.0f;
                    break;

                case NORTH:
//                    newAngle += 180.0f;

                default:
                    break;
            }
        }

        return newAngle;
    }

    public boolean isNotInBox(BlockPos pos)
    {
        return !box.isVecInside(pos);
    }
}
