package svenhjol.charm.feature.collection;

import svenhjol.charm.foundation.Registration;

public final class CommonRegistration extends Registration<Collection> {
    public CommonRegistration(Collection feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        Collection.enchantment = feature.registry()
            .enchantment("collection", CollectionEnchantment::new);
    }
}
