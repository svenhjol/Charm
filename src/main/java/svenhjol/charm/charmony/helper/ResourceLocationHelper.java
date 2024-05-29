package svenhjol.charm.charmony.helper;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.charmony.Charmony;
import svenhjol.charm.charmony.Log;

import java.util.List;

public final class ResourceLocationHelper {
    private static final Log LOGGER = new Log(Charmony.ID, "ResourceLocationHelper");

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
