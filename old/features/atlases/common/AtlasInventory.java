package svenhjol.charm.feature.atlases.common;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import svenhjol.charm.feature.atlases.Atlases;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class AtlasInventory implements MenuProvider, Container {
    private static final Map<UUID, AtlasInventory> SERVER_CACHE = new HashMap<>();
    private static final Map<UUID, AtlasInventory> CLIENT_CACHE = new HashMap<>();
    private final Table<ResourceKey<Level>, Index, MapInfo> mapInfos;
    private List<ItemStack> emptyMaps;
    private int diameter;
    private ItemStack atlas;
    private int scale;
    private boolean isOpen = false;
    private @Nullable MapId lastActiveMap;

    public AtlasInventory(ItemStack atlas) {
        this.atlas = atlas;
        this.scale = Atlases.defaultScale;
        this.diameter = 128;
        this.emptyMaps = NonNullList.withSize(Atlases.EMPTY_MAP_SLOTS, ItemStack.EMPTY);
        this.mapInfos = HashBasedTable.create();
        this.lastActiveMap = null;
        load();
    }

    public static AtlasInventory find(Inventory inventory) {
        for (var hand : InteractionHand.values()) {
            var held = inventory.player.getItemInHand(hand);
            if (held.getItem() == Atlases.registers.item.get()) {
                return get(inventory.player.level(), held);
            }
        }
        throw new IllegalStateException("No atlas in any hand, can't open!");
    }

    public static AtlasInventory get(Level level, ItemStack stack) {
        UUID id;

        if (AtlasData.has(stack)) {
            id = AtlasData.get(stack).getId();
        } else {
            id = UUID.randomUUID();
            AtlasData.create(id).save(stack);
        }

        var cache = level.isClientSide ? CLIENT_CACHE : SERVER_CACHE;
        var inventory = cache.get(id);

        if (inventory == null) {
            inventory = new AtlasInventory(stack);
            cache.put(id, inventory);
        }

        if (inventory.getAtlasItem() != stack) {
            inventory.reload(stack);
        }

        return inventory;
    }

    public void reload(ItemStack atlas) {
        this.atlas = atlas;
        emptyMaps.clear();
        mapInfos.clear();
        load();
    }

    private void load() {
        var data = AtlasData.get(atlas);
        scale = data.getScale();
        diameter = 128 * (1 << scale);
        emptyMaps = data.getEmptyMaps();

        for (var filledMap : data.getFilledMaps()) {
            putMapInfo(MapInfo.readFrom(filledMap));
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    private void putMapInfo(MapInfo mapInfo) {
        mapInfos.put(mapInfo.dimension, convertCoordsToIndex(mapInfo.x, mapInfo.z), mapInfo);
    }

    public Index getIndexOf(Player player) {
        return convertCoordsToIndex((int) player.getX() + 64,(int) player.getZ() + 64);
    }

    public Index convertCoordsToIndex(int x, int y) {
        return Index.of(convertCoordToIndex(x), convertCoordToIndex(y));
    }

    public int convertCoordToIndex(int coordinate) {
        return Math.floorDiv(coordinate, diameter);
    }

    @Nullable
    private static MapInfo createMapInfo(Level level, ItemStack map) {
        var mapId = map.get(DataComponents.MAP_ID);
        if (mapId == null) {
            return null;
        }

        var mapData = MapItem.getSavedData(mapId, level);
        if (mapData != null) {
            return new MapInfo(mapData.centerX, mapData.centerZ, mapId, map, mapData.dimension);
        }

        return null;
    }

    public boolean updateActiveMap(ServerPlayer player) {
        var activeMap = mapInfos.get(player.level().dimension(), getIndexOf(player));
        var madeNewMap = false;

        if (activeMap == null && !isOpen) {
            activeMap = makeNewMap(player, (int) player.getX(), (int) player.getZ());
            madeNewMap = activeMap != null;
        }


        if (madeNewMap || (activeMap != null && activeMap.mapId != lastActiveMap)) {
            AtlasData.getMutable(atlas)
                .setActiveMap(activeMap.mapId)
                .save(atlas);

            lastActiveMap = activeMap.mapId;
        }

        if (activeMap != null) {
            Atlases.networking.sendMapToClient(player, activeMap.map, false);
        }

        return madeNewMap;
    }

    private MapInfo makeNewMap(ServerPlayer player, int x, int z) {
        for (int i = 0; i < Atlases.EMPTY_MAP_SLOTS; ++i) {
            ItemStack stack = emptyMaps.get(i);
            if (stack.getItem() == Items.MAP) {
                if (!player.isCreative()) {
                    removeItem(i, 1);
                }
                ItemStack map = MapItem.create(player.level(), x, z, (byte) scale, true, true);
                MapInfo mapInfo = createMapInfo(player.level(), map);
                putMapInfo(mapInfo);
                setChanged();

                player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundSource.BLOCKS, 0.5f,
                    player.level().random.nextFloat() * 0.1F + 0.9F);

                return mapInfo;
            }
        }
        return null;
    }

    @Nullable
    public MapId getActiveMapId(Level level) {
        if (AtlasData.has(atlas)) {
            return AtlasData.get(atlas).getActiveMap();
        }
        return null;
    }

    @Nullable
    public MapItemSavedData getActiveMap(Level level) {
        var mapId = getActiveMapId(level);
        if (mapId == null) return null;

        return level.getMapData(mapId);
    }

    @Override
    @Nonnull
    public Component getDisplayName() {
        return atlas.getHoverName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, @Nonnull Inventory playerInventory, @Nonnull Player player) {
        return new Menu(syncId, playerInventory, this);
    }

    @Override
    public int getContainerSize() {
        return Atlases.EMPTY_MAP_SLOTS;
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
    public ItemStack getItem(int index) {
        return emptyMaps.get(index);
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = ContainerHelper.removeItem(emptyMaps, index, count);
        setChanged();
        return itemstack;
    }

    @Nonnull
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack itemStack = ContainerHelper.takeItem(emptyMaps, index);
        setChanged();
        return itemStack;
    }

    public MapInfo removeMapByCoords(Level level, int x, int z) {
        MapInfo info = mapInfos.remove(level.dimension(), convertCoordsToIndex(x, z));
        setChanged();
        return info;
    }

    public void addToInventory(Level level, ItemStack itemStack) {
        if (itemStack.getItem() == Items.FILLED_MAP) {
            putMapInfo(createMapInfo(level, itemStack));
            setChanged();
        }
    }

    @Override
    public void setItem(int index, @Nonnull ItemStack stack) {
        if (!emptyMaps.get(index).equals(stack)) { // WARNING: doesn't match Forge
            emptyMaps.set(index, stack);
            if (stack.getCount() > getMaxStackSize()) {
                stack.setCount(getMaxStackSize());
            }
            setChanged();
        }
    }

    @Override
    public void setChanged() {
        List<MapData> list = new ArrayList<>();
        for (MapInfo mapInfo : mapInfos.values()) {
            list.add(MapData.fromMapInfo(mapInfo));
        }

        AtlasData.getMutable(atlas)
            .setScale(scale)
            .setEmptyMaps(emptyMaps)
            .setFilledMaps(list)
            .save(atlas);
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        var data = AtlasData.get(atlas);
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (heldItem.getItem() == Atlases.registers.item.get()) {
                return Objects.equals(data.getId(), AtlasData.get(heldItem).getId());
            }
        }

        return false;
    }

    @Override
    public void clearContent() {
        emptyMaps.clear();
        mapInfos.clear();
    }

    @Override
    public void startOpen(Player player) {
        isOpen = true;
        if (!player.level().isClientSide) {
            player.playNotifySound(Atlases.registers.openSound.get(), SoundSource.BLOCKS, 0.4f, player.level().random.nextFloat() * 0.1F + 0.9F);
        }
    }

    @Override
    public void stopOpen(Player player) {
        isOpen = false;
        if (!player.level().isClientSide) {
            player.playNotifySound(Atlases.registers.closeSound.get(), SoundSource.BLOCKS, 0.4f, player.level().random.nextFloat() * 0.1F + 0.9F);
        }
    }

    public boolean matches(Predicate<ItemStack> predicate) {
        return mapInfos.values().stream().anyMatch(i -> predicate.test(i.map));
    }

    public boolean hasItemStack(ItemStack stack) {
        return Stream.concat(emptyMaps.stream(), mapInfos.values().stream().map(it -> it.map)).anyMatch(
            it -> !it.isEmpty() && ItemStack.isSameItem(it, stack));
    }

    public ItemStack getAtlasItem() {
        return atlas;
    }

    public Table<ResourceKey<Level>, Index, MapInfo> getMapInfos() {
        return mapInfos;
    }

    public Map<Index, MapInfo> getCurrentDimensionMapInfos(Level level) {
        return mapInfos.row(level.dimension());
    }

    public int getScale() {
        return scale;
    }

    public static class MapInfo {
        public final int x;
        public final int z;
        public final MapId mapId;
        public final ItemStack map;
        public final ResourceKey<Level> dimension;

        private MapInfo(int x, int z, MapId mapId, ItemStack map, ResourceKey<Level> dimension) {
            this.x = x;
            this.z = z;
            this.mapId = mapId;
            this.map = map;
            this.dimension = dimension;
        }

        public static MapInfo readFrom(MapData data) {
            return new MapInfo(
                data.x(),
                data.z(),
                data.mapId(),
                data.map(),
                data.dimension()
            );
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
            return transform(it -> Mth.clamp(it.apply(this), it.apply(min), it.apply(max)));
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
