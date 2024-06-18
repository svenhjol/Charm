package svenhjol.charm.feature.collection.common;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.collection.Collection;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<Collection> {
    public final Supplier<Attribute> attribute;
    public final ResourceKey<Enchantment> enchantment;

    public Registers(Collection feature) {
        super(feature);
        var registry = feature().registry();

        enchantment = registry.enchantment("collection");
        attribute = registry.attribute("player.automatic_item_pickup", () -> new RangedAttribute(
            "attribute.name.player.charm.automatic_item_pickup", 0.0, 0.0, 1.0).setSyncable(true));

        // Must attach the collection attribute to the player for it to function.
        registry.entityAttribute(() -> EntityType.PLAYER, attribute);
    }
}
