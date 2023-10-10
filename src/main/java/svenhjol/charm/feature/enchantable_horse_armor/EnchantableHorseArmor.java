package svenhjol.charm.feature.enchantable_horse_armor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.feature.advancements.Advancements;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony.helper.PlayerHelper;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.iface.IHorseArmorEnchantmentProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Horse armor can be enchanted.")
public class EnchantableHorseArmor extends CharmonyFeature implements IHorseArmorEnchantmentProvider {
    static final List<Enchantment> ENCHANTMENTS = new ArrayList<>();
    
    @Configurable(name = "Enchantments", description = "Enchantments that will function on horse armor.")
    public static List<String> configEnchantments = Arrays.asList(
        "minecraft:protection",
        "minecraft:fire_protection",
        "minecraft:blast_protection",
        "minecraft:projectile_protection",
        "minecraft:thorns",
        "minecraft:frost_walker",
        "minecraft:feather_falling",
        "minecraft:respiration",
        "minecraft:soul_speed"
    );

    @Override
    public void register() {
        ApiHelper.consume(IHorseArmorEnchantmentProvider.class,
            provider -> provider.getEnchantments().forEach(this::addEnchantment));

        CharmonyApi.registerProvider(this);
    }

    private void addEnchantment(Enchantment enchantment) {
        if (!ENCHANTMENTS.contains(enchantment)) {
            Charm.instance().log().debug(getClass(), "Adding enchantment " + enchantment);
            ENCHANTMENTS.add(enchantment);
        }
    }

    /**
     * Resolves config enchantments into registered enchantments.
     * @return List of resolved enchantments.
     */
    @Override
    public List<Enchantment> getEnchantments() {
        List<Enchantment> validatedEnchantments = new ArrayList<>();

        // Validate configured enchantments and cache them.
        for (var enchantment : configEnchantments) {
            BuiltInRegistries.ENCHANTMENT.getOptional(new ResourceLocation(enchantment))
                .ifPresent(validatedEnchantments::add);
        }

        return validatedEnchantments;
    }

    /**
     * Called by mixin.
     * Check if the itemstack can be enchanted with the given enchantment.
     * The itemstack should always be checked to see if it's horse armor.
     */
    public static boolean canEnchant(ItemStack stack, Enchantment enchantment) {
        return stack.getItem() instanceof HorseArmorItem && getAllEnchantments().contains(enchantment);
    }

    /**
     * Used by the mixin to fetch the total set of possible horse armor enchantments.
     * @return Complete list of horse armor enchantments.
     */
    public static List<Enchantment> getAllEnchantments() {
        return ENCHANTMENTS;
    }

    public static void triggerAddEnchantmentToHorseArmor(ServerLevel level, BlockPos pos) {
        var players = PlayerHelper.getPlayersInRange(level, pos, 4.0d);
        players.forEach(
            player -> Advancements.trigger(Charm.instance().makeId("equipped_enchanted_horse_armor"), (ServerPlayer) player));
    }
}