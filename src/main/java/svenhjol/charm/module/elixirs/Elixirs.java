package svenhjol.charm.module.elixirs;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.PlayerTickCallback;
import svenhjol.charm.helper.ClassHelper;
import svenhjol.charm.helper.ItemNbtHelper;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.CommonRegistry;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Discoverable potions with much greater strength and duration.")
public class Elixirs extends CharmModule {
    public static final String ITEM_NAMESPACE = "svenhjol.charm.module.elixirs.item";
    public static final String ELIXIR_TAG = "charm_elixir";
    public static final List<IElixir> POTIONS = new ArrayList<>();
    public static final Map<ResourceLocation, Float> LOOT_TABLES = new HashMap<>();
    public static final ResourceLocation LOOT_ID = new ResourceLocation(Charm.MOD_ID, "elixirs_loot");
    public static LootItemFunctionType LOOT_FUNCTION;
    private static final ResourceLocation TRIGGER_FIND_ELIXIR = new ResourceLocation(Charm.MOD_ID, "find_elixir");
    private static Advancement CACHED_ELIXIR_ADVANCEMENT = null;

    @Config(name = "Blacklist", description = "List of elixirs that will not be loaded. See wiki for details.")
    public static List<String> blacklist = new ArrayList<>();

    @Config(name = "Dungeon chest chance", description = "Chance (out of 1.0) of an elixir being found in a vanilla dungeon chest.")
    public static float dungeonChance = 0.05F;

    @Config(name = "Woodland mansion chest chance", description = "Chance (out of 1.0) of an elixir being found in a woodland mansion chest.")
    public static float mansionChance = 1.0F;

    @Config(name = "Stronghold chest chance", description = "Chance (out of 1.0) of an elixir being found in stronghold corridor and crossing chests.")
    public static float strongholdChance = 0.25F;

    @Config(name = "Ancient city chest chance", description = "Chance (out of 1.0) of an elixir being found in an ancient city chest.")
    public static float ancientCityChance = 0.25F;

    @Config(name = "Minimum elixirs per chest", description = "Minimum number of elixirs that will be in the chest.")
    public static int minRolls = 1;

    @Config(name = "Maximum elixirs per chest", description = "Maximum number of elixirs that will be in the chest.")
    public static int maxRolls = 3;

    @Override
    public void register() {
        LOOT_FUNCTION = CommonRegistry.lootFunctionType(LOOT_ID, new LootItemFunctionType(new ElixirsLootFunction.Serializer()));
    }

    @Override
    public void runWhenEnabled() {
        LootTableEvents.MODIFY.register(this::handleLootTables);
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);

        registerLootTable(BuiltInLootTables.SIMPLE_DUNGEON, dungeonChance);
        registerLootTable(BuiltInLootTables.WOODLAND_MANSION, mansionChance);
        registerLootTable(BuiltInLootTables.ANCIENT_CITY, ancientCityChance);
        registerLootTable(BuiltInLootTables.STRONGHOLD_CORRIDOR, strongholdChance);
        registerLootTable(BuiltInLootTables.STRONGHOLD_CROSSING, strongholdChance);

        try {
            List<String> classes = ClassHelper.getClassesInPackage(ITEM_NAMESPACE);
            for (String className : classes) {
                var simpleClassName = className.substring(className.lastIndexOf(".") + 1);
                try {
                    Class<?> clazz = Class.forName(className);
                    if (blacklist.contains(simpleClassName)) continue;

                    IElixir potion = (IElixir)clazz.getDeclaredConstructor().newInstance();
                    POTIONS.add(potion);

                    LogHelper.debug(Charm.MOD_ID, getClass(), "Loaded potion: " + simpleClassName);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    LogHelper.warn(getClass(), "Elixir `" + simpleClassName + "` failed to load: " + e.getMessage());
                }
            }
        } catch (IOException | URISyntaxException e) {
            LogHelper.info(Charm.MOD_ID, getClass(), "Failed to load classes from namespace: " + e.getMessage());
        }
    }

    public static void triggerFindElixir(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_FIND_ELIXIR);
    }

    private void handlePlayerTick(Player player) {
        if (player.level.isClientSide || player.level.getGameTime() % 80 == 0) return;
        var serverPlayer = (ServerPlayer) player;

        if (CACHED_ELIXIR_ADVANCEMENT == null) {
            var advancements = serverPlayer.getLevel().getServer().getAdvancements();
            var id = new ResourceLocation(Charm.MOD_ID, "elixirs/" + TRIGGER_FIND_ELIXIR.getPath());
            CACHED_ELIXIR_ADVANCEMENT = advancements.getAdvancement(id);
        }

        if (CACHED_ELIXIR_ADVANCEMENT != null) {
            var progress = serverPlayer.getAdvancements().getOrStartProgress(CACHED_ELIXIR_ADVANCEMENT);

            if (!progress.isDone()) {
                var inventory = player.getInventory();
                var size = inventory.getContainerSize();
                for (int i = 0; i < size; i++) {
                    var inSlot = inventory.getItem(i);
                    if (inSlot.isEmpty()) continue;
                    if (ItemNbtHelper.tagExists(inSlot, ELIXIR_TAG)) {
                        triggerFindElixir(serverPlayer);
                        break;
                    }
                }
            }
        }
    }

    public static void registerLootTable(ResourceLocation id, float chance) {
        LOOT_TABLES.put(id, chance);
    }

    private void handleLootTables(ResourceManager resourceManager, LootTables lootTables, ResourceLocation id, LootTable.Builder builder, LootTableSource source) {
        if (LOOT_TABLES.containsKey(id)) {
            var chance = LOOT_TABLES.get(id);
            var lootPool = LootPool.lootPool()
                .when(LootItemRandomChanceCondition.randomChance(chance))
                .setRolls(UniformGenerator.between((float)minRolls, (float)maxRolls))
                .add(LootItem.lootTableItem(Items.GLASS_BOTTLE)
                    .setWeight(1)
                    .apply(() -> new ElixirsLootFunction(new LootItemCondition[0])));

            builder.withPool(lootPool);
        }
    }
}
