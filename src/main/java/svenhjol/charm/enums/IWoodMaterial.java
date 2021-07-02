package svenhjol.charm.enums;

public interface IWoodMaterial extends ICharmEnum {
    default boolean isFlammable() {
        return true;
    }
}
