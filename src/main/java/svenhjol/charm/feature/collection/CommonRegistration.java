package svenhjol.charm.feature.collection;

import svenhjol.charm.foundation.feature.Register;

public final class CommonRegistration extends Register<Collection> {
    public CommonRegistration(Collection feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        Collection.enchantment = feature.registry()
            .enchantment("collection", CollectionEnchantment::new);
    }
}
