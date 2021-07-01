package svenhjol.charm.module.variant_bookshelves;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.loader.CharmCommonModule;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Bookshelves available in all types of vanilla wood.")
public class VariantBookshelves extends CharmCommonModule {
    public static final Map<IVariantMaterial, VariantBookshelfBlock> BOOKSHELF_BLOCKS = new HashMap<>();

    @Override
    public void register() {
        VanillaVariantMaterial.getTypes().forEach(type -> {
            registerBookshelf(this, type);
        });
    }

    public static VariantBookshelfBlock registerBookshelf(CharmCommonModule module, IVariantMaterial material) {
        VariantBookshelfBlock bookshelf = new VariantBookshelfBlock(module, material);
        BOOKSHELF_BLOCKS.put(material, bookshelf);
        return bookshelf;
    }
}
