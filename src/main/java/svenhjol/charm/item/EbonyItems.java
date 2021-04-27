package svenhjol.charm.item;

import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmSignItem;
import svenhjol.charm.module.EbonyWood;

public class EbonyItems {
    public static class EbonySignItem extends CharmSignItem {
        public EbonySignItem(CharmModule module) {
            super(module, "ebony_sign", EbonyWood.SIGN_BLOCK, EbonyWood.WALL_SIGN_BLOCK);
        }
    }
}
