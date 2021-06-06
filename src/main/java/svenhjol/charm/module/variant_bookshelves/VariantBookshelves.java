package svenhjol.charm.module.variant_bookshelves;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.enums.IVariantMaterial;
import svenhjol.charm.enums.VanillaVariantMaterial;
import svenhjol.charm.helper.OverrideHandler;
import svenhjol.charm.annotation.Module;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charm.module.variant_bookshelves.VariantBookshelfBlock;

@Module(mod = Charm.MOD_ID, description = "Bookshelves available in all types of vanilla wood.")
public class VariantBookshelves extends CharmModule {
    public static final Map<IVariantMaterial, svenhjol.charm.module.variant_bookshelves.VariantBookshelfBlock> BOOKSHELF_BLOCKS = new HashMap<>();

    @Override
    public void register() {
        VanillaVariantMaterial.getTypesWithout(VanillaVariantMaterial.OAK).forEach(type -> {
            registerBookshelf(this, type);
        });
    }

    @Override
    public void init() {
        OverrideHandler.changeBlockTranslationKey(Blocks.BOOKSHELF, "block.charm.oak_bookshelf");
    }

    public static svenhjol.charm.module.variant_bookshelves.VariantBookshelfBlock registerBookshelf(CharmModule module, IVariantMaterial material) {
        svenhjol.charm.module.variant_bookshelves.VariantBookshelfBlock bookshelf = new VariantBookshelfBlock(module, material);
        BOOKSHELF_BLOCKS.put(material, bookshelf);
        return bookshelf;
    }
}
