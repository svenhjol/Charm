package svenhjol.charm.feature.atlases.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.foundation.Resolve;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public final class AtlasData {
    private static final Supplier<Atlases> ATLASES = Resolve.defer(Atlases.class);

    public static final Codec<AtlasData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.OPTIONAL_CODEC.listOf().fieldOf("empty_maps")
            .forGetter(a -> a.emptyMaps),
        MapData.CODEC.listOf().fieldOf("filled_maps")
            .forGetter(a -> a.filledMaps),
        Codec.STRING.fieldOf("id")
            .forGetter(a -> a.id),
        Codec.INT.fieldOf("scale")
            .forGetter(a -> a.scale),
        Codec.INT.optionalFieldOf("active_map", -1)
            .forGetter(a -> a.activeMap)
    ).apply(instance, AtlasData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AtlasData> STREAM_CODEC = StreamCodec.composite(
        ItemStack.OPTIONAL_STREAM_CODEC.apply(ByteBufCodecs.list()),
            a -> a.emptyMaps,
        MapData.STREAM_CODEC.apply(ByteBufCodecs.list()),
            a -> a.filledMaps,
        ByteBufCodecs.STRING_UTF8,
            a -> a.id,
        ByteBufCodecs.INT,
            a -> a.scale,
        ByteBufCodecs.INT,
            a -> a.activeMap,
        AtlasData::new
    );

    private final NonNullList<ItemStack> emptyMaps;
    private final List<MapData> filledMaps;
    private final int activeMap;
    private final String id;
    private final int scale;

    public AtlasData(List<ItemStack> emptyMaps, List<MapData> filledMaps, String id, int scale, int active) {
        this.activeMap = active;
        this.id = id;
        this.scale = scale;
        this.emptyMaps = NonNullList.withSize(Atlases.EMPTY_MAP_SLOTS, ItemStack.EMPTY);
        for (int i = 0; i < emptyMaps.size(); i++) {
            this.emptyMaps.set(i, emptyMaps.get(i));
        }
        this.filledMaps = filledMaps;
    }

    public static Mutable create(UUID id) {
        List<ItemStack> emptyMaps = new ArrayList<>();
        for (int i = 0; i < Atlases.EMPTY_MAP_SLOTS; i++) {
            emptyMaps.add(i, ItemStack.EMPTY);
        }
        return new Mutable(new AtlasData(emptyMaps, List.of(), id.toString(), 0, -1));
    }

    public static boolean has(ItemStack stack) {
        return stack.has(ATLASES.get().registers.atlasData.get());
    }

    public static AtlasData get(ItemStack stack) {
        return stack.get(ATLASES.get().registers.atlasData.get());
    }

    public static Mutable getMutable(ItemStack stack) {
        return new Mutable(get(stack));
    }

    public static ItemStack set(ItemStack stack, Mutable data) {
        var immutable = data.toImmutable();
        stack.set(ATLASES.get().registers.atlasData.get(), immutable);
        return stack;
    }

    public List<ItemStack> getEmptyMaps() {
        return emptyMaps;
    }

    public List<MapData> getFilledMaps() {
        return filledMaps;
    }

    public UUID getId() {
        return UUID.fromString(id);
    }

    public @Nullable MapId getActiveMap() {
        return activeMap > -1 ? new MapId(activeMap) : null;
    }

    public int getScale() {
        return scale;
    }

    public static class Mutable {
        private List<ItemStack> emptyMaps;
        private List<MapData> filledMaps;
        private int activeMap;
        private String id;
        private int scale;

        public Mutable(AtlasData data) {
            this.emptyMaps = new ArrayList<>(data.emptyMaps);
            this.filledMaps = new ArrayList<>(data.filledMaps);
            this.activeMap = data.activeMap;
            this.id = data.id;
            this.scale = data.scale;
        }

        public AtlasData toImmutable() {
            return new AtlasData(
                emptyMaps,
                filledMaps,
                id, scale, activeMap
            );
        }

        public void save(ItemStack stack) {
            AtlasData.set(stack, this);
        }

        public Mutable setId(UUID id) {
            this.id = id.toString();
            return this;
        }

        public Mutable setActiveMap(@Nullable MapId activeMap) {
            this.activeMap = activeMap == null ? -1 : activeMap.id();
            return this;
        }

        public Mutable setEmptyMaps(List<ItemStack> emptyMaps) {
            if (emptyMaps.isEmpty() || emptyMaps.size() > Atlases.EMPTY_MAP_SLOTS) {
                throw new RuntimeException("Invalid emptyMaps size");
            }
            this.emptyMaps = emptyMaps;
            return this;
        }

        public Mutable setFilledMaps(List<MapData> filledMaps) {
            this.filledMaps = filledMaps;
            return this;
        }

        public Mutable setScale(int scale) {
            this.scale = scale;
            return this;
        }
    }
}
