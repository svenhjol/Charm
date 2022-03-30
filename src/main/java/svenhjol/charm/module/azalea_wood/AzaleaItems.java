package svenhjol.charm.module.azalea_wood;

import svenhjol.charm.item.CharmSignItem;
import svenhjol.charm.loader.CharmModule;

public class AzaleaItems {
    public static class AzaleaSignItem extends CharmSignItem {
        public AzaleaSignItem(CharmModule module) {
            super(module, "azalea_sign", AzaleaWood.SIGN_BLOCK, AzaleaWood.WALL_SIGN_BLOCK);
        }
    }
}
