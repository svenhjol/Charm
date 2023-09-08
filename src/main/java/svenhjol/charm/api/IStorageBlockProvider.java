package svenhjol.charm.api;

import java.util.List;

public interface IStorageBlockProvider {
    List<Class<? extends IStorageBlockFeature>> getStorageBlockFeatures();

    List<Class<? extends IStorageBlockFeature>> getStorageBlockClientFeatures();
}
