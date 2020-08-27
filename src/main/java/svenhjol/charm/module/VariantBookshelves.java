package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import svenhjol.charm.block.VariantBookshelfBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IStorageMaterial;
import svenhjol.meson.enums.VanillaStorageMaterial;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.helper.OverrideHandler;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(description = "Bookshelves available in all types of vanilla wood.")
public class VariantBookshelves extends MesonModule {
    public static final Map<IStorageMaterial, VariantBookshelfBlock> BOOKSHELF_BLOCKS = new HashMap<>();

    @Override
    public void init() {
        VanillaStorageMaterial.getTypesWithout(VanillaStorageMaterial.OAK).forEach(type -> {
            BOOKSHELF_BLOCKS.put(type, new VariantBookshelfBlock(this, type));
        });

        if (enabled) {
            OverrideHandler.changeBlockTranslationKey(Blocks.BOOKSHELF, "block.charm.oak_bookshelf");
            EnchantmentsHelper.ENCHANTING_BLOCKS.addAll(BOOKSHELF_BLOCKS.values());
        }
    }
}
