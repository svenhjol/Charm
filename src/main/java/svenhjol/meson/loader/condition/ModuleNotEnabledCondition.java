package svenhjol.meson.loader.condition;

import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import svenhjol.meson.Meson;

public class ModuleNotEnabledCondition implements ICondition {
    private final String moduleName;

    public ModuleNotEnabledCondition(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(Meson.MOD_ID.toLowerCase(), "module_not_enabled");
    }

    @Override
    public boolean test() {
        return !Meson.isModuleEnabled(new ResourceLocation(moduleName));
    }

    public static class Serializer implements IConditionSerializer<ModuleNotEnabledCondition> {
        public Serializer() {
        }

        @Override
        public void write(JsonObject json, ModuleNotEnabledCondition value) {
            json.addProperty("module", value.moduleName);
        }

        @Override
        public ModuleNotEnabledCondition read(JsonObject json) {
            return new ModuleNotEnabledCondition(json.getAsJsonPrimitive("module").getAsString());
        }

        @Override
        public ResourceLocation getID() {
            return new ResourceLocation(Meson.MOD_ID.toLowerCase(), "module_not_enabled");
        }
    }
}
