package svenhjol.charm.enums;

public interface IMetalMaterial extends ICharmEnum {
    float getDestroyTime();

    float getResistance();

    boolean isFireResistant();

    boolean hasNuggets();
}
