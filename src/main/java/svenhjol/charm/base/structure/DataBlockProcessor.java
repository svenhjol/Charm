package svenhjol.charm.base.structure;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.Structure.StructureBlockInfo;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldView;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.helper.DecorationHelper;
import svenhjol.charm.block.BookcaseBlock;
import svenhjol.charm.blockentity.BookcaseBlockEntity;
import svenhjol.charm.blockentity.EntitySpawnerBlockEntity;
import svenhjol.charm.module.*;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

import static svenhjol.charm.base.helper.DataBlockHelper.*;
import static svenhjol.charm.base.helper.DecorationHelper.*;

@SuppressWarnings("unused")
public class DataBlockProcessor extends StructureProcessor {
    public DataBlockResolver resolver = new DataBlockResolver();
    public static Map<String, Consumer<DataBlockResolver>> callbacks = new HashMap<>();

    @Nullable
    @Override
    public StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos blockPos, StructureBlockInfo unused, StructureBlockInfo blockInfo, StructurePlacementData placement) {
        if (blockInfo.state.getBlock() == Blocks.STRUCTURE_BLOCK) {
            StructureBlockMode mode = StructureBlockMode.valueOf(blockInfo.tag.getString("mode"));
            if (mode == StructureBlockMode.DATA) {
                return resolver.replace(world, placement.getRotation(), blockInfo, new Random(pos.asLong()));
            }
        }

        return blockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return null;
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
        public static float BOOKCASE_CHANCE = 0.15F;
        public static float BOOKCASE_LOOT_CHANCE = 0.4F;
        public static float CHEST_CHANCE = 0.66F;
        public static float DECORATION_CHANCE = 0.85F;
        public static float FLOWER_CHANCE = 0.8F;
        public static float FLOWERPOT_CHANCE = 0.8F;
        public static float LANTERN_CHANCE = 0.9F;
        public static float LANTERN_GOLD_CHANCE = 0.25F;
        public static float LAVA_CHANCE = 0.7F;
        public static float MOB_CHANCE = 0.75F;
        public static float ORE_CHANCE = 0.75F;
        public static float RARE_ORE_CHANCE = 0.25F;
        public static float RARE_CHEST_CHANCE = 0.1F;
        public static float RUBBLE_CHANCE = 0.9F;
        public static float RUNESTONE_CHANCE = 0.75F;
        public static float SAPLING_CHANCE = 0.8F;
        public static float SPAWNER_CHANCE = 0.8F;
        public static float STORAGE_CHANCE = 0.7F;

        public String data;
        public BlockRotation rotation;
        public BlockState state;
        public BlockPos pos;
        public WorldView world;
        public CompoundTag tag;
        public Random fixedRandom; // fixed according to parent template
        public Random random; // random according to the replaced block hashcode
        public float chance;

        public StructureBlockInfo replace(WorldView world, BlockRotation rotation, StructureBlockInfo blockInfo, Random random) {
            String data = blockInfo.tag.getString("metadata");
            this.world = world;
            this.fixedRandom = random;
            this.rotation = rotation;
            this.pos = blockInfo.pos;
            this.state = null;
            this.tag = null;
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
                    this.state = Blocks.AIR.getDefaultState();
            }

            return new StructureBlockInfo(this.pos, this.state, this.tag);
        }

        protected void anvil() {
            float f = random.nextFloat();
            if (f < 0.33F) {
                this.state = Blocks.ANVIL.getDefaultState();
            } else if (f < 0.66F) {
                this.state = Blocks.CHIPPED_ANVIL.getDefaultState();
            } else if (f < 1.0F) {
                this.state = Blocks.DAMAGED_ANVIL.getDefaultState();
            }
        }

        protected void armorStand() {
            EntitySpawnerBlockEntity blockEntity = EntitySpawner.BLOCK_ENTITY.instantiate(BlockPos.ORIGIN, EntitySpawner.ENTITY_SPAWNER.getDefaultState());
            if (blockEntity == null) return;
            this.tag = new CompoundTag();

            blockEntity.entity = new Identifier("minecraft:armor_stand");
            blockEntity.meta = this.data;
            blockEntity.rotation = this.rotation;
            blockEntity.toTag(this.tag);

            this.state = EntitySpawner.ENTITY_SPAWNER.getDefaultState();
        }

        protected void block() {
            if (!withChance(BLOCK_CHANCE)) return;

            String type = getValue("type", this.data, "");
            if (type.isEmpty()) return;

            Identifier typeId = new Identifier(type);
            Optional<Block> optionalBlock = Registry.BLOCK.getOrEmpty(typeId);

            if (!optionalBlock.isPresent())
                return;

            Block block = optionalBlock.get();
            this.state = block.getDefaultState();
        }

        protected void bookshelf() {
            IVariantMaterial variantMaterial = DecorationHelper.getRandomVariantMaterial(fixedRandom);

            if (ModuleHandler.enabled("charm:bookcases") && withChance(BOOKCASE_CHANCE)) {
                state = Bookcases.BOOKCASE_BLOCKS.get(variantMaterial).getDefaultState()
                    .with(BookcaseBlock.SLOTS, BookcaseBlockEntity.SIZE); // make it have the "full" texture

                if (random.nextFloat() < BOOKCASE_LOOT_CHANCE) {
                    BookcaseBlockEntity blockEntity = Bookcases.BLOCK_ENTITY.instantiate(BlockPos.ORIGIN, Bookcases.BOOKCASE_BLOCKS.get(VanillaVariantMaterial.OAK).getDefaultState());
                    if (blockEntity == null)
                        return;

                    blockEntity.setLootTable(DecorationHelper.getRandomLootTable(BOOKCASE_LOOT_TABLES, random), random.nextLong());
                    this.tag = new CompoundTag();
                    blockEntity.toTag(this.tag);
                }
            } else if (ModuleHandler.enabled("charm:variant_bookshelves") && variantMaterial != VanillaVariantMaterial.OAK) {
                state = VariantBookshelves.BOOKSHELF_BLOCKS.get(variantMaterial).getDefaultState();
            } else {
                state = Blocks.BOOKSHELF.getDefaultState();
            }
        }

        protected void carpet() {
            List<Block> types = new ArrayList<>(DecorationHelper.CARPETS);
            Collections.shuffle(types, fixedRandom);

            int type = getValue("type", this.data, 0);
            if (type > types.size()) type = 0;
            state = types.get(type).getDefaultState();
        }

        protected void cauldron() {
            state = Blocks.CAULDRON.getDefaultState();
        }

        protected void chest() {
            if (!withChance(CHEST_CHANCE)) return;

            if (ModuleHandler.enabled("charm:variant_chests")) {
                IVariantMaterial variantMaterial = DecorationHelper.getRandomVariantMaterial(random);

                state = random.nextFloat() < 0.1F ?
                    VariantChests.TRAPPED_CHEST_BLOCKS.get(variantMaterial).getDefaultState() :
                    VariantChests.NORMAL_CHEST_BLOCKS.get(variantMaterial).getDefaultState();

            } else {
                state = Blocks.CHEST.getDefaultState();
            }

            state = setFacing(state, ChestBlock.FACING, getValue("facing", data, "north"));

            Identifier lootTable = DecorationHelper.getRandomLootTable(random.nextFloat() < RARE_CHEST_CHANCE ? RARE_CHEST_LOOT_TABLES : CHEST_LOOT_TABLES, random);
            ChestBlockEntity blockEntity = BlockEntityType.CHEST.instantiate(BlockPos.ORIGIN, Blocks.CHEST.getDefaultState());
            if (blockEntity == null)
                return;

            blockEntity.setLootTable(getLootTable(data, lootTable), random.nextLong());
            tag = new CompoundTag();
            blockEntity.toTag(tag);
        }

        protected void decoration() {
            if (!withChance(DECORATION_CHANCE)) return;

            Direction facing = getFacing(getValue("facing", this.data, "north"));
            state = DecorationHelper.getRandomBlock(DECORATION_BLOCKS, random, facing);
        }

        protected void entity() {
            EntitySpawnerBlockEntity blockEntity = EntitySpawner.BLOCK_ENTITY.instantiate(BlockPos.ORIGIN, EntitySpawner.ENTITY_SPAWNER.getDefaultState());
            if (blockEntity == null) return;
            tag = new CompoundTag();

            String type = getValue("type", this.data, "");
            if (type.isEmpty()) return;

            Identifier typeId = new Identifier(type);

            if (!Registry.ENTITY_TYPE.getOrEmpty(typeId).isPresent())
                return;

            blockEntity.entity = typeId;
            blockEntity.meta = this.data;
            blockEntity.rotation = this.rotation;
            blockEntity.toTag(this.tag);

            this.state = EntitySpawner.ENTITY_SPAWNER.getDefaultState();
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
            state = Blocks.LANTERN.getDefaultState();

            if (ModuleHandler.enabled("charm:gold_lanterns") && random.nextFloat() < LANTERN_GOLD_CHANCE)
                state = GoldLanterns.GOLD_LANTERN.getDefaultState();

            if (data.contains("hanging"))
                state = state.with(LanternBlock.HANGING, true);
        }

        protected void lava() {
            state = Blocks.MAGMA_BLOCK.getDefaultState();

            if (fixedRandom.nextFloat() < LAVA_CHANCE)
                state = Blocks.LAVA.getDefaultState();
        }

        protected void mob() {
            if (!withChance(MOB_CHANCE)) return;

            EntitySpawnerBlockEntity blockEntity = EntitySpawner.BLOCK_ENTITY.instantiate(BlockPos.ORIGIN, EntitySpawner.ENTITY_SPAWNER.getDefaultState());
            if (blockEntity == null) return;

            String type = getValue("type", this.data, "");
            if (type.isEmpty()) return;
            tag = new CompoundTag();

            blockEntity.entity = new Identifier(type);
            blockEntity.health = getValue("health", this.data, 0.0D);
            blockEntity.persist = getValue("persist", this.data, true);
            blockEntity.count = getValue("count", this.data, 1);
            blockEntity.rotation = this.rotation;
            blockEntity.toTag(this.tag);

            this.state = EntitySpawner.ENTITY_SPAWNER.getDefaultState();
        }

        protected void ore() {
            if (!withChance(ORE_CHANCE)) return;

            String type = getValue("type", this.data, "");
            if (!type.isEmpty()) {
                Identifier typeId = new Identifier(type);
                if (!Registry.ENTITY_TYPE.getOrEmpty(typeId).isPresent())
                    return;

                Block ore = Registry.BLOCK.get(typeId);
                state = ore.getDefaultState();
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
                Identifier typeId = new Identifier(type);
                if (!Registry.ENTITY_TYPE.getOrEmpty(typeId).isPresent())
                    return;

                entity = Registry.ENTITY_TYPE.get(typeId);
            }

            if (entity == null)
                return;

            state = Blocks.SPAWNER.getDefaultState();

            MobSpawnerBlockEntity blockEntity = BlockEntityType.MOB_SPAWNER.instantiate(BlockPos.ORIGIN, Blocks.SPAWNER.getDefaultState());
            if (blockEntity != null) {
                blockEntity.getLogic().setEntityId(entity);
                tag = new CompoundTag();
                blockEntity.toTag(this.tag);
            }
        }

        protected void storage() {
            if (!withChance(STORAGE_CHANCE)) return;

            LootableContainerBlockEntity blockEntity;
            IVariantMaterial woodType = DecorationHelper.getRandomVariantMaterial(random);

            if (random.nextFloat() < 0.5F && ModuleHandler.enabled("charm:crates")) {
                // get a crate
                state = Crates.CRATE_BLOCKS.get(woodType).getDefaultState();
                blockEntity = Crates.BLOCK_ENTITY.instantiate(BlockPos.ORIGIN, Crates.CRATE_BLOCKS.get(VanillaVariantMaterial.OAK).getDefaultState());
            } else {
                // get a barrel
                if (ModuleHandler.enabled("charm:variant_barrels")) {
                    // get variant barrel
                    state = VariantBarrels.BARREL_BLOCKS.get(woodType).getDefaultState();
                } else {
                    // get vanilla barrel
                    state = Blocks.BARREL.getDefaultState();
                }
                state = state.with(BarrelBlock.FACING, Direction.UP);
                blockEntity = BlockEntityType.BARREL.instantiate(BlockPos.ORIGIN, Blocks.BARREL.getDefaultState());
            }

            if (blockEntity == null)
                return;

            Identifier lootTable = DecorationHelper.getRandomLootTable(COMMON_LOOT_TABLES, random);
            blockEntity.setLootTable(getLootTable(data, lootTable), random.nextLong());
            tag = new CompoundTag();
            blockEntity.toTag(tag);
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
