package svenhjol.meson.loader.condition;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonLoader;

public class ModuleEnabledCondition implements ICondition
{
    private final String moduleName;

    public ModuleEnabledCondition(String moduleName)
    {
        this.moduleName = moduleName;
    }

    @Override
    public ResourceLocation getID()
    {
        return new ResourceLocation(Meson.MOD_ID.toLowerCase(), "module_enabled");
    }

    @Override
    public boolean test()
    {
        String key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, moduleName).toLowerCase();
        return MesonLoader.hasModule(new ResourceLocation(key));
    }

    public static class Serializer implements IConditionSerializer<ModuleEnabledCondition>
    {
        public Serializer()
        {

        }

        @Override
        public void write(JsonObject json, ModuleEnabledCondition value)
        {
            json.addProperty("module", value.moduleName);
        }

        @Override
        public ModuleEnabledCondition read(JsonObject json)
        {
            return new ModuleEnabledCondition(JSONUtils.getString(json, "module"));
        }

        @Override
        public ResourceLocation getID()
        {
            return new ResourceLocation(Meson.MOD_ID.toLowerCase(), "module_enabled");
        }
    }
}
