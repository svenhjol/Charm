package svenhjol.charm.feature.raid_horns;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@SuppressWarnings("deprecation")
@ClientFeature
public class RaidHornsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(RaidHorns.class));
    }

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();
        registry.itemProperties("minecraft:tooting",
            RaidHorns.item, () -> this::handleTooting);
    }

    @Override
    public void runWhenEnabled() {
        var registry = CharmClient.instance().registry();
        registry.itemTab(
            RaidHorns.item,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.TNT_MINECART
        );
    }

    private float handleTooting(ItemStack stack, ClientLevel level, LivingEntity entity, int i) {
        return entity != null
            && entity.isUsingItem()
            && entity.getUseItem() == stack ? 1.0F : 0.0F;
    }
}
