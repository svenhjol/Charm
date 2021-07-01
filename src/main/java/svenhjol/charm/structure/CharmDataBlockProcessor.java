package svenhjol.charm.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import svenhjol.charm.Charm;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.helper.DecorationHelper;
import svenhjol.charm.helper.LootHelper;
import svenhjol.charm.init.CharmStructures;
import svenhjol.charm.module.bookcases.BookcaseBlock;
import svenhjol.charm.module.bookcases.BookcaseBlockEntity;
import svenhjol.charm.module.bookcases.Bookcases;
import svenhjol.charm.module.entity_spawners.EntitySpawnerBlockEntity;
import svenhjol.charm.module.entity_spawners.EntitySpawners;
import svenhjol.charm.module.variant_barrels.VariantBarrels;
import svenhjol.charm.module.variant_bookshelves.VariantBookshelves;
import svenhjol.charm.module.variant_chests.VariantChests;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static svenhjol.charm.helper.DataBlockHelper.*;
import static svenhjol.charm.helper.DecorationHelper.*;

@SuppressWarnings("unused")
public class CharmDataBlockProcessor extends StructureProcessor {
    public DataBlockResolver resolver = new DataBlockResolver();
    public static Map<String, Consumer<DataBlockResolver>> callbacks = new HashMap<>();

    @Nullable
    @Override
    public StructureBlockInfo processBlock(LevelReader world, BlockPos pos, BlockPos blockPos, StructureBlockInfo unused, StructureBlockInfo blockInfo, StructurePlaceSettings placement) {
        if (blockInfo.state.getBlock() == Blocks.STRUCTURE_BLOCK) {
            StructureMode mode = StructureMode.valueOf(blockInfo.nbt.getString("mode"));
            if (mode == StructureMode.DATA) {
                return resolver.replace(world, placement.getRotation(), blockInfo, new Random(pos.asLong()));
            }
        }

        return blockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return CharmStructures.DATA_BLOCK_PROCESSOR_TYPE;
    }

    public static class DataBlockResolver {
        private static final String ANVIL = "anvil";
        private static final String ARMOR = "armor";
        private static final String BLOCK = "block";
        private static final String BOOKSHELF = "bookshelf";
        private static final String CARPET = "carpet";
        private static final String CAULDRON = "cauldron";
        private static final String CHEST = "chest";
        private static final String DECORATION = "decoration";
        private static final String ENTITY = "entity";
        private static final String FLOWER = "flower";
        private static final String LANTERN = "lantern";
        private static final String LAVA = "lava";
        private static final String MOB = "mob";
        private static final String ORE = "ore";
        private static final String FLOWERPOT = "plantpot";
        private static final String SAPLING = "sapling";
        private static final String SPAWNER = "spawner";
        private static final String STORAGE = "storage";

        public static float BLOCK_CHANCE = 0.8F;
        public static float BOOKCASE_CHANCE = 0.13F;
        public static float CHEST_CHANCE = 0.66F;
        public static float DECORATION_CHANCE = 0.85F;
        public static float FLOWER_CHANCE = 0.8F;
        public static float FLOWERPOT_CHANCE = 0.8F;
        public static float LANTERN_CHANCE = 0.9F;
        public static float LAVA_CHANCE = 0.7F;
        public static float MOB_CHANCE = 0.75F;
        public static float ORE_CHANCE = 0.75F;
        public static float RARE_ORE_CHANCE = 0.25F;
        public static float RARE_BOOKCASE_CHANCE = 0.05F;
        public static float RARE_CHEST_CHANCE = 0.1F;
        public static float RUBBLE_CHANCE = 0.9F;
        public static float RUNESTONE_CHANCE = 0.75F;
        public static float SAPLING_CHANCE = 0.8F;
        public static float SPAWNER_CHANCE = 0.8F;
        public static float STORAGE_CHANCE = 0.66F;

        public String data;
        public Rotation rotation;
        public BlockState state;
        public BlockPos pos;
        public LevelReader world;
        public CompoundTag nbt;
        public Random fixedRandom; // fixed according to parent template
        public Random random; // random according to the replaced block hashcode
        public float chance;

