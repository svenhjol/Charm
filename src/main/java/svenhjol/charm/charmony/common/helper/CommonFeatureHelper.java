package svenhjol.charm.charmony.common.helper;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.charmony.Charmony;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.enums.Side;
import svenhjol.charm.charmony.helper.TextHelper;

public final class CommonFeatureHelper {
    private static final Log LOGGER = new Log(Charmony.ID, "CommonFeatureHelper");

    /**
     * Check whether the resource is a valid+enabled Charm feature according to the following criteria:
     * - NOT a minecraft resource
     * - a Charm mod (match on namespace)
     * - an enabled feature (match on path)
     * @param id Resource to check.
     * @return True if resource is an enabled Charmony feature.
     */
    @SuppressWarnings("unused")
    public static boolean isDisabledCharmonyFeature(ResourceLocation id) {
        var namespace = id.getNamespace();
        var path = id.getPath();

        // If the resource is a minecraft resource or not a charm-based mod resource, allow it through.
        if (namespace.equals("minecraft") || !Resolve.hasLoader(Side.COMMON, namespace)) {
            return false;
        }

        var featureName = path.split("/")[0];
        var niceFeatureName = TextHelper.snakeToUpperCamel(featureName);

        // Remove for disabled charm features.
        if (!Resolve.isEnabled(Side.COMMON, new ResourceLocation(namespace, featureName))) {
            LOGGER.debug("Feature " + niceFeatureName + " not enabled for " + id);
            return true;
        }

        return false;
    }
}
