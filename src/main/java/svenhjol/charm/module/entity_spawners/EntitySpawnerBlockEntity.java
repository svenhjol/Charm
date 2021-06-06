package svenhjol.charm.module.entity_spawners;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Difficulty;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.AABB;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.DataBlockHelper;
import svenhjol.charm.helper.LootHelper;

import java.util.*;

public class EntitySpawnerBlockEntity extends BlockEntity {
    private final static String ENTITY = "entity";
    private final static String PERSIST = "persist";
    private final static String HEALTH = "health";
    private final static String ARMOR = "armor";
    private final static String EFFECTS = "effects";
    private final static String META = "meta";
    private final static String COUNT = "count";
    private final static String ROTATION = "rotation";

    public ResourceLocation entity = null;
    public Rotation rotation = Rotation.NONE;
    public boolean persist = false;
    public double health = 0;
    public int count = 1;
    public String effects = "";
    public String armor = "";
    public String meta = "";

    public EntitySpawnerBlockEntity(BlockPos pos, BlockState state) {
        super(EntitySpawners.BLOCK_ENTITY, pos, state);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.entity = ResourceLocation.tryParse(nbt.getString(ENTITY));
        this.persist = nbt.getBoolean(PERSIST);
        this.health = nbt.getDouble(HEALTH);
        this.count = nbt.getInt(COUNT);
        this.effects = nbt.getString(EFFECTS);
        this.armor = nbt.getString(ARMOR);
        this.meta = nbt.getString(META);

        String rot = nbt.getString(ROTATION);
        this.rotation = rot.isEmpty() ? Rotation.NONE : Rotation.valueOf(rot);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);

        nbt.putString(ENTITY, entity.toString());
        nbt.putString(ROTATION, rotation.name());
        nbt.putBoolean(PERSIST, persist);
        nbt.putDouble(HEALTH, health);
        nbt.putInt(COUNT, count);
        nbt.putString(EFFECTS, effects);
        nbt.putString(ARMOR, armor);
        nbt.putString(META, meta);

