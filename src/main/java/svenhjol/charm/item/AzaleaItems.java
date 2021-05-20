package svenhjol.charm.item;

import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmBoatItem;
import svenhjol.charm.base.item.CharmSignItem;
import svenhjol.charm.entity.CharmBoatEntity;
import svenhjol.charm.module.AzaleaWood;

public class AzaleaItems {
    public static class AzaleaBoatItem extends CharmBoatItem {
        public AzaleaBoatItem(CharmModule module) {
            super(module, "azalea_boat", CharmBoatEntity.BoatType.AZALEA);
        }
    }

    public static class AzaleaSignItem extends CharmSignItem {
        public AzaleaSignItem(CharmModule module) {
            super(module, "azalea_sign", AzaleaWood.SIGN_BLOCK, AzaleaWood.WALL_SIGN_BLOCK);
        }
    }
}
