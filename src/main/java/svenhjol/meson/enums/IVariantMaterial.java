package svenhjol.meson.enums;

public interface IVariantMaterial extends IMesonEnum {
    default boolean isFlammable() {
        return true;
    }
}
