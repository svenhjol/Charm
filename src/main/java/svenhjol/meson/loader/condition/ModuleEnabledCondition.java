package svenhjol.meson.loader.condition;

import com.google.gson.JsonObject;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonLoader;

public class ModuleEnabledCondition implements ICondition
{
    private static final ResourceLocation NAME = new ResourceLocation(Charm.MOD_ID, "module_enabled");
    private final String module;
    private final MesonLoader instance;

    public ModuleEnabledCondition(String module)
    {
        this.module = module;
        this.instance = MesonLoader.instances.get(module);
    }

    @Override
    public ResourceLocation getID()
    {
        return NAME;
    }

    @Override
    public boolean test()
    {
        return instance.enabledModules.containsKey(module);
    }

    public static class Serializer implements IConditionSerializer<ModuleEnabledCondition>
    {
        public static final ModuleEnabledCondition.Serializer INSTANCE = new ModuleEnabledCondition.Serializer();

        @Override
        public void write(JsonObject json, ModuleEnabledCondition value)
        {
            json.addProperty("module_enabled", value.module);
        }

        @Override
        public ModuleEnabledCondition read(JsonObject json)
        {
            return new ModuleEnabledCondition(JSONUtils.getString(json, "module_enabled"));
        }

        @Override
        public ResourceLocation getID()
        {
            return ModuleEnabledCondition.NAME;
        }
    }
}
