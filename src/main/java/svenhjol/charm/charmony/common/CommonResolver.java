package svenhjol.charm.charmony.common;

import svenhjol.charm.charmony.Resolve;

public interface CommonResolver<CF extends CommonFeature> {
    /**
     * A resolved reference to a common feature class.
     * @return Common feature
     */
    default CF common() {
        return Resolve.feature(typeForCommon());
    }

    /**
     * The commeon feature class type.
     * @return Class type to provide IDE completion.
     */
    Class<CF> typeForCommon();
}
