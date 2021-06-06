package svenhjol.charm.enums;

import svenhjol.charm.enums.ICharmEnum;

public interface IVariantMaterial extends ICharmEnum {
    default boolean isFlammable() {
        return true;
    }
}
