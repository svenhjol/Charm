package svenhjol.charm.module.variant_bookshelves;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.IWoodMaterial;
import svenhjol.charm.enums.VanillaWoodMaterial;
import svenhjol.charm.loader.CharmModule;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Bookshelves available in all types of vanilla wood.")
public class VariantBookshelves extends CharmModule {
    public static final Map<IWoodMaterial, VariantBookshelfBlock> BOOKSHELF_BLOCKS = new HashMap<>();

    @Override
    public void register() {
        VanillaWoodMaterial.getTypes().forEach(type -> {
            registerBookshelf(this, type);
        });
    }

    public static VariantBookshelfBlock registerBookshelf(CharmModule module, IWoodMaterial material) {
        VariantBookshelfBlock bookshelf = new VariantBookshelfBlock(module, material);
        BOOKSHELF_BLOCKS.put(material, bookshelf);
        return bookshelf;
    }
}
