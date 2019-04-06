package svenhjol.meson.decorator;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityPainting;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import svenhjol.meson.MesonTileInventory;
import svenhjol.meson.helper.EntityHelper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MesonDecoratorItems
{
    protected MesonInnerDecorator generator;
    protected MesonDecoratorTheme theme;
    protected Random rand;
    protected World world;
    protected StructureBoundingBox box;
    protected StructureComponent structure;

    public MesonDecoratorItems(MesonInnerDecorator generator)
    {
        this.generator = generator;
        this.world = generator.getWorld();
        this.rand = generator.getRand();
        this.theme = generator.getTheme();
        this.box = generator.getBox();
        this.structure = generator.getStructure();
    }

    public void addStorageBlock(int x, int y, int z, EnumFacing facing)
    {
        IBlockState type = theme.getStorageBlock();
        addStorageBlock(type, x, y, z, facing);
    }

    public void addStorageBlock(IBlockState type, int x, int y, int z, EnumFacing facing)
    {
        addStorageBlock(type, x, y, z, facing, theme.getLootTable());
    }

    public void addStorageBlock(IBlockState type, int x, int y, int z, EnumFacing facing, ResourceLocation loot)
    {
        BlockPos place = new BlockPos(x, y, z);
        TileEntity tile = generator.addTileEntity(place, type, facing);

        if (tile instanceof MesonTileInventory) {
            ((MesonTileInventory)tile).setLootTable(loot);
        } else if (tile instanceof TileEntityChest) {
            ((TileEntityChest)tile).setLootTable(loot, world.rand.nextLong());
        }
    }

    public void addFunctionalBlock(int x, int y, int z, EnumFacing facing)
    {
        generator.add(theme.getFunctionalBlock(), x, y, z, facing);
    }

    public void addDecorationBlock(int x, int y, int z, EnumFacing facing)
    {
        generator.add(theme.getDecorationBlock(), x, y, z, facing);
    }

    public void addRandomBlock(int x, int y, int z, EnumFacing facing)
    {
        float f = rand.nextFloat();
        if (f <= 0.5f) {
            addFunctionalBlock(x, y, z, facing);
        } else {
            addDecorationBlock(x, y, z, facing);
        }
    }

    public void addArmorStand(int x, int y, int z, float angle)
    {
        BlockPos place = new BlockPos(x, y, z);
        addArmorStand(place, angle, theme.getWearables());
    }

    public void addArmorStand(BlockPos pos, float angle, @Nullable Map<EntityEquipmentSlot, ItemStack> wearing)
    {
        BlockPos place = generator.getRelativePos(pos);
        if (generator.isNotInBox(place)) return;

        EntityArmorStand stand = new EntityArmorStand(world, place.getX() + 0.5f, place.getY(), place.getZ() + 0.5f);

        if (wearing != null) {
            List<EntityEquipmentSlot> slots = new ArrayList<EntityEquipmentSlot>() {{
                add(EntityEquipmentSlot.HEAD);
                add(EntityEquipmentSlot.CHEST);
                add(EntityEquipmentSlot.LEGS);
                add(EntityEquipmentSlot.FEET);
                add(EntityEquipmentSlot.MAINHAND);
                add(EntityEquipmentSlot.OFFHAND);
            }};

            slots.forEach(slot -> {
                if (wearing.get(slot) != null) {
                    stand.setItemStackToSlot(slot, wearing.get(slot));
                }
            });
        }

        world.setBlockToAir(place);
        world.setBlockToAir(place.up());

//        stand.rotationYaw = getRotatedAngle(component.getCoordBaseMode(), angle);
        stand.setLocationAndAngles(place.getX() + 0.5f, place.getY(), place.getZ() + 0.5f, generator.getRotatedAngle(structure.getCoordBaseMode(), angle), 0.0f);
        world.spawnEntity(stand);
    }

    public void addAnvil(int x, int y, int z, int damage, @Nullable EnumFacing facing)
    {
        IBlockState anvil = Blocks.ANVIL.getDefaultState().withProperty(BlockAnvil.DAMAGE, damage);
        generator.add(anvil, x, y, z, facing);
    }

    public void addFramedItem(int x, int y, int z, EnumFacing direction)
    {
        BlockPos place = new BlockPos(x, y, z);
        addFramedItem(place, direction, theme.getFramedItem());
    }

    public void addFramedItem(BlockPos pos, EnumFacing facing, @Nullable ItemStack displayedItem)
    {
        BlockPos place = generator.getRelativePos(pos);
        if (generator.isNotInBox(place)) return;

        EntityItemFrame frame = new EntityItemFrame(world, place, generator.getRelativeFacing(facing));

        if (displayedItem != null) {
            frame.setDisplayedItem(displayedItem);
        }
        world.spawnEntity(frame);
    }

    public void addPainting(int x, int y, int z, EnumFacing direction)
    {
        BlockPos place = new BlockPos(x, y, z);
        addPainting(place, direction);
    }

    public void addPainting(BlockPos pos, EnumFacing facing)
    {
        BlockPos place = generator.getRelativePos(pos);
        if (generator.isNotInBox(place)) return;

        EntityPainting painting = new EntityPainting(world, place, generator.getRelativeFacing(facing));

        world.spawnEntity(painting);
    }

    public void addRug(int floor, int x0, int z0, int x1, int z1, @Nullable Random rand)
    {
        if (rand == null) {
            rand = generator.getRand();
        }

        IBlockState carpet1 = Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.byDyeDamage(rand.nextInt(15)));
        IBlockState carpet2 = Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.byDyeDamage(rand.nextInt(15)));

        int style = rand.nextInt(3) + 1;
        IBlockState carpet;

        int i = 0;
        for (int xx = x0; xx <= x1; xx++) {
            for (int zz = z0; zz <= z1; zz++) {
                if (i % style == 0) {
                    carpet = carpet1;
                } else {
                    carpet = carpet2;
                }

                i++;

                generator.add(carpet, xx, floor, zz);
            }
        }
    }

    public void addDoor(int x, int y, int z, EnumFacing facing, BlockDoor door)
    {
        generator.addRow(getDoor(door), x, y, z, EnumFacing.UP, facing);
    }

    public void addStand(int x, int y, int z, EnumFacing facing, @Nullable IBlockState onTop)
    {
        if (onTop == null) {
            switch (rand.nextInt(2)) {
                case 0: onTop = getCake(); break;
                case 1: onTop = getFlowerPot(); break;
            }
        }

        if (onTop != null) {
            generator.add(Blocks.PLANKS.getDefaultState(), x, y, z, facing);
            generator.add(onTop, x, y + 1, z, facing);
        } else {
            addRandomBlock(x, y, z, facing);
        }
    }

    public void addTorch(int x, int y, int z, @Nullable EnumFacing facing)
    {
        IBlockState torch = Blocks.TORCH.getDefaultState();
        if (facing != null) {
            torch = torch.withProperty(BlockTorch.FACING, facing);
        }
        generator.add(torch, x, y, z);
    }


    public List<IBlockState> getBed()
    {
        List<IBlockState> bed = new ArrayList<>();
        bed.add(Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD));
        bed.add(Blocks.BED.getDefaultState().withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT));

        return bed;
    }

    public List<IBlockState> getDoor(BlockDoor door)
    {
        List<IBlockState> d = new ArrayList<>();
        d.add(door.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.LOWER));
        d.add(door.getDefaultState().withProperty(BlockDoor.HALF, BlockDoor.EnumDoorHalf.UPPER));
        return d;
    }

    public IBlockState getCake()
    {
        return Blocks.CAKE.getDefaultState().withProperty(BlockCake.BITES, generator.getRand().nextInt(2));
    }

    public IBlockState getFlowerPot()
    {
        return getFlowerPot(generator.getRand().nextInt(14) + 1);
    }

    public IBlockState getFlowerPot(int flowerType)
    {
        return Blocks.FLOWER_POT.getDefaultState().withProperty(BlockFlowerPot.LEGACY_DATA, flowerType);
    }

    public void spawnCat(int x, int y, int z, int skin, boolean tamed, @Nullable EntityPlayer player)
    {
        BlockPos posForMob = generator.getRelativePos(new BlockPos(x, y, z));
        if (generator.isNotInBox(posForMob)) return;

        Entity entity = EntityHelper.spawnEntity(new ResourceLocation("minecraft:ocelot"), world, posForMob);
        if (entity != null) {
            EntityOcelot cat = (EntityOcelot) entity;
            cat.setTameSkin(skin);
            if (tamed) {
                cat.setTamed(true);
                if (player != null) {
                    cat.setOwnerId(EntityPlayer.getUUID(player.getGameProfile()));
                }
            }
        }
    }
}
