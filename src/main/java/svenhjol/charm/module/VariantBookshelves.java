package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import svenhjol.charm.Charm;
import svenhjol.charm.block.VariantBookshelfBlock;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.IVariantMaterial;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.helper.EnchantmentsHelper;
import svenhjol.charm.base.helper.OverrideHandler;
import svenhjol.charm.base.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, description = "Bookshelves available in all types of vanilla wood.")
public class VariantBookshelves extends CharmModule {
    public static final Map<IVariantMaterial, VariantBookshelfBlock> BOOKSHELF_BLOCKS = new HashMap<>();

    @Override
    public void register() {
        VanillaVariantMaterial.getTypesWithout(VanillaVariantMaterial.OAK).forEach(type -> {
            BOOKSHELF_BLOCKS.put(type, new VariantBookshelfBlock(this, type));
        });
    }

    @Override
    public void init() {
        OverrideHandler.changeBlockTranslationKey(Blocks.BOOKSHELF, "block.charm.oak_bookshelf");
        EnchantmentsHelper.ENCHANTING_BLOCKS.addAll(BOOKSHELF_BLOCKS.values());
    }
}
