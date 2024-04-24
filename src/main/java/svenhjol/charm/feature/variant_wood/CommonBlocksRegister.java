package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.foundation.Register;

public class CommonBlocksRegister extends Register<VariantWood> {
    public CommonBlocksRegister(VariantWood feature) {
        super(feature);
    }

    @Override
    public void onRegister() {
        CustomChest.blockEntity = feature.registry().blockEntity("variant_chest", () -> VariantChestBlockEntity::new);
        CustomTrappedChest.blockEntity = feature.registry().blockEntity("variant_trapped_chest", () -> VariantTrappedChestBlockEntity::new);
    }
}