        return nbt;
    }

    public static <T extends EntitySpawnerBlockEntity> void tick(Level world, BlockPos pos, BlockState state, T entitySpawner) {
        if (world == null || world.getGameTime() % 10 == 0 || world.getDifficulty() == Difficulty.PEACEFUL)
            return;

        List<Player> players = world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(EntitySpawners.triggerDistance));

        if (players.size() == 0)
            return;

        // remove the spawner, create the entity
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
        boolean result = trySpawn(world, entitySpawner.worldPosition, entitySpawner);

        if (result) {
            Charm.LOG.debug("EntitySpawner spawned entity " + entitySpawner.entity.toString() + " at pos: " + pos);
        } else {
            Charm.LOG.debug("EntitySpawner failed to spawn entity " + entitySpawner.entity.toString() + " at pos: " + pos);
        }
    }

    public static boolean trySpawn(Level world, BlockPos pos, EntitySpawnerBlockEntity entitySpawner) {
        Entity spawned;
        if (world == null)
            return false;

        Optional<EntityType<?>> optionalEntityType = Registry.ENTITY_TYPE.getOptional(entitySpawner.entity);
        if (!optionalEntityType.isPresent())
            return false;

        EntityType<?> type = optionalEntityType.get();

        if (type == EntityType.MINECART || type == EntityType.CHEST_MINECART)
            return tryCreateMinecart(world, pos, type, entitySpawner);

        if (type == EntityType.ARMOR_STAND)
            return tryCreateArmorStand(world, pos, entitySpawner);

        for (int i = 0; i < entitySpawner.count; i++) {
            spawned = type.create(world);
            if (spawned == null)
                return false;

            spawned.moveTo(pos, 0.0F, 0.0F);

            if (spawned instanceof Mob) {
                Mob mob = (Mob) spawned;
                if (entitySpawner.persist) mob.setPersistenceRequired();

                // set the mob health if specified (values greater than zero)
                if (entitySpawner.health > 0) {
                    // need to override this attribute on the entity to allow health values greater than maxhealth
                    AttributeInstance healthAttribute = mob.getAttribute(Attributes.MAX_HEALTH);
                    if (healthAttribute != null)
                        healthAttribute.setBaseValue(entitySpawner.health);

                    mob.setHealth((float) entitySpawner.health);
                }

                // add armor to the mob
                if (!entitySpawner.armor.isEmpty()) {
                    Random random = world.random;
                    tryEquip(mob, entitySpawner.armor, random);
                }

                // apply status effects to the mob
                // TODO: make this a helper so that Strange can use it too
                final List<String> effectsList = new ArrayList<>();
                if (entitySpawner.effects.length() > 0) {
                    if (entitySpawner.effects.contains(",")) {
                        effectsList.addAll(Arrays.asList(entitySpawner.effects.split(",")));
                    } else {
                        effectsList.add(entitySpawner.effects);
                    }
                    if (effectsList.size() > 0) {
                        effectsList.forEach(effectName -> {
                            MobEffect effect = Registry.MOB_EFFECT.get(new ResourceLocation(effectName));
                            if (effect != null)
                                mob.addEffect(new MobEffectInstance(effect, 999999, 1));
                        });
                    }
                }

                mob.finalizeSpawn((ServerLevelAccessor)world, world.getCurrentDifficultyAt(pos), MobSpawnType.TRIGGERED, null, null);
            }

            world.addFreshEntity(spawned);
        }
        return true;
    }

    public static boolean tryCreateMinecart(Level world, BlockPos pos, EntityType<?> type, EntitySpawnerBlockEntity entitySpawner) {
        AbstractMinecart minecart = null;
        if (world == null) return false;

        if (type == EntityType.CHEST_MINECART) {
            minecart = new MinecartChest(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);

            String loot = DataBlockHelper.getValue("loot", entitySpawner.meta, "");
            ResourceLocation lootTable = LootHelper.getLootTable(loot, BuiltInLootTables.ABANDONED_MINESHAFT);
            ((MinecartChest)minecart).setLootTable(lootTable, world.random.nextLong());
        } else if (type == EntityType.MINECART) {
            minecart = new Minecart(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        }

        if (minecart == null)
            return false;

        world.addFreshEntity(minecart);

        return true;
    }

    public static boolean tryCreateArmorStand(Level world, BlockPos pos, EntitySpawnerBlockEntity entitySpawner) {
        if (world == null)
            return false;

        Random random = world.random;
        ArmorStand stand = EntityType.ARMOR_STAND.create(world);
        if (stand == null)
            return false;

        Direction face = DataBlockHelper.getFacing(DataBlockHelper.getValue("facing", entitySpawner.meta, "north"));
        Direction facing = entitySpawner.rotation.rotate(face);
        String type = DataBlockHelper.getValue("type", entitySpawner.meta, "");

        tryEquip(stand, type, random);

        float yaw = facing.get2DDataValue();
        stand.moveTo(pos, yaw, 0.0F);
        world.addFreshEntity(stand);

        return true;
    }

    private static void tryEquip(LivingEntity entity, String type, Random random) {
        List<Item> ironHeld = new ArrayList<>(Arrays.asList(
            Items.IRON_SWORD, Items.IRON_PICKAXE, Items.IRON_AXE
        ));

        List<Item> goldHeld = new ArrayList<>(Arrays.asList(
            Items.DIAMOND_SWORD, Items.DIAMOND_PICKAXE
        ));

        List<Item> diamondHeld = new ArrayList<>(Arrays.asList(
            Items.DIAMOND_SWORD, Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL
        ));

        if (type.equals("leather")) {
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ironHeld.get(random.nextInt(ironHeld.size()))));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
        }
        if (type.equals("chain")) {
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ironHeld.get(random.nextInt(ironHeld.size()))));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.CHAINMAIL_CHESTPLATE));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.CHAINMAIL_BOOTS));
        }
        if (type.equals("iron")) {
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ironHeld.get(random.nextInt(ironHeld.size()))));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        }
        if (type.equals("gold")) {
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(goldHeld.get(random.nextInt(goldHeld.size()))));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.GOLDEN_LEGGINGS));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.GOLDEN_BOOTS));
        }
        if (type.equals("diamond")) {
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(diamondHeld.get(random.nextInt(diamondHeld.size()))));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            if (random.nextFloat() < 0.25F)
                entity.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        }
    }
}
