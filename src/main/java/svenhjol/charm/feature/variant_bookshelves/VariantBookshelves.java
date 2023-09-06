package svenhjol.charm.feature.variant_bookshelves;

import svenhjol.charm.Charm;
import svenhjol.charm.api.IVariantBookshelfProvider;
import svenhjol.charm.feature.variant_bookshelves.VariantBookshelfBlock.BlockItem;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false, description = "Registers variant bookshelves.")
public class VariantBookshelves extends CharmFeature {
    public static final Map<IVariantMaterial, Supplier<VariantBookshelfBlock>> BOOKSHELF_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<BlockItem>> BOOKSHELF_BLOCK_ITEMS = new HashMap<>();

    @Override
    public void register() {
        ApiHelper.consume(IVariantBookshelfProvider.class,
            provider -> provider.getVariantBookshelves().forEach(this::registerBookshelf));
    }

    private void registerBookshelf(IVariantMaterial material) {
        var registry = Charm.instance().registry();
        var id = material.getSerializedName() + "_bookshelf";

        var block = registry.block(id, () -> new VariantBookshelfBlock(material));
        var blockItem = registry.item(id, () -> new BlockItem(block));

        BOOKSHELF_BLOCKS.put(material, block);
        BOOKSHELF_BLOCK_ITEMS.put(material, blockItem);

        registry.ignite(block);
        registry.fuel(blockItem);
    }
}
