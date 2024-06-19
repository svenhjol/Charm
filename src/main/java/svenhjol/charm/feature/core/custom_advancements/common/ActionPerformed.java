package svenhjol.charm.feature.core.custom_advancements.common;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public final class ActionPerformed extends SimpleCriterionTrigger<ActionPerformed.TriggerInstance> {
    public static ResourceLocation ID = new ResourceLocation("charmony_action_performed");

    public void trigger(ResourceLocation action, ServerPlayer player) {
        this.trigger(player, conditions -> conditions.matches(action));
    }

    @Override
    protected TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate predicate, DeserializationContext deserializer) {
        ResourceLocation action = new ResourceLocation(GsonHelper.getAsString(jsonObject, "action"));
        return new TriggerInstance(action, predicate);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {
        private final ResourceLocation action;

        public TriggerInstance(ResourceLocation action, ContextAwarePredicate predicate) {
            super(ID, predicate);
            this.action = action;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonObject = super.serializeToJson(context);
            jsonObject.addProperty("action", this.action.toString());
            return jsonObject;
        }

        public boolean matches(ResourceLocation action) {
            return this.action.equals(action);
        }
    }
}
