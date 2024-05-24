package svenhjol.charm.api.iface;

import java.util.List;

@Deprecated
public interface ConditionalTagProvider {
    List<ConditionalTag> getTagConditions();
}
