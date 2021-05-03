package svenhjol.charm.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import svenhjol.charm.Charm;

public class ActionPerformedCriterion extends AbstractCriterion<ActionPerformedCriterion.Conditions> {
    private static final Identifier ID = new Identifier(Charm.MOD_ID, "action_performed");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer deserializer) {
        Identifier identifier = new Identifier(JsonHelper.getString(obj, "action"));
        return new ActionPerformedCriterion.Conditions(extended, identifier);
    }

    public void trigger(ServerPlayerEntity player, Identifier action) {
        this.test(player, conditions -> conditions.matches(action));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final Identifier action;

        public Conditions(EntityPredicate.Extended extended, Identifier action) {
            super(ActionPerformedCriterion.ID, extended);
            this.action = action;
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.addProperty("action", this.action.toString());
            return jsonObject;
        }

        public boolean matches(Identifier action) {
            return this.action.equals(action);
        }
    }
}
