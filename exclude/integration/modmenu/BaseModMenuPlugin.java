package svenhjol.charm.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.network.chat.Component;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.helper.StringHelper;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.loader.CharmModule;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @version 1.0.2-charm
 */
@SuppressWarnings("unchecked")
public abstract class BaseModMenuPlugin<T extends CharmModule> implements ModMenuApi {
    public abstract String getModId();

    public abstract List<T> getModules();

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(TextHelper.translatable("cloth." + getModId() + ".title"));

            // get all the loader modules
            List<T> modules = new LinkedList<>(getModules());

            builder.setSavingRunnable(() -> {
                // Serialise the config into the config file. This will be called last after all variables are updated.
                ConfigHelper.writeConfig(getModId(), modules);
            });

            ConfigCategory mainCategory = builder.getOrCreateCategory(TextHelper.translatable("cloth." + getModId() + ".category.title"));

            for (T module : modules) {
                Map<Field, Object> properties = getModuleConfigProperties(module);

                SubCategoryBuilder subcategory = builder.entryBuilder()
                    .startSubCategory(TextHelper.literal(module.getName()));

                if (!module.isAlwaysEnabled()) {
                    FieldBuilder<?, ?> enabledValue = builder.entryBuilder()
                        .startBooleanToggle(TextHelper.translatable("cloth." + getModId() + ".category.module_enabled"), module.isEnabledInConfig())
                        .setDefaultValue(module.isEnabledByDefault()) // Used when user click "Reset"
                        .setTooltip(TextHelper.translatable(StringHelper.splitOverLines(module.getDescription()))) // Shown when the user hover over this option
                        .setSaveConsumer(module::setEnabledInConfig)
                        .requireRestart();

                    if (enabledValue != null) {
                        enabledValue.requireRestart(true);
                        subcategory.add(enabledValue.build());
                    }
                }

                properties.forEach((prop, value) -> {
                    ConfigEntryBuilder propBuilder = builder.entryBuilder();

                    FieldBuilder<?, ?> propValue = null;
                    Config annotation = prop.getDeclaredAnnotation(Config.class);

                    Component name = TextHelper.literal(annotation.name());
                    Component desc = TextHelper.literal(StringHelper.splitOverLines(annotation.description()));

                    if (value instanceof Boolean) {
                        propValue = propBuilder
                            .startBooleanToggle(name, (Boolean)value).setDefaultValue(() -> (Boolean)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof Integer) {
                        propValue = propBuilder
                            .startIntField(name, (Integer)value).setDefaultValue(() -> (Integer)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof Double) {
                        propValue = propBuilder
                            .startDoubleField(name, (Double)value).setDefaultValue(() -> (Double)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof Float) {
                        propValue = propBuilder
                            .startFloatField(name, (Float)value).setDefaultValue(() -> (Float)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof String) {
                        propValue = propBuilder
                            .startTextField(name, (String)value).setDefaultValue(() -> (String)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    } else if (value instanceof List) {
                        propValue = propBuilder
                            .startStrList(name, (List<String>)value).setDefaultValue(() -> (List<String>)tryGetDefault(prop)).setTooltip(desc).setSaveConsumer(val -> trySetProp(prop, val));
                    }

                    if (propValue != null) {
                        propValue.requireRestart(annotation.requireRestart());
                        subcategory.add(propValue.build());
                    }
                });

                if (!subcategory.isEmpty())
                    mainCategory.addEntry(subcategory.build());
            }

            return builder.build();
        };
    }

    private Map<Field, Object> getModuleConfigProperties(T module) {
        Map<Field, Object> properties = new LinkedHashMap<>();

        // get and set module config options
        ArrayList<Field> classFields = new ArrayList<>(Arrays.asList(module.getClass().getDeclaredFields()));
        classFields.forEach(prop -> {
            try {
                Config annotation = prop.getDeclaredAnnotation(Config.class);
                if (annotation == null) return;

                Object value = prop.get(null);
                properties.put(prop, value);

            } catch (Exception e) {
                LogHelper.error(this.getClass(), "Failed to read config property " + prop.getName() + " in " + module.getName());
            }
        });

        return properties;
    }

    private void trySetProp(Field prop, Object val) {
        try { prop.set(null, val); } catch (IllegalAccessException e) { e.printStackTrace(); }
    }

    private Object tryGetDefault(Field prop) {
        return ConfigHelper.getDefaultPropValues().getOrDefault(prop, null);
    }
}