        public StructureBlockInfo replace(LevelReader world, Rotation rotation, StructureBlockInfo blockInfo, Random random) {
            String data = blockInfo.nbt.getString("metadata");
            this.world = world;
            this.fixedRandom = random;
            this.rotation = rotation;
            this.pos = blockInfo.pos;
            this.state = null;
            this.nbt = null;
            this.random = new Random(blockInfo.hashCode());

            // pipe character acts as an OR. Data will use one of the definitions at random.
            if (data.contains("|")) {
                String[] split = data.split("\\|");
                data = split[this.random.nextInt(split.length)];
            }

            this.data = data.trim();
            this.chance = getChance(this.data, 0.0F);

            if (this.data.startsWith(ANVIL)) anvil();
            if (this.data.startsWith(ARMOR)) armorStand();
            if (this.data.startsWith(BLOCK)) block();
            if (this.data.startsWith(BOOKSHELF)) bookshelf();
            if (this.data.startsWith(CARPET)) carpet();
            if (this.data.startsWith(CAULDRON)) cauldron();
            if (this.data.startsWith(CHEST)) chest();
            if (this.data.startsWith(DECORATION)) decoration();
            if (this.data.startsWith(ENTITY)) entity();
            if (this.data.startsWith(FLOWER)) flower();
            if (this.data.startsWith(FLOWERPOT)) flowerpot();
            if (this.data.startsWith(LANTERN)) lantern();
            if (this.data.startsWith(LAVA)) lava();
            if (this.data.startsWith(MOB)) mob();
            if (this.data.startsWith(ORE)) ore();
            if (this.data.startsWith(SAPLING)) sapling();
            if (this.data.startsWith(SPAWNER)) spawner();
            if (this.data.startsWith(STORAGE)) storage();

            if (this.state == null) {
                callbacks.entrySet().stream().filter(entry -> this.data.startsWith(entry.getKey())).forEach(entry ->
                    entry.getValue().accept(this));

                if (this.state == null)
                    this.state = Blocks.AIR.defaultBlockState();
            }

            return new StructureBlockInfo(this.pos, this.state, this.nbt);
        }

        protected void anvil() {
            float f = random.nextFloat();
            if (f < 0.33F) {
                this.state = Blocks.ANVIL.defaultBlockState();
            } else if (f < 0.66F) {
                this.state = Blocks.CHIPPED_ANVIL.defaultBlockState();
            } else if (f < 1.0F) {
                this.state = Blocks.DAMAGED_ANVIL.defaultBlockState();
            }
        }

        protected void armorStand() {
            EntitySpawnerBlockEntity blockEntity = EntitySpawners.BLOCK_ENTITY.create(BlockPos.ZERO, EntitySpawners.ENTITY_SPAWNER.defaultBlockState());
            if (blockEntity == null) return;
            this.nbt = new CompoundTag();

            blockEntity.entity = new ResourceLocation("minecraft:armor_stand");
            blockEntity.meta = this.data;
            blockEntity.rotation = this.rotation;
            blockEntity.save(this.nbt);

            this.state = EntitySpawners.ENTITY_SPAWNER.defaultBlockState();
        }

        protected void block() {
            if (!withChance(BLOCK_CHANCE)) return;

            String type = getValue("type", this.data, "");
            if (type.isEmpty()) return;

            ResourceLocation typeId = new ResourceLocation(type);
            Optional<Block> optionalBlock = Registry.BLOCK.getOptional(typeId);

            if (!optionalBlock.isPresent())
                return;

            Block block = optionalBlock.get();
            this.state = block.defaultBlockState();
        }

        protected void bookshelf() {
            IVariantMaterial variantMaterial = DecorationHelper.getRandomOverworldVariantMaterial(fixedRandom);

            String type = getValue("material", this.data, "");
            if (!type.isEmpty()) {
                IVariantMaterial material = DecorationHelper.getVariantMaterial(type);
                if (material != null)
                    variantMaterial = material;
            }

            if (Charm.LOADER.isEnabled("charm:bookcases") && withChance(BOOKCASE_CHANCE)) {
                state = Bookcases.BOOKCASE_BLOCKS.get(variantMaterial).defaultBlockState()
                    .setValue(BookcaseBlock.SLOTS, BookcaseBlockEntity.SIZE); // make it have the "full" texture

                BookcaseBlockEntity blockEntity = Bookcases.BLOCK_ENTITY.create(BlockPos.ZERO, Bookcases.BOOKCASE_BLOCKS.get(variantMaterial).defaultBlockState());
                if (blockEntity == null)
                    return;

                ResourceLocation lootTable = DecorationHelper.getRandomLootTable(random.nextFloat() < RARE_BOOKCASE_CHANCE ? RARE_BOOKCASE_LOOT_TABLES : BOOKCASE_LOOT_TABLES, random);
                blockEntity.setLootTable(lootTable, random.nextLong());

                this.nbt = new CompoundTag();
                blockEntity.save(this.nbt);
            } else if (Charm.LOADER.isEnabled("charm:variant_bookshelves") && variantMaterial != VanillaVariantMaterial.OAK) {
                state = VariantBookshelves.BOOKSHELF_BLOCKS.get(variantMaterial).defaultBlockState();
            } else {
                state = Blocks.BOOKSHELF.defaultBlockState();
            }
        }

