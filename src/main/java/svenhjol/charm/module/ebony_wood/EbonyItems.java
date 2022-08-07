package svenhjol.charm.module.ebony_wood;

import svenhjol.charm.enums.CharmWoodMaterial;
import svenhjol.charm.item.CharmBoatItem;
import svenhjol.charm.item.CharmSignItem;
import svenhjol.charm.loader.CharmModule;

public class EbonyItems {
    public static class EbonyBoatItem extends CharmBoatItem {
        public EbonyBoatItem(CharmModule module) {
            super(module, "ebony_boat", CharmWoodMaterial.EBONY.getSerializedName());
        }
    }

    public static class EbonySignItem extends CharmSignItem {
        public EbonySignItem(CharmModule module) {
            super(module, "ebony_sign", EbonyWood.SIGN_BLOCK, EbonyWood.WALL_SIGN_BLOCK);
        }
    }
}
