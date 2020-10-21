package svenhjol.charm.base.enums;

public interface IVariantMaterial extends ICharmEnum {
    default boolean isFlammable() {
        return true;
    }
}
