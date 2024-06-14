package svenhjol.charm.feature.aerial_affinity.common;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.charmony.event.BlockBreakSpeedEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.aerial_affinity.AerialAffinity;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<AerialAffinity> {
    public final Supplier<Holder<Attribute>> attribute;
    public final ResourceKey<Enchantment> enchantment;

    public Registers(AerialAffinity feature) {
        super(feature);
        var registry = feature().registry();

        enchantment = registry.enchantment("aerial_affinity");
        attribute = registry.attribute("player.aerial_mining_speed", () -> new RangedAttribute(
            "attribute.name.player.charm.aerial_mining_speed", 0.2, 0.0, 20.0).setSyncable(true));

        // Must attach the aerial_mining_speed to the player for it to function.
        registry.entityAttribute(() -> EntityType.PLAYER, attribute);
    }

    @Override
    public void onEnabled() {
        BlockBreakSpeedEvent.INSTANCE.handle(feature().handlers::blockBreakSpeed);
    }
}
