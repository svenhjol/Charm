package svenhjol.charm.enchanting.module;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.enchanting.enchantment.CurseBreakEnchantment;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.ENCHANTING, hasSubscriptions = true,
    description = "Combine the Curse Break enchanted book with a cursed item on an anvil to remove the item's curse.")
public class CurseBreak extends MesonModule {
    public static CurseBreakEnchantment enchantment;

    @Config(name = "XP cost", description = "Number of levels required to remove a curse from an item.")
    public static int xpCost = 1;

    @Override
    public void init() {
        enchantment = new CurseBreakEnchantment(this);
    }

    @Override
    public void onCommonSetup(FMLCommonSetupEvent event) {
        if (!Meson.isModuleEnabled("charm:no_anvil_minimum_xp") && xpCost == 0)
            xpCost = 1;
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        if (!left.isEmpty()
            && !right.isEmpty()
            && right.getItem() == Items.ENCHANTED_BOOK
            && EnchantmentsHelper.hasEnchantment(enchantment, right)
        ) {
            if (EnchantmentHelper.getEnchantments(left).isEmpty())
                return;

            out = left.copy();
            ITextComponent displayName = out.getDisplayName();
            EnchantmentsHelper.removeRandomCurse(out);

            if (!displayName.getString().isEmpty()) {
                out.setDisplayName(displayName);
            }

            event.setCost(CurseBreak.xpCost);
            event.setOutput(out);
        }
    }
}
