package svenhjol.charm.world.decorator.inner;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmDecoratorItems;
import svenhjol.charm.crafting.block.BlockBookshelfChest;
import svenhjol.charm.crafting.feature.BookshelfChest;
import svenhjol.charm.world.decorator.theme.*;
import svenhjol.charm.world.feature.MoreVillageBiomes;
import svenhjol.charm.world.feature.VillageDecorations;
import svenhjol.meson.decorator.MesonDecoratorTheme;
import svenhjol.meson.decorator.MesonInnerDecorator;
import svenhjol.meson.helper.LootHelper;
import svenhjol.meson.helper.ObfuscationHelper;
import svenhjol.meson.helper.WorldHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class VillageInnerDecorator extends MesonInnerDecorator
{
    protected CharmDecoratorItems items;

    public BlockPos pos;
    public Random villageRand;
    public Biome biome;

    // change in subclass constructors
    public int floor = 1;
    public int roof = 5;

    public VillageInnerDecorator(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
    {
        super(structure, world, box);
        this.pos = getPos();
        this.biome = getBiome();
        this.villageRand = getVillageRand();
        this.items = new CharmDecoratorItems(this);
    }

    @Override
    protected Class<? extends MesonDecoratorTheme> getThemeClass()
    {
        List<Class<? extends MesonDecoratorTheme>> availableThemes = new ArrayList<Class<? extends MesonDecoratorTheme>>() {{
            add(VillageSmithTheme.class);
            add(VillagePriestTheme.class);
            add(VillageLibrarianTheme.class);
            add(VillageButcherTheme.class);
            add(VillageCarpenterTheme.class);
            add(VillageShepherdTheme.class);
            add(VillageFishermanTheme.class);
        }};

        return availableThemes.get(rand.nextInt(availableThemes.size()));
    }

    @Override
    protected boolean common()
    {
        return chance(VillageDecorations.common);
    }

    @Override
    protected boolean uncommon()
    {
        return chance(VillageDecorations.uncommon);
    }

    @Override
    protected boolean valuable()
    {
        return chance(VillageDecorations.valuable);
    }

    @Override
    protected boolean rare()
    {
        return chance(VillageDecorations.rare);
    }

    public long getSeed()
    {
        return WorldHelper.getNearestVillageSeed(world, pos);
    }

    public BlockPos getPos()
    {
        StructureBoundingBox bb = structure.getBoundingBox();
        return new BlockPos(bb.minX, bb.minY, bb.minZ);
    }

    public ChunkPos getChunkPos()
    {
        return WorldHelper.getChunkPos(getPos());
    }

    public Random getVillageRand()
    {
        long seed = getSeed();
        Random rand = new Random();
        rand.setSeed(seed);
        return rand;
    }

    /**
     * @return True if this structure is part of a village that is zombie infested
     */
    public boolean isZombieInfested()
    {
        return ReflectionHelper.getPrivateValue(StructureVillagePieces.Village.class, (StructureVillagePieces.Village)structure, ObfuscationHelper.Fields.IS_ZOMBIE_INFESTED);
    }

    public Biome getBiome()
    {
        BlockPos nearest = WorldHelper.getNearestVillage(world, pos);
        return nearest != null ? world.getBiome(nearest) : Biomes.PLAINS; // just default to plains, gah
    }

    public static class Church extends VillageInnerDecorator
    {
        public Church(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
        }

        @Override
        protected Class<? extends MesonDecoratorTheme> getThemeClass()
        {
            return VillagePriestTheme.class;
        }

        @Override
        public void generate()
        {
            if (VillageDecorations.functionalBlocks) {
                if (valuable()) add(Blocks.ENCHANTING_TABLE, 2, 2, 7);
            }

            if (VillageDecorations.torches) {
                if (common()) items.addLantern(2, floor + 3, 6, true);
                items.addTorch(3, 7, 1, EnumFacing.WEST);
                items.addTorch(2, 10, 2, null);
            }

            if (VillageDecorations.decorativeBlocks) {
                if (common()) {
                    addRow(items.getBed(), 3, 5, 2, EnumFacing.NORTH, EnumFacing.NORTH);
                }
                if (common()) {
                    addRow(items.getBed(), 2, 5, 2, EnumFacing.NORTH, EnumFacing.NORTH);
                }
            }

            if (VillageDecorations.storage) {
                if (common()) {
                    items.addStorageBlock(3, 10, 1, EnumFacing.NORTH);
                }
            }
        }
    }

    public static class Field1 extends VillageInnerDecorator
    {
        public Field1(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
        }

        @Override
        public void generate()
        {
            if (VillageDecorations.torches) {
                items.addTorch(3, 1, 0, null);
                items.addTorch(3, 1, 8, null);
            }
        }
    }

    public static class Field2 extends VillageInnerDecorator
    {
        public Field2(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
        }

        @Override
        public void generate()
        {
            if (VillageDecorations.torches) {
                items.addTorch(3, 1, 0, null);
                items.addTorch(3, 1, 8, null);
                items.addTorch(9, 1, 0, null);
                items.addTorch(9, 1, 8, null);
            }
        }
    }

    /**
     * Butcher's House
     */
    public static class Hall extends VillageInnerDecorator
    {
        public Hall(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
        }

        @Override
        protected Class<? extends MesonDecoratorTheme> getThemeClass()
        {
            return VillageButcherTheme.class;
        }

        @Override
        public void generate()
        {
            if (VillageDecorations.storage) {
                if (uncommon()) items.addStorageBlock(3, floor, 4, EnumFacing.SOUTH);
            }

            if (VillageDecorations.functionalBlocks) {
                if (common()) {
                    if (chance(0.5f)) {
                        items.addFunctionalBlock(4, floor, 4, EnumFacing.SOUTH);
                    } else {
                        items.addStand(4, floor, 4, EnumFacing.SOUTH, null);
                    }
                }
            }

            if (VillageDecorations.itemFrames) {
                if (common()) items.addFramedItem(5, floor + 2, 4, EnumFacing.SOUTH);
                if (common()) items.addFramedItem(7, floor + 2, 1, EnumFacing.WEST);
                if (common()) items.addFramedItem(3, floor + 2, 1, EnumFacing.NORTH);
            }

            if (VillageDecorations.armorStands) {
                if (uncommon()) items.addArmorStand(7, floor, 4, 215.0f);
            }

            if (VillageDecorations.decorativeBlocks) {
                // decorate the little field outside
                fill(Blocks.FARMLAND.getDefaultState(), 3, 0, 6, 7, 0, 9, EnumFacing.UP, true);
                for (int xx = 3; xx <= 7; xx++) {
                    for (int zz = 6; zz <= 9; zz++) {
                        if (rand.nextFloat() < 0.4f) {
                            add(((BlockCrops) Blocks.WHEAT).withAge(6 + rand.nextInt(1)), xx, floor, zz);
                        }
                    }
                }
                add(Blocks.WATER.getDefaultState(), 5, 0, 8);
                add(Blocks.AIR.getDefaultState(), 5, 1, 8);
            }

            if (VillageDecorations.torches) {
                items.addTorch(2, floor + 1, 10, null);
                items.addTorch(8, floor + 1, 10, null);
            }
        }
    }

    /**
     * This is the librarian's house.
     */
    public static class House1 extends VillageInnerDecorator
    {
        public House1(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
        }

        @Override
        protected Class<? extends MesonDecoratorTheme> getThemeClass()
        {
            return VillageLibrarianTheme.class;
        }

        @Override
        public void generate()
        {
            if (VillageDecorations.storage) {
                // add storage next to the crafting table
                if (common()) items.addStorageBlock(7, 1, 2, EnumFacing.WEST);

                // swap out some static bookshelves for bookshelf chests
                if (Charm.hasFeature(BookshelfChest.class)) {
                    IBlockState type = BookshelfChest.bookshelfChest.getDefaultState();
                    for (int i = 0; i < 7; i++) {
                        if (uncommon()) {
                            int numberOfBooks = rand.nextInt(4) + 1;
                            type.withProperty(BlockBookshelfChest.SLOTS, numberOfBooks);
                            items.addStorageBlock(type, i + 1, 3, 4, EnumFacing.SOUTH, LootHelper.getRandomLootTable(LootHelper.RARITY.COMMON, LootHelper.TYPE.BOOK), numberOfBooks);
                        }
                    }
                }
            }

            if (VillageDecorations.torches) {
                // brighten it up
                items.addTorch(1, 4, 2, EnumFacing.EAST);
                items.addTorch(1, 4, 3, EnumFacing.EAST);
                items.addTorch(7, 4, 2, EnumFacing.WEST);
                items.addTorch(7, 4, 3, EnumFacing.WEST);
            }

            if (VillageDecorations.itemFrames) {
                if (common()) items.addFramedItem(7, 2, 1, EnumFacing.WEST);
                if (common()) items.addFramedItem(1, 3, 1, EnumFacing.NORTH);
            }
        }
    }

    /**
     * This is the square blacksmith building
     */
    public static class House2 extends VillageInnerDecorator
    {
        public House2(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
        }

        @Override
        protected Class<? extends MesonDecoratorTheme> getThemeClass()
        {
            return VillageSmithTheme.class;
        }

        @Override
        public void generate()
        {
            if (VillageDecorations.functionalBlocks) {
                // change the double slabs to an actual anvil
                items.addAnvil(8, floor, 1, chance(0.5f) ? 1 : 0, EnumFacing.EAST);
            }

            if (VillageDecorations.decorativeBlocks) {
                if (common()) {
                    // add a chimney
                    fill(Blocks.STONE_SLAB, 6, 5, 4, 8, 5, 6, true);
                    add(Blocks.AIR, 7, 4, 5);
                    add(Blocks.AIR, 8, 4, 5);
                    add(Blocks.AIR, 7, 5, 5);
                    add(Blocks.AIR, 8, 5, 5);
                    add(Blocks.MAGMA, 7, 3, 5);
                    add(Blocks.MAGMA, 8, 3, 5);
                }
            }

            if (VillageDecorations.torches) {
                // adds more torches to internal part of the building
                items.addTorch(1, 3, 3, EnumFacing.EAST);
                items.addTorch(3, 3, 5, EnumFacing.SOUTH);
            }

            if (VillageDecorations.armorStands) {
                if (uncommon()) items.addArmorStand(1, floor, 1, 40.0f);
            }
        }
    }

    /**
     * The largest "hall-like" house.
     */
    public static class House3 extends VillageInnerDecorator
    {
        public House3(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
        }

        @Override
        public void generate()
        {
            float r = rand.nextFloat();

            if (r <= 0.25f) {

                if (VillageDecorations.beds && VillageDecorations.decorativeBlocks) {
                    // add two beds with decoration by side
                    addRow(items.getBed(), 4, floor, 9, EnumFacing.NORTH, EnumFacing.NORTH);
                    addRow(items.getBed(), 6, floor, 9, EnumFacing.NORTH, EnumFacing.NORTH);
                    add(Blocks.CRAFTING_TABLE.getDefaultState(), 5, floor, 9);
                    add(Blocks.BOOKSHELF.getDefaultState(), 3, floor, 9);
                    add(Blocks.BOOKSHELF.getDefaultState(), 7, floor, 9);
                }
            } else if (r <= 0.5f) {

                if (VillageDecorations.beds && VillageDecorations.decorativeBlocks) {
                    // add twos bed along the wall
                    addRow(items.getBed(), 4, floor, 9, EnumFacing.WEST, EnumFacing.EAST);
                    add(Blocks.BOOKSHELF.getDefaultState(), 5, floor, 9);
                    addRow(items.getBed(), 6, floor, 9, EnumFacing.EAST, EnumFacing.WEST);

                    if (common()) items.addRandomBlock(6, floor, 1, EnumFacing.SOUTH);
                    if (common()) items.addRandomBlock(7, floor, 1, EnumFacing.SOUTH);
                }
            } else if (r <= 0.75f) {

                if (VillageDecorations.functionalBlocks && VillageDecorations.decorativeBlocks) {
                    if (common()) items.addFunctionalBlock(3, floor, 9, EnumFacing.SOUTH);
                    if (common()) items.addFunctionalBlock(4, floor, 9, EnumFacing.SOUTH);
                    if (uncommon()) items.addStorageBlock(5, floor, 9, EnumFacing.SOUTH);
                    if (common()) items.addFunctionalBlock(6, floor, 9, EnumFacing.SOUTH);
                    if (common()) items.addFunctionalBlock(7, floor, 9, EnumFacing.SOUTH);
                }

            } else {

                if (VillageDecorations.storage) {
                    if (common()) items.addStorageBlock(5, floor, 9, EnumFacing.SOUTH);
                    if (common()) items.addStorageBlock(6, floor, 9, EnumFacing.SOUTH);
                }

            }

            if (VillageDecorations.decorativeBlocks && VillageDecorations.functionalBlocks) {
                if (common()) items.addStand(7, floor, 5, EnumFacing.WEST, null);
                if (common()) items.addRandomBlock(7, floor, 6, EnumFacing.WEST);
                if (common()) items.addStand(4, floor, 1, EnumFacing.NORTH, null);
                if (common()) items.addRandomBlock(5, floor, 1, EnumFacing.NORTH);
            }

            if (VillageDecorations.storage) {
                // add storage
                if (uncommon()) items.addStorageBlock(1, floor, 2, EnumFacing.EAST);
                if (uncommon()) items.addStorageBlock(1, floor, 3, EnumFacing.EAST);
            }

            if (VillageDecorations.itemFrames) {
                // framed items on walls
                if (uncommon()) items.addFramedItem(6, floor + 1, 1, EnumFacing.NORTH);
                if (uncommon()) items.addFramedItem(4, floor + 1, 9, EnumFacing.SOUTH);
                if (uncommon()) items.addFramedItem(6, floor + 1, 9, EnumFacing.SOUTH);
            }

            if (VillageDecorations.functionalBlocks) {
                // functional blocks on far right wall
                if (common()) items.addFunctionalBlock(7, floor, 2, EnumFacing.WEST);
                if (common()) items.addFunctionalBlock(7, floor, 3, EnumFacing.WEST);
            }

            if (VillageDecorations.carpet) {
                // add a cosy rug
                items.addRug(floor, 3, 3, 6, 6, null);
            }

            if (VillageDecorations.torches) {
                // adds more torches so it's not really dark by default
                items.addTorch(5, 3, 9, EnumFacing.SOUTH);
                items.addTorch(7, 3, 5, EnumFacing.WEST);
                items.addTorch(3, 3, 5, EnumFacing.EAST);
                if (uncommon()) items.addLantern(5, 5, 5, true);
            }
        }
    }

    /**
     * This is a wooden hut, 5x5.  Spawns either normally or with ladder.
     * When there is a ladder, isRoofAccessible is true.
     */
    public static class House4 extends VillageInnerDecorator
    {
        public House4(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
        }

        @Override
        public void generate()
        {
            if (chance(0.5f)) {
                if (VillageDecorations.beds) {
                    addRow(items.getBed(), 2, 1, 3, EnumFacing.WEST, EnumFacing.EAST);
                }
                if (VillageDecorations.carpet) {
                    items.addRug(1, 1, 2, 2, 2, null);
                }
            } else {
                if (VillageDecorations.storage) {
                    // add storage in the corner
                    if (uncommon()) items.addStorageBlock(1, 1, 3, EnumFacing.SOUTH);
                    if (uncommon()) items.addStorageBlock(2, 1, 3, EnumFacing.SOUTH);
                }
            }

            // add a door
            if (common() && Charm.hasFeature(MoreVillageBiomes.class)) {
                BlockPlanks.EnumType wood = MoreVillageBiomes.getWoodForBiome(biome);
                items.addDoor(2, 1, 0, EnumFacing.NORTH, MoreVillageBiomes.getDoorForWood(wood));
            }

            if (isRoofAccessible()) {
                if (VillageDecorations.storage) {
                    // add storage on the roof
                    if (uncommon()) items.addStorageBlock(1, 5, 3, EnumFacing.EAST);
                    if (uncommon()) items.addStorageBlock(3, 5, 1, EnumFacing.NORTH);
                }
            } else {
                if (VillageDecorations.itemFrames) {
                    if (common()) items.addFramedItem(3, 3, 3, EnumFacing.SOUTH);
                }
            }

            if (VillageDecorations.torches) {
                // add torches to lighten it
                items.addTorch(0, 6, 0, null);
                items.addTorch(4, 6, 4, null);
                if (uncommon()) items.addLantern(2, floor + 2, 2, true);
            }

            if (VillageDecorations.itemFrames) {
                if (common()) items.addFramedItem(1, 3, 3, EnumFacing.SOUTH);
                if (common()) items.addFramedItem(1, 3, 1, EnumFacing.NORTH);
                if (common()) items.addFramedItem(3, 3, 1, EnumFacing.NORTH);
            }

            if (VillageDecorations.armorStands) {
                if (valuable()) items.addArmorStand(3, floor, 1, 135.0f);
            }
        }

        public boolean isRoofAccessible()
        {
            return ReflectionHelper.getPrivateValue(StructureVillagePieces.House4Garden.class, (StructureVillagePieces.House4Garden)structure, ObfuscationHelper.Fields.IS_ROOF_ACCESSIBLE);
        }
    }

    public static class Well extends VillageInnerDecorator
    {
        public Well(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
        }

        @Override
        public void generate()
        {
            if (VillageDecorations.carpet) {
                // decorated well
                int roof = 16;
                items.addRug(roof, 1, 1, 4, 4, getVillageRand());
            }

            if (common()) {
                IBlockState ore = null;

                switch (getRand().nextInt(4)) {
                    case 0: ore = Blocks.IRON_ORE.getDefaultState(); break;
                    case 1: ore = Blocks.GOLD_ORE.getDefaultState(); break;
                    case 2: ore = Blocks.EMERALD_ORE.getDefaultState(); break;
                    case 3: ore = Blocks.DIAMOND_ORE.getDefaultState(); break;
                }

                if (ore != null) {
                    add(ore, 2, 0, 2, EnumFacing.NORTH);
                }
            }
        }
    }

    /**
     * This is the smallest building, inner floor-space of 2 by 3.
     */
    public static class WoodHut extends VillageInnerDecorator
    {
        public WoodHut(StructureVillagePieces.Village structure, World world, StructureBoundingBox box)
        {
            super(structure, world, box);
            roof = 5;
        }

        @Override
        public void generate()
        {
            if (common()) {
                // make a proper floor
                fill(Blocks.COBBLESTONE, 1, 0, 1, 2, 0, 3, true);
            }

            if (chance(0.5f)) {
                if (VillageDecorations.beds) {
                    // remove the table and put a bed in there
                    addRow(items.getBed(), 2, 1, 3, EnumFacing.WEST, EnumFacing.EAST);
                    add(Blocks.AIR, 1, 2, 3);
                    add(Blocks.AIR, 2, 2, 3);
                }

                if (VillageDecorations.carpet) {
                    items.addRug(1, 1, 2, 2, 2, null);
                }
            } else {
                if (VillageDecorations.storage) {
                    if (uncommon()) {
                        items.addStorageBlock(1, 1, 3, EnumFacing.SOUTH);
                        add(Blocks.AIR, 1, 2, 3);
                    }
                }
            }

            if (chance(0.5f)) {
                if (VillageDecorations.functionalBlocks) {
                    if (common()) {
                        if (chance(0.5f)) {
                            items.addFunctionalBlock(2, 1, 1, EnumFacing.EAST);
                        } else {
                            items.addStand(2, 1, 1, EnumFacing.EAST, null);
                        }
                    }
                }
            } else {
                if (VillageDecorations.armorStands) {
                    if (valuable()) items.addArmorStand(2, floor, 1, 135.0f);
                }
            }

            if (VillageDecorations.itemFrames) {
                // add item frames to walls
                if (common()) items.addFramedItem(1, floor + 2, 3, EnumFacing.SOUTH);
            }

            if (VillageDecorations.torches) {
                // add torches to lighten it
                items.addTorch(2, floor + 2, 3, EnumFacing.SOUTH);
            }
        }
    }
}
