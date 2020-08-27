package svenhjol.meson.enums;

public interface IStorageMaterial extends IMesonEnum {
    default boolean isFlammable() {
        return true;
    }
}
