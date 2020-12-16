package svenhjol.charm.screenhandler;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.helper.ItemNBTHelper;
import svenhjol.charm.module.Atlas;
import svenhjol.charm.screenhandler.AtlasContainer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AtlasInventory implements NamedScreenHandlerFactory, Inventory {
    public static String CONTENTS = "contents";
    public static String ACTIVE_MAP = "active_map";
    public static int SIZE = 18;

    private final DefaultedList<ItemStack> items;
    private final World world;
    private final ItemStack itemStack;
    private final Text name;
    private List<MapInfo> mapInfos = new ArrayList<>();
    private MapInfo activeMap = null;
    private boolean isOpen = false;

    public AtlasInventory(World world, ItemStack itemStack, Text name) {
        this.world = world;
        this.itemStack = itemStack;
        this.name = name;
        this.items = getInventory(itemStack);
        updateMapInfos();
    }

    private static DefaultedList<ItemStack> getInventory(ItemStack itemStack) {
        CompoundTag nbt = ItemNBTHelper.getCompound(itemStack, CONTENTS);
        DefaultedList<ItemStack> items = DefaultedList.ofSize(SIZE, ItemStack.EMPTY);
        Inventories.fromTag(nbt, items);
        return items;
    }

    private static boolean isOnMap(MapInfo info, int x, int z) {
        return x >= info.x - info.scale && x < info.x + info.scale && z >= info.z - info.scale && z < info.z + info.scale;
    }

    private void updateMapInfos() {
        if (!world.isClient) {
            mapInfos = items.stream()
                    .filter(stack -> stack.getItem() == Items.FILLED_MAP)
                    .map(this::getMapInfo)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparingInt(info -> info.scale))
                    .collect(Collectors.toList());
            activeMap = null;
        }
    }

    private MapInfo getMapInfo(ItemStack itemStack) {
        MapState mapData = FilledMapItem.getMapState(itemStack, world);
        return mapData != null ? new MapInfo(mapData.xCenter, mapData.zCenter, 64 * (1 << mapData.scale), FilledMapItem.getMapId(itemStack),
                items.indexOf(itemStack)) : null;
    }

    @Nullable
    public MapInfo updateActiveMap(ServerPlayerEntity player) {
        int x = (int) Math.floor(player.getX());
        int z = (int) Math.floor(player.getZ());
        MapInfo activeMap = null;
        if (this.activeMap != null && isOnMap(this.activeMap, x, z)) {
            activeMap = this.activeMap;
        }
        if (activeMap == null) {
            activeMap = mapInfos.stream()
                    .filter(info -> isOnMap(info, x, z))
                    .findFirst().orElse(null);
        }
        if (activeMap == null) {
            activeMap = makeNewMap(player, x, z);
        }
        ItemNBTHelper.setInt(itemStack, ACTIVE_MAP, activeMap != null ? activeMap.id : -1);
        return activeMap;
    }

    private MapInfo makeNewMap(ServerPlayerEntity player, int x, int z) {
        if (!isOpen) {
            int from = -1;
            int to = -1;
            for (int i = 0; i < size() && (from == -1 || to == -1); ++i) {
                ItemStack stack = getStack(i);
                if (stack.isEmpty()) {
                    if (to == -1) {
                        to = i;
                    }
                } else if (stack.getItem() == Items.MAP) {
                    if (from == -1) {
                        from = i;
                    }
                    if (!player.isCreative() && to == -1 && stack.getCount() == 1) {
                        to = i;
                    }
                }
            }
            if (from != -1 && to != -1) {
                if (!player.isCreative()) {
                    removeStack(from, 1);
                }
                ItemStack map = FilledMapItem.createMap(world, x, z, (byte) Atlas.mapSize, true, true);
                setStack(to, map);
                world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT,
                        SoundCategory.BLOCKS, 0.5f, player.world.random.nextFloat() * 0.1F + 0.9F);
                return getMapInfo(map);
            }
        }
        return null;
    }

    @Nullable
    public MapState getActiveMap(ClientPlayerEntity player) {
        int activeId = ItemNBTHelper.getInt(itemStack, ACTIVE_MAP, -1);
        if (activeId == -1) return null;
        return player.world.getMapState(FilledMapItem.getMapName(activeId));
    }

    @Override
    public Text getDisplayName() {
        return name;
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new AtlasContainer(syncId, playerInventory, this);
    }

    @Override
    public int size() {
        return SIZE;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public ItemStack getStack(int index) {
        return items.get(index);
    }

    @Override
    public ItemStack removeStack(int index, int count) {
        ItemStack itemstack = Inventories.splitStack(items, index, count);
        markDirty();
        return itemstack;
    }

    @Override
    public ItemStack removeStack(int index) {
        ItemStack itemStack = Inventories.removeStack(items, index);
        markDirty();
        return itemStack;
    }

    @Override
    public void setStack(int index, ItemStack stack) {
        items.set(index, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }

        this.markDirty();
    }

    @Override
    public void markDirty() {
        updateMapInfos();
        CompoundTag nbt = new CompoundTag();
        Inventories.toTag(nbt, items, false);
        ItemNBTHelper.setCompound(itemStack, CONTENTS, nbt);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            if (player.getStackInHand(hand) == itemStack) return true;
        }
        return false;
    }

    @Override
    public void clear() {
        items.clear();
    }

    @Override
    public void onOpen(PlayerEntity player) {
        isOpen = true;
        world.playSound(null, player.getX(), player.getY(), player.getZ(), CharmSounds.BOOKSHELF_OPEN, SoundCategory.BLOCKS, 0.5f,
                player.world.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    public void onClose(PlayerEntity player) {
        isOpen = false;
        world.playSound(null, player.getX(), player.getY(), player.getZ(), CharmSounds.BOOKSHELF_CLOSE, SoundCategory.BLOCKS, 0.5f,
                player.world.random.nextFloat() * 0.1F + 0.9F);
    }

    public boolean hasItemStack(ItemStack stack) {
        return items.stream().anyMatch(it -> !it.isEmpty() && it.isItemEqual(stack));
    }

    public static class MapInfo {
        public final double x;
        public final double z;
        public final int scale;
        public final int id;
        public final int slot;

        private MapInfo(double x, double z, int scale, int id, int slot) {
            this.x = x;
            this.z = z;
            this.scale = scale;
            this.id = id;
            this.slot = slot;
        }
    }
}
