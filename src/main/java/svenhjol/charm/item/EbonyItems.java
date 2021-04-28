package svenhjol.charm.item;

import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmBoatItem;
import svenhjol.charm.base.item.CharmSignItem;
import svenhjol.charm.entity.CharmBoatEntity;
import svenhjol.charm.module.EbonyWood;

public class EbonyItems {
    public static class EbonyBoatItem extends CharmBoatItem {
        public EbonyBoatItem(CharmModule module) {
            super(module, "ebony_boat", CharmBoatEntity.BoatType.EBONY);
        }
    }

    public static class EbonySignItem extends CharmSignItem {
        public EbonySignItem(CharmModule module) {
            super(module, "ebony_sign", EbonyWood.SIGN_BLOCK, EbonyWood.WALL_SIGN_BLOCK);
        }
    }
}
