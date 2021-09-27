package svenhjol.charm.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import svenhjol.charm.Charm;

public class ActionPerformedCriterion extends SimpleCriterionTrigger<ActionPerformedCriterion.Conditions> {
    private static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "action_performed");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Conditions createInstance(JsonObject obj, EntityPredicate.Composite extended, DeserializationContext deserializer) {
        ResourceLocation identifier = new ResourceLocation(GsonHelper.getAsString(obj, "action"));
        return new Conditions(extended, identifier);
    }

    public void trigger(ServerPlayer player, ResourceLocation action) {
        this.trigger(player, conditions -> conditions.matches(action));
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {
        private final ResourceLocation action;

        public Conditions(EntityPredicate.Composite extended, ResourceLocation action) {
            super(ActionPerformedCriterion.ID, extended);
            this.action = action;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            jsonObject.addProperty("action", this.action.toString());
            return jsonObject;
        }

        public boolean matches(ResourceLocation action) {
            return this.action.equals(action);
        }
    }
}
