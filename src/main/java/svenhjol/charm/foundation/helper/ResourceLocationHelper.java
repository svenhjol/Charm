package svenhjol.charm.foundation.helper;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.enums.Side;

import java.util.List;

public final class ResourceLocationHelper {
    private static final Log LOGGER = new Log(Charm.ID, "ResourceLocationHelper");

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

    /**
     * Match the given resource against a list of exact matches and fuzzy matches.
     * If the resource matches anything return true.
     * @param res The resource to filter.
     * @param exactMatches List of exact match patterns.
     * @param fuzzyMatches List of fuzzy match patterns.
     * @return True if the resource is matched
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean match(ResourceLocation res, List<String> exactMatches, List<String> fuzzyMatches) {
        String path;
        var id = res.toString();

        if (id.contains(":")) {
            path = id.split(":")[1];
        } else {
            path = id;
        }

        /* --- Pattern checking --- */

        // Exact string match.
        if (exactMatches.contains(id)) {
            LOGGER.debug("Removed " + id + ": exact id match was found");
            return true;
        }

        // Partial match.
        for (var str : fuzzyMatches) {
            var pattern = TextHelper.createRegexFromGlob(str);
            if (id.matches(pattern) || ( !str.contains(":") && path.matches(pattern)) ) {
                LOGGER.debug("Removed " + id + ": fuzzy match was found (" + str + ")");
                return true;
            }
        }

        return false;
    }
}
