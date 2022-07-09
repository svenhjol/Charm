package svenhjol.charm.module.ebony_wood;

import svenhjol.charm.item.CharmSignItem;
import svenhjol.charm.loader.CharmModule;

public class EbonyItems {
    public static class EbonySignItem extends CharmSignItem {
        public EbonySignItem(CharmModule module) {
            super(module, "ebony_sign", EbonyWood.SIGN_BLOCK, EbonyWood.WALL_SIGN_BLOCK);
        }
    }
}
