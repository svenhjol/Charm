package svenhjol.charm.feature.mob_textures.client;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum MobType implements StringRepresentable {
    CHICKEN,
    COW,
    DOLPHIN,
    PIG,
    SHEEP,
    SNOW_GOLEM,
    SQUID,
    TURTLE,
    WANDERING_TRADER;

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}