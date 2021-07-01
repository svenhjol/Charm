package svenhjol.charm.integration.modmenu;

import com.moandjiezana.toml.Toml;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.network.chat.TextComponent;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.loader.CharmCommonModule;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

public class CharmModMenuPlugin implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(new TextComponent("Charm configuration"));

            // load user config from disk
            Toml toml = ConfigHelper.getConfig(Charm.MOD_ID);

            // get all the loader modules
            List<CharmCommonModule> modules = Charm.LOADER.getModules();

            // prepare confi to write to disk
            List<CharmCommonModule> config = new LinkedList<>();

            builder.setSavingRunnable(() -> {
                // Serialise the config into the config file. This will be called last after all variables are updated.
                Charm.LOG.info("here");
                ConfigHelper.writeConfig(Charm.MOD_ID, config);
            });

            ConfigCategory mainCategory = builder.getOrCreateCategory(new TextComponent("Charm"));

            for (CharmCommonModule module : modules) {
                config.add(module);
                boolean enabled = ConfigHelper.isModuleEnabled(toml, module);

                if (!module.isAlwaysEnabled()) {
                    ConfigEntryBuilder enabledBuilder = builder.entryBuilder();

                    Map<Field, Object> properties = getModuleConfigProperties(module);
                    SubCategoryBuilder subcategory = enabledBuilder.startSubCategory(new TextComponent(module.getName()));

                    subcategory.add(enabledBuilder.startBooleanToggle(new TextComponent("Module Enabled"), enabled)
                        .setDefaultValue(module.isEnabledByDefault()) // Used when user click "Reset"
                        .setTooltip(new TextComponent("Enable or disable this module")) // Shown when the user hover over this option
                        .setSaveConsumer(module::setEnabled) // Called when user save the config
                        .build()); // Builds the option entry for cloth config

                    properties.forEach((prop, value) -> {
                        Consumer<?> trySetProp = val -> {
                            try { prop.set(null, val); } catch (IllegalAccessException e) { e.printStackTrace(); }
                        };

                        ConfigEntryBuilder propBuilder = builder.entryBuilder();
                        Config annotation = prop.getDeclaredAnnotation(Config.class);

                        TextComponent name = new TextComponent(annotation.name());
                        TextComponent description = new TextComponent(annotation.description());

                        if (value instanceof Boolean) {
                            subcategory.add(propBuilder
                                .startBooleanToggle(name, (Boolean)value)
                                .setTooltip(description)
                                .setSaveConsumer((Consumer<Boolean>) trySetProp)
                                .build());
                        } else if (value instanceof Integer) {
                            subcategory.add(propBuilder
                                .startIntField(name, (Integer) value)
                                .setTooltip(description)
                                .setSaveConsumer((Consumer<Integer>) trySetProp)
                                .build());
                        } else if (value instanceof Double) {
                            subcategory.add(propBuilder
                                .startDoubleField(name, (Double) value)
                                .setTooltip(description)
                                .setSaveConsumer((Consumer<Double>) trySetProp)
                                .build());
                        } else if (value instanceof Float) {
                            subcategory.add(propBuilder
                                .startFloatField(name, (Float) value)
                                .setTooltip(description)
                                .setSaveConsumer((Consumer<Float>) trySetProp)
                                .build());
                        } else if (value instanceof String) {
                            subcategory.add(propBuilder
                                .startTextField(name, (String)value)
                                .setTooltip(description)
                                .setSaveConsumer((Consumer<String>) trySetProp)
                                .build());
                        } else if (value instanceof List) {
                            subcategory.add(propBuilder
                                .startStrList(name, (List<String>)value)
                                .setTooltip(description)
                                .setSaveConsumer((Consumer<List<String>>) trySetProp)
                                .build());
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
}
