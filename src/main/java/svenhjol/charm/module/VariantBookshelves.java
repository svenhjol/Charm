package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.helper.OverrideHandler;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.VariantBookshelfBlock;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, description = "Bookshelves available in all types of vanilla wood.")
public class VariantBookshelves extends CharmModule {
    public static final Map<IVariantMaterial, VariantBookshelfBlock> BOOKSHELF_BLOCKS = new HashMap<>();

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

    public static VariantBookshelfBlock registerBookshelf(CharmModule module, IVariantMaterial material) {
        VariantBookshelfBlock bookshelf = new VariantBookshelfBlock(module, material);
        BOOKSHELF_BLOCKS.put(material, bookshelf);
        return bookshelf;
    }
}
