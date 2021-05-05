package svenhjol.charm.screenhandler;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.mojang.serialization.Dynamic;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtOps;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import svenhjol.charm.init.CharmSounds;
import svenhjol.charm.base.helper.ItemNBTHelper;
import svenhjol.charm.module.Atlases;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class AtlasInventory implements NamedScreenHandlerFactory, Inventory {
    public static final String EMPTY_MAPS = "empty_maps";
    public static final String FILLED_MAPS = "filled_maps";
    public static final String ACTIVE_MAP = "active_map";
    public static final String SCALE = "scale";
    public static final String ID = "id";
    private static final int EMPTY_MAP_SLOTS = 3;
    private final Table<RegistryKey<World>, Index, MapInfo> mapInfos;
    private final DefaultedList<ItemStack> emptyMaps;
    private int diameter;
    private ItemStack atlas;
    private int scale;
    private boolean isOpen = false;

    public AtlasInventory(ItemStack atlas) {
        this.atlas = atlas;
        this.scale = Atlases.defaultScale;
        this.diameter = 128;
        this.emptyMaps = DefaultedList.ofSize(EMPTY_MAP_SLOTS, ItemStack.EMPTY);
        this.mapInfos = HashBasedTable.create();
        load();
    }

    public void reload(ItemStack atlas) {
        this.atlas = atlas;
        emptyMaps.clear();
        mapInfos.clear();
        load();
    }

    private void load() {
        scale = ItemNBTHelper.getInt(atlas, SCALE, Atlases.defaultScale);
        diameter = 128 * (1 << scale);
        Inventories.readNbt(ItemNBTHelper.getCompound(atlas, EMPTY_MAPS), emptyMaps);
        NbtList listNBT = ItemNBTHelper.getList(atlas, FILLED_MAPS);
        for (int i = 0; i < listNBT.size(); ++i) {
            putMapInfo(MapInfo.readFrom(listNBT.getCompound(i)));
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    private void putMapInfo(MapInfo mapInfo) {
        mapInfos.put(mapInfo.dimension, convertCoordsToIndex(mapInfo.x, mapInfo.z), mapInfo);
    }

    public Index getIndexOf(PlayerEntity player) {
        return convertCoordsToIndex((int) player.getX() + 64,(int) player.getZ() + 64);
    }

    public Index convertCoordsToIndex(int x, int y) {
        return Index.of(convertCoordToIndex(x), convertCoordToIndex(y));
    }

    public int convertCoordToIndex(int coordinate) {
        return Math.floorDiv(coordinate, diameter);
    }

    @Nullable
    private static MapInfo createMapInfo(World world, ItemStack map) {
        Integer mapId = FilledMapItem.getMapId(map);
        if (mapId == null) return null;

        MapState mapData = FilledMapItem.getMapState(mapId, world);
        return mapData != null ? new MapInfo(mapData.centerX, mapData.centerZ, FilledMapItem.getMapId(map), map, mapData.dimension) : null;
    }

    public boolean updateActiveMap(ServerPlayerEntity player) {
        MapInfo activeMap = mapInfos.get(player.world.getRegistryKey(), getIndexOf(player));
        boolean madeNewMap = false;
        if (activeMap == null && !isOpen) {
            activeMap = makeNewMap(player, (int) player.getX(), (int) player.getZ());
            madeNewMap = activeMap != null;
        }
        if (activeMap != null) {
            Atlases.sendMapToClient(player, activeMap.map, false);
            ItemNBTHelper.setInt(atlas, ACTIVE_MAP, activeMap.id);
        } else {
            ItemNBTHelper.setInt(atlas, ACTIVE_MAP, -1);
        }
        return madeNewMap;
    }

    private MapInfo makeNewMap(ServerPlayerEntity player, int x, int z) {
        for (int i = 0; i < EMPTY_MAP_SLOTS; ++i) {
            ItemStack stack = emptyMaps.get(i);
            if (stack.getItem() == Items.MAP) {
                if (!player.isCreative()) {
                    removeStack(i, 1);
                }
                ItemStack map = FilledMapItem.createMap(player.world, x, z, (byte) scale, true, true);
                MapInfo mapInfo = createMapInfo(player.world, map);
                putMapInfo(mapInfo);
                markDirty();
                player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.BLOCKS, 0.5f,
                    player.world.random.nextFloat() * 0.1F + 0.9F);
                return mapInfo;
            }
        }
        return null;
    }

    @Nullable
    public int getActiveMapId(World world) {
        return ItemNBTHelper.getInt(atlas, ACTIVE_MAP, -1);
    }

    @Nullable
    public MapState getActiveMap(World world) {
        int mapId = getActiveMapId(world);
        if (mapId == -1)
            return null;

        return world.getMapState(FilledMapItem.getMapName(mapId));
    }

    @Nullable
    public ItemStack getLastActiveMapItem() {
        int activeId = ItemNBTHelper.getInt(atlas, ACTIVE_MAP, -1);
        if (activeId == -1) return null;
        return mapInfos.values().stream().filter(it -> it.id == activeId).findAny().map(it -> it.map).orElse(null);
    }

    @Override
    @Nonnull
    public Text getDisplayName() {
        return atlas.getName();
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity player) {
        return new AtlasContainer(syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return EMPTY_MAP_SLOTS;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.emptyMaps) {
            if (!stack.isEmpty()) return false;
        }
        return mapInfos.isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getStack(int index) {
        return emptyMaps.get(index);
    }

    @Nonnull
    @Override
    public ItemStack removeStack(int index, int count) {
        ItemStack itemstack = Inventories.splitStack(emptyMaps, index, count);
        markDirty();
        return itemstack;
    }

    @Nonnull
    @Override
    public ItemStack removeStack(int index) {
        ItemStack itemStack = Inventories.removeStack(emptyMaps, index);
        markDirty();
        return itemStack;
    }

    public MapInfo removeMapByCoords(World world, int x, int z) {
        MapInfo info = mapInfos.remove(world.getRegistryKey(), convertCoordsToIndex(x, z));
        markDirty();
        return info;
    }

    public void addToInventory(World world, ItemStack itemStack) {
        if (itemStack.getItem() == Items.FILLED_MAP) {
            putMapInfo(createMapInfo(world, itemStack));
            markDirty();
        }
    }

    @Override
    public void setStack(int index, @Nonnull ItemStack stack) {
        if (!emptyMaps.get(index).equals(stack)) { // WARNING: doesn't match Forge
            emptyMaps.set(index, stack);
            if (stack.getCount() > getMaxCountPerStack()) {
                stack.setCount(getMaxCountPerStack());
            }
            markDirty();
        }
    }

    @Override
    public void markDirty() {
        ItemNBTHelper.setInt(atlas, SCALE, scale);
        NbtCompound emptyMapNBT = new NbtCompound();
        Inventories.writeNbt(emptyMapNBT, emptyMaps, false);
        ItemNBTHelper.setCompound(atlas, EMPTY_MAPS, emptyMapNBT);
        NbtList listNBT = new NbtList();
        for (MapInfo mapInfo : mapInfos.values()) {
            NbtCompound nbt = new NbtCompound();
            mapInfo.writeTo(nbt);
            listNBT.add(nbt);
        }
        ItemNBTHelper.setList(atlas, FILLED_MAPS, listNBT);
    }

    @Override
    public boolean canPlayerUse(@Nonnull PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack heldItem = player.getStackInHand(hand);
            if (heldItem.getItem() == Atlases.ATLAS_ITEM && Objects.equals(ItemNBTHelper.getUuid(atlas, ID), ItemNBTHelper.getUuid(heldItem, ID))) return true;
        }
        return false;
    }

    @Override
    public void clear() {
        emptyMaps.clear();
        mapInfos.clear();
    }

    @Override
    public void onOpen(PlayerEntity player) {
        isOpen = true;
        if(!player.world.isClient) {
            player.playSound(CharmSounds.BOOKSHELF_OPEN, SoundCategory.BLOCKS, 0.5f, player.world.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public void onClose(PlayerEntity player) {
        isOpen = false;
        if(!player.world.isClient) {
            player.playSound(CharmSounds.BOOKSHELF_CLOSE, SoundCategory.BLOCKS, 0.5f, player.world.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    public boolean hasItemStack(ItemStack stack) {
        return Stream.concat(emptyMaps.stream(), mapInfos.values().stream().map(it -> it.map)).anyMatch(it -> !it.isEmpty() && it.isItemEqual(stack));
    }

    public ItemStack getAtlasItem() {
        return atlas;
    }

    public Table<RegistryKey<World>, Index, MapInfo> getMapInfos() {
        return mapInfos;
    }

    public Map<Index, MapInfo> getCurrentDimensionMapInfos(World world) {
        return mapInfos.row(world.getRegistryKey());
    }

    public int getScale() {
        return scale;
    }

    public static class MapInfo {
        private static final String X = "x";
        private static final String Z = "z";
        private static final String ID = "id";
        private static final String MAP = "map";
        private static final String DIMENSION = "dimension";
        public final int x;
        public final int z;
        public final int id;
        public final ItemStack map;
        public final RegistryKey<World> dimension;

        private MapInfo(int x, int z, int id, ItemStack map, RegistryKey<World> dimension) {
            this.x = x;
            this.z = z;
            this.id = id;
            this.map = map;
            this.dimension = dimension;
        }

        public static MapInfo readFrom(NbtCompound nbt) {
            return new MapInfo(nbt.getInt(X), nbt.getInt(Z), nbt.getInt(ID), ItemStack.fromNbt(nbt.getCompound(MAP)),
                DimensionType.worldFromDimensionNbt(new Dynamic<>(NbtOps.INSTANCE, nbt.get(DIMENSION))).result().orElse(World.OVERWORLD));
        }

        public void writeTo(NbtCompound nbt) {
            nbt.putInt(X, x);
            nbt.putInt(Z, z);
            nbt.putInt(ID, id);
            NbtCompound mapNBT = new NbtCompound();
            map.writeNbt(mapNBT);
            nbt.put(MAP, mapNBT);
            Identifier.CODEC.encodeStart(NbtOps.INSTANCE, dimension.getValue()).result().ifPresent(it -> nbt.put(DIMENSION, it));
        }
    }

    public static class Index {
        private static final Int2ObjectMap<Int2ObjectMap<Index>> cache = new Int2ObjectOpenHashMap<>();
        public final int x;
        public final int y;

        private Index(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Index plus(Index value) {
            return transform(it -> it.apply(this) + it.apply(value));
        }

        public Index plus(int value) {
            return transform(it -> it.apply(this) + value);
        }

        public Index minus(Index value) {
            return transform(it -> it.apply(this) - it.apply(value));
        }

        public Index minus(int value) {
            return transform(it -> it.apply(this) - value);
        }

        public Index multiply(int value) {
            return transform(it -> it.apply(this) * value);
        }

        public Index divide(int value) {
            return transform(it -> it.apply(this) / value);
        }

        public Index clamp(Index min, Index max) {
            return transform(it -> MathHelper.clamp(it.apply(this), it.apply(min), it.apply(max)));
        }

        private Index transform(Function<Function<Index, Integer>, Integer> transformer) {
            return Index.of(transformer.apply(it -> it.x), transformer.apply(it -> it.y));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Index index = (Index) o;
            return x == index.x && y == index.y;
        }

        @Override
        public int hashCode() {
            return 7079 * x + y;
        }

        public static Index of(int x, int y) {
            Int2ObjectMap<Index> columnCache = cache.get(x);
            if (columnCache == null) {
                columnCache = new Int2ObjectOpenHashMap<>();
                cache.put(x, columnCache);
            }
            Index index = columnCache.get(y);
            if (index == null) {
                index = new Index(x, y);
                columnCache.put(y, index);
            }
            return index;
        }
    }
}
