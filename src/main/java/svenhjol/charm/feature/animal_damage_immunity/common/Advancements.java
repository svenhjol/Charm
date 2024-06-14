package svenhjol.charm.feature.animal_damage_immunity.common;

import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.feature.animal_damage_immunity.AnimalDamageImmunity;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<AnimalDamageImmunity> {
    public Advancements(AnimalDamageImmunity feature) {
        super(feature);
    }
    
    public void negatedAnimalDamage(ServerPlayer player) {
        trigger("negated_animal_damage", player);
    }
}
