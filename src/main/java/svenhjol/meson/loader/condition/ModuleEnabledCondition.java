package svenhjol.meson.loader.condition;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import svenhjol.meson.MesonLoader;

public class ModuleEnabledCondition implements ICondition
{
    private MesonLoader instance;
    private final String moduleName;

    public ModuleEnabledCondition(MesonLoader instance, String moduleName)
    {
        this.instance = instance;
        this.moduleName = moduleName;
    }

    @Override
    public ResourceLocation getID()
    {
        return new ResourceLocation(instance.id, "module_enabled");
    }

    @Override
    public boolean test()
    {
        return instance.enabledModules.containsKey(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, moduleName));
    }

    public static class Serializer implements IConditionSerializer<ModuleEnabledCondition>
    {
        private MesonLoader instance;

        public Serializer(MesonLoader instance)
        {
            this.instance = instance;
        }

        @Override
        public void write(JsonObject json, ModuleEnabledCondition value)
        {
            json.addProperty("module", value.moduleName);
        }

        @Override
        public ModuleEnabledCondition read(JsonObject json)
        {
            return new ModuleEnabledCondition(instance, JSONUtils.getString(json, "module"));
        }

        @Override
        public ResourceLocation getID()
        {
            return new ResourceLocation(instance.id, "module_enabled");
        }
    }
}
