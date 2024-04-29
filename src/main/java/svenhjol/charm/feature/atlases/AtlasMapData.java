package svenhjol.charm.feature.atlases;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapId;

public class AtlasMapData {
    public static final Codec<AtlasMapData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        MapId.CODEC.fieldOf("id")
            .forGetter(a -> a.id),
        Codec.INT.fieldOf("x")
            .forGetter(a -> a.x),
        Codec.INT.fieldOf("z")
            .forGetter(a -> a.z),
    ).apply(instance, AtlasMapData::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, AtlasMapData> STREAM_CODEC = StreamCodec.composite(
        MapId.STREAM_CODEC,
            a -> a.id,
        ByteBufCodecs.INT,
            a -> a.x,
        ByteBufCodecs.INT,
            a -> a.z,
        AtlasMapData::new
    );

    private final MapId id;
    private final int x;
    private final int z;
    private final ResourceKey<Level> dimension;

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public MapId getId() {
        var x = MapItem.getSavedData(id, new Level());
        x.
    }

    public AtlasMapData(MapId id, int x, int z, ResourceLocation dimension) {
        this.id = id;
        this.x = x;
        this.z = z;
    }
}
