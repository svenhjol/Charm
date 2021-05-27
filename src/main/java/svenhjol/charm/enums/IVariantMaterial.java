package svenhjol.charm.enums;

public interface IVariantMaterial extends ICharmEnum {
    default boolean isFlammable() {
        return true;
    }
}
