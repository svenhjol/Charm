package svenhjol.charm.feature.extractable_enchantments;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.helper.ConfigHelper;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

public class ExtractableEnchantments extends CommonFeature {
    @Configurable(name = "Initial cost", description = "Initial cost (in XP levels) of extraction before adding on the cost of the enchantment(s).")
    public static int initialCost = 5;

    @Configurable(name = "Treasure cost", description = "Adds extra cost (in XP levels) if the enchantment is a treasure enchantment such as Mending.")
    public static int treasureCost = 5;

    @Configurable(name = "Add item repair cost", description = "If true, the item's repair cost will be added to the cost of extraction.")
    public static boolean addRepairCost = true;

    @Override
    public String description() {
        return "Extract enchantments from any enchanted item onto an empty book using the grindstone.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new CommonRegistration(this));
    }

    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> !ConfigHelper.isModLoaded("grindenchantments"));
    }

    public static void triggerExtractedEnchantment(Player player) {
        Advancements.trigger(new ResourceLocation(Charm.ID, "extracted_enchantment"), player);
    }
}