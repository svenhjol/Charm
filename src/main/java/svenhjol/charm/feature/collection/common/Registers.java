package svenhjol.charm.feature.collection.common;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.feature.collection.Collection;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Collection> {
    public final Supplier<Holder<Attribute>> attribute;
    public final ResourceKey<Enchantment> enchantment;

    public Registers(Collection feature) {
        super(feature);
        var registry = feature().registry();

        enchantment = registry.enchantment("collection");
        attribute = registry.attribute("player.collection", () -> new RangedAttribute(
            "attribute.name.player.collection", 0.0, 0.0, 1.0).setSyncable(true));

        // Must attach the collection attribute to the player for it to function.
        registry.entityAttributes(() -> EntityType.PLAYER, () -> Player.createAttributes().add(attribute.get()));
    }
}
