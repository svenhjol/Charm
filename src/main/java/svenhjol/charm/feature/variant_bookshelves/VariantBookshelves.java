package svenhjol.charm.feature.variant_bookshelves;

import svenhjol.charm.Charm;
import svenhjol.charm.feature.variant_bookshelves.VariantBookshelfBlock.BlockItem;
import svenhjol.charmony.api.iface.IVariantMaterial;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.iface.ICommonRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, canBeDisabled = false, description = "Registers variant bookshelves.")
public class VariantBookshelves extends CharmFeature {
    public static final Map<IVariantMaterial, Supplier<VariantBookshelfBlock>> BOOKSHELF_BLOCKS = new HashMap<>();
    public static final Map<IVariantMaterial, Supplier<BlockItem>> BOOKSHELF_BLOCK_ITEMS = new HashMap<>();

    public static void registerBookshelf(ICommonRegistry registry, IVariantMaterial material) {
        var id = material.getSerializedName() + "_bookshelf";

        var block = registry.block(id, () -> new VariantBookshelfBlock(material));
        var blockItem = registry.item(id, () -> new BlockItem(block));

        BOOKSHELF_BLOCKS.put(material, block);
        BOOKSHELF_BLOCK_ITEMS.put(material, blockItem);

        registry.ignite(block);
        registry.fuel(blockItem);
    }
}
