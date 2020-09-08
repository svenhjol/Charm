package svenhjol.charm.module;

import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import svenhjol.charm.block.VariantBookshelfBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.IVariantMaterial;
import svenhjol.meson.enums.VanillaVariantMaterial;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.helper.OverrideHandler;
import svenhjol.meson.iface.Module;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Module(description = "Bookshelves available in all types of vanilla wood.")
public class VariantBookshelves extends MesonModule {
    public static final Map<IVariantMaterial, VariantBookshelfBlock> BOOKSHELF_BLOCKS = new HashMap<>();

    @Override
    public void register() {
        VanillaVariantMaterial.getTypesWithout(VanillaVariantMaterial.OAK).forEach(type -> {
            BOOKSHELF_BLOCKS.put(type, new VariantBookshelfBlock(this, type));
        });
    }

    @Override
    public void initWhenEnabled() {
        OverrideHandler.changeBlockTranslationKey(Blocks.BOOKSHELF, "block.charm.oak_bookshelf");
        EnchantmentsHelper.ENCHANTING_BLOCKS.addAll(BOOKSHELF_BLOCKS.values());
    }

    @Override
    public List<Identifier> getRecipesToRemove() {
        return Arrays.asList(new Identifier("minecraft", "bookshelf"));
    }
}