        protected void carpet() {
            List<Block> types = new ArrayList<>(DecorationHelper.CARPETS);
            Collections.shuffle(types, fixedRandom);

            int type = getValue("type", this.data, 0);
            if (type > types.size()) type = 0;
            state = types.get(type).defaultBlockState();
        }

        protected void cauldron() {
            state = Blocks.CAULDRON.defaultBlockState();
        }

        protected void chest() {
            if (!withChance(CHEST_CHANCE)) return;

            if (Charm.LOADER.isEnabled("charm:variant_chests")) {
                IVariantMaterial variantMaterial = DecorationHelper.getRandomOverworldVariantMaterial(random);

                String type = getValue("material", this.data, "");
                if (!type.isEmpty()) {
                    IVariantMaterial material = DecorationHelper.getVariantMaterial(type);
                    if (material != null)
                        variantMaterial = material;
                }

                state = random.nextFloat() < 0.05F ?
                    VariantChests.TRAPPED_CHEST_BLOCKS.get(variantMaterial).defaultBlockState() :
                    VariantChests.NORMAL_CHEST_BLOCKS.get(variantMaterial).defaultBlockState();

            } else {
                state = Blocks.CHEST.defaultBlockState();
            }

            state = setFacing(state, ChestBlock.FACING, getValue("facing", data, "north"));

            ResourceLocation lootTable = DecorationHelper.getRandomLootTable(random.nextFloat() < RARE_CHEST_CHANCE ? RARE_CHEST_LOOT_TABLES : CHEST_LOOT_TABLES, random);
            ChestBlockEntity blockEntity = BlockEntityType.CHEST.create(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
            if (blockEntity == null)
                return;

            String loot = getValue("loot", data, "");
            blockEntity.setLootTable(LootHelper.getLootTable(loot, lootTable), random.nextLong());
            nbt = new CompoundTag();
            blockEntity.save(nbt);
        }

        protected void decoration() {
            if (!withChance(DECORATION_CHANCE)) return;

            Direction facing = getFacing(getValue("facing", this.data, "north"));
            state = DecorationHelper.getRandomBlock(DECORATION_BLOCKS, random, facing);
        }

        protected void entity() {
            EntitySpawnerBlockEntity blockEntity = EntitySpawners.BLOCK_ENTITY.create(BlockPos.ZERO, EntitySpawners.ENTITY_SPAWNER.defaultBlockState());
            if (blockEntity == null) return;
            nbt = new CompoundTag();

            String type = getValue("type", this.data, "");
            if (type.isEmpty()) return;

            ResourceLocation typeId = new ResourceLocation(type);

            if (!Registry.ENTITY_TYPE.getOptional(typeId).isPresent())
                return;

            blockEntity.entity = typeId;
            blockEntity.meta = this.data;
            blockEntity.rotation = this.rotation;
            blockEntity.save(this.nbt);

            this.state = EntitySpawners.ENTITY_SPAWNER.defaultBlockState();
        }

        protected void flower() {
            if (!withChance(FLOWER_CHANCE)) return;
            state = DecorationHelper.getRandomBlock(FLOWERS, random);
        }

        protected void flowerpot() {
            if (!withChance(FLOWERPOT_CHANCE)) return;
            state = DecorationHelper.getRandomBlock(FLOWER_POTS, random);
        }

        protected void lantern() {
            if (!withChance(LANTERN_CHANCE)) return;
            state = Blocks.LANTERN.defaultBlockState();

            if (data.contains("hanging"))
                state = state.setValue(LanternBlock.HANGING, true);
        }

        protected void lava() {
            state = Blocks.MAGMA_BLOCK.defaultBlockState();

            if (fixedRandom.nextFloat() < LAVA_CHANCE)
                state = Blocks.LAVA.defaultBlockState();
        }

        protected void mob() {
            if (!withChance(MOB_CHANCE)) return;

            EntitySpawnerBlockEntity blockEntity = EntitySpawners.BLOCK_ENTITY.create(BlockPos.ZERO, EntitySpawners.ENTITY_SPAWNER.defaultBlockState());
            if (blockEntity == null) return;

            String type = getValue("type", this.data, "");
            if (type.isEmpty()) return;
            nbt = new CompoundTag();

            blockEntity.entity = new ResourceLocation(type);
            blockEntity.health = getValue("health", this.data, 0.0D);
            blockEntity.persist = getValue("persist", this.data, true);
            blockEntity.armor = getValue("armor", this.data, "");
            blockEntity.effects = getValue("effects", this.data, "");
            blockEntity.count = getValue("count", this.data, 1);
            blockEntity.rotation = this.rotation;
            blockEntity.save(this.nbt);

            this.state = EntitySpawners.ENTITY_SPAWNER.defaultBlockState();
        }

        protected void ore() {
            if (!withChance(ORE_CHANCE)) return;

            String type = getValue("type", this.data, "");
            if (!type.isEmpty()) {
                ResourceLocation typeId = new ResourceLocation(type);
                if (!Registry.ENTITY_TYPE.getOptional(typeId).isPresent())
                    return;

                Block ore = Registry.BLOCK.get(typeId);
                state = ore.defaultBlockState();
                return;
            }

            state = fixedRandom.nextFloat() < RARE_ORE_CHANCE ?
                DecorationHelper.getRandomBlock(RARE_ORES, fixedRandom) :
                DecorationHelper.getRandomBlock(COMMON_ORES, fixedRandom);
        }

        protected void sapling() {
            if (!withChance(SAPLING_CHANCE)) return;
            state = DecorationHelper.getRandomBlock(SAPLINGS, random);
        }

        protected void spawner() {
            if (!withChance(SPAWNER_CHANCE)) return;

            EntityType<?> entity;
            String type = getValue("type", this.data, "");
            if (type.isEmpty()) {
                // get random spawner mob
                entity = SPAWNER_MOBS.size() > 0 ? SPAWNER_MOBS.get(random.nextInt(SPAWNER_MOBS.size())) : null;
            } else {
                // try and use the specified entity
                ResourceLocation typeId = new ResourceLocation(type);
                if (!Registry.ENTITY_TYPE.getOptional(typeId).isPresent())
                    return;

                entity = Registry.ENTITY_TYPE.get(typeId);
            }

            if (entity == null)
                return;

            state = Blocks.SPAWNER.defaultBlockState();

            SpawnerBlockEntity blockEntity = BlockEntityType.MOB_SPAWNER.create(BlockPos.ZERO, Blocks.SPAWNER.defaultBlockState());
            if (blockEntity != null) {
                blockEntity.getSpawner().setEntityId(entity);
                nbt = new CompoundTag();
                blockEntity.save(this.nbt);
            }
        }

        protected void storage() {
            if (!withChance(STORAGE_CHANCE)) return;

            RandomizableContainerBlockEntity blockEntity;
            IVariantMaterial woodType = DecorationHelper.getRandomOverworldVariantMaterial(random);

            if (Charm.LOADER.isEnabled("charm:variant_barrels")) {
                // get variant barrel
                state = VariantBarrels.BARREL_BLOCKS.get(woodType).defaultBlockState();
            } else {
                // get vanilla barrel
                state = Blocks.BARREL.defaultBlockState();
            }
            state = state.setValue(BarrelBlock.FACING, Direction.UP);
            blockEntity = BlockEntityType.BARREL.create(BlockPos.ZERO, Blocks.BARREL.defaultBlockState());

            if (blockEntity == null)
                return;

            ResourceLocation lootTable = DecorationHelper.getRandomLootTable(COMMON_LOOT_TABLES, random);

            String loot = getValue("loot", data, "");
            blockEntity.setLootTable(LootHelper.getLootTable(loot, lootTable), random.nextLong());
            nbt = new CompoundTag();
            blockEntity.save(nbt);
        }

        public boolean withChance(float chance) {
            float f = this.random.nextFloat();
            return this.chance > 0 ? f < this.chance : f < chance;
        }

        public float getChance(String data, float fallback) {
            int i = getValue("chance", data, 0);
            return i == 0 ? fallback : ((float) i) / 100.0F;
        }
    }
}
