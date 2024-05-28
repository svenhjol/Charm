package svenhjol.charm.feature.casks.common;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record CaskContentsData(
    int bottles,
    List<ResourceLocation> effects,
    Map<ResourceLocation, Integer> durations,
    Map<ResourceLocation, Integer> amplifiers,
    Map<ResourceLocation, Integer> dilutions
) {
    public static final Codec<CaskContentsData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("bottles")
            .forGetter(CaskContentsData::bottles),
        ResourceLocation.CODEC.listOf().fieldOf("effects")
            .forGetter(CaskContentsData::effects),
        Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("durations")
            .forGetter(CaskContentsData::durations),
        Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("amplifiers")
            .forGetter(CaskContentsData::amplifiers),
        Codec.unboundedMap(ResourceLocation.CODEC, Codec.INT).fieldOf("dilutions")
            .forGetter(CaskContentsData::dilutions)
    ).apply(instance, CaskContentsData::new));

    public static final StreamCodec<FriendlyByteBuf, CaskContentsData> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.INT, CaskContentsData::bottles,
        ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), CaskContentsData::effects,
        ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), CaskContentsData::durations,
        ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), CaskContentsData::amplifiers,
        ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, ByteBufCodecs.INT), CaskContentsData::dilutions,
        CaskContentsData::new
    );

    public static final CaskContentsData EMPTY = new CaskContentsData(
        0,
        new ArrayList<>(),
        new HashMap<>(),
        new HashMap<>(),
        new HashMap<>());
}
