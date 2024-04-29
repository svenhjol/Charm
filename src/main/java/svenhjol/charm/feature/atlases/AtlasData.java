package svenhjol.charm.feature.atlases;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;

import java.util.List;
import java.util.UUID;

public class AtlasData {
    public static final Codec<AtlasData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.CODEC.listOf().fieldOf("empty_maps")
            .forGetter(a -> a.emptyMaps),
        ItemStack.CODEC.listOf().fieldOf("filled_maps")
            .forGetter(a -> a.filledMaps),
        MapId.CODEC.fieldOf("active_map")
            .forGetter(a -> a.activeMap),
        Codec.STRING.fieldOf("id")
            .forGetter(a -> a.id),
        Codec.INT.fieldOf("scale")
            .forGetter(a -> a.scale)
    ).apply(instance, AtlasData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AtlasData> STREAM_CODEC = StreamCodec.composite(
        ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            a -> a.emptyMaps,
        ItemStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
            a -> a.filledMaps,
        MapId.STREAM_CODEC,
            a -> a.activeMap,
        ByteBufCodecs.STRING_UTF8,
            a -> a.id,
        ByteBufCodecs.INT,
            a -> a.scale,
        AtlasData::new
    );

    private final NonNullList<ItemStack> emptyMaps;
    private final NonNullList<ItemStack> filledMaps;
    private final MapId activeMap;
    private final String id;
    private final int scale;

    public AtlasData(List<ItemStack> emptyMaps, List<ItemStack> filledMaps, MapId active, String id, int scale) {
        this.activeMap = active;
        this.id = id;
        this.scale = scale;

        this.emptyMaps = NonNullList.withSize(AtlasInventory.EMPTY_MAP_SLOTS, ItemStack.EMPTY);
        this.filledMaps = NonNullList.withSize(AtlasInventory.EMPTY_MAP_SLOTS, ItemStack.EMPTY);
        this.emptyMaps.addAll(emptyMaps);
        this.filledMaps.addAll(filledMaps);
    }

    public UUID getId() {
        return UUID.fromString(id);
    }

    public MapId getActiveMap() {
        return activeMap;
    }

    public static class Mutable {
        private final List<ItemStack> emptyMaps;
        private final List<ItemStack> filledMaps;
        private MapId activeMap;
        private final String id;
        private int scale;

        public AtlasData toImmutable() {
            return new AtlasData(
                emptyMaps,
                filledMaps,
                activeMap,
                id,
                scale);
        }

        public
    }
}
