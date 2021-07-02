package svenhjol.charm.integration.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.FieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.network.chat.TextComponent;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.loader.CharmCommonModule;

import java.lang.reflect.Field;
import java.util.*;

public class CharmModMenuPlugin implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TextComponent("Charm configuration"));

            // get all the loader modules
            List<CharmCommonModule> modules = new LinkedList<>(Charm.LOADER.getModules());

            builder.setSavingRunnable(() -> {
                // Serialise the config into the config file. This will be called last after all variables are updated.
                Charm.LOG.info("here");
                ConfigHelper.writeConfig(Charm.MOD_ID, modules);
            });

            ConfigCategory mainCategory = builder.getOrCreateCategory(new TextComponent("Charm"));

            for (CharmCommonModule module : modules) {
                if (!module.isAlwaysEnabled()) {
                    ConfigEntryBuilder enabledBuilder = builder.entryBuilder();

                    Map<Field, Object> properties = getModuleConfigProperties(module);
                    SubCategoryBuilder subcategory = enabledBuilder.startSubCategory(new TextComponent(module.getName()));

                    subcategory.add(enabledBuilder.startBooleanToggle(new TextComponent("Module Enabled"), module.isEnabledInConfig())
                        .setDefaultValue(module.isEnabledByDefault()) // Used when user click "Reset"
                        .setTooltip(new TextComponent("Enable or disable this module")) // Shown when the user hover over this option
                        .setSaveConsumer(module::setEnabledInConfig) // Called when user save the config
                        .requireRestart()
                        .build()); // Builds the option entry for cloth config

                    properties.forEach((prop, value) -> {
                        ConfigEntryBuilder propBuilder = builder.entryBuilder();
                        FieldBuilder<?, ?> fieldBuilder = null;
                        Config annotation = prop.getDeclaredAnnotation(Config.class);

                        TextComponent name = new TextComponent(annotation.name());
                        TextComponent description = new TextComponent(annotation.description());

                        if (value instanceof Boolean) {
                            fieldBuilder = propBuilder
                                .startBooleanToggle(name, (Boolean) value).setTooltip(description).setSaveConsumer(val -> trySetProp(prop, val));
                        } else if (value instanceof Integer) {
                            fieldBuilder = propBuilder
                                .startIntField(name, (Integer) value).setTooltip(description).setSaveConsumer(val -> trySetProp(prop, val));
                        } else if (value instanceof Double) {
                            fieldBuilder = propBuilder
                                .startDoubleField(name, (Double) value).setTooltip(description).setSaveConsumer(val -> trySetProp(prop, val));
                        } else if (value instanceof Float) {
                            fieldBuilder = propBuilder
                                .startFloatField(name, (Float) value).setTooltip(description).setSaveConsumer(val -> trySetProp(prop, val));
                        } else if (value instanceof String) {
                            fieldBuilder = propBuilder
                                .startTextField(name, (String)value).setTooltip(description).setSaveConsumer(val -> trySetProp(prop, val));
                        } else if (value instanceof List) {
                            fieldBuilder = propBuilder
                                .startStrList(name, (List<String>)value).setTooltip(description).setSaveConsumer(val -> trySetProp(prop, val));
                        }

                        if (fieldBuilder != null) {
                            fieldBuilder.requireRestart(annotation.requireRestart());
                            subcategory.add(fieldBuilder.build());
                        }
                    });

                    mainCategory.addEntry(subcategory.build());
                }
            }

            return builder.build();
        };
    }

    private Map<Field, Object> getModuleConfigProperties(CharmCommonModule module) {
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
                Charm.LOG.error("Failed to read config property " + prop.getName() + " in " + module.getName());
            }
        });

        return properties;
    }

    private void trySetProp(Field prop, Object val) {
        try { prop.set(null, val); } catch (IllegalAccessException e) { e.printStackTrace(); }
    }
}
