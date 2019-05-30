package svenhjol.charm.world.feature;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.charm.world.item.ItemTotemOfShielding;
import svenhjol.meson.Feature;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.helper.LootHelper;

public class TotemOfShielding extends Feature
{
    public static Item totem;
    public static double damageMultiplier; // %age damage transferred to the totem
    public static int maxHealth; // how much damage the totem can take
    public static boolean addToLoot; // add to loot tables
    public static boolean addToIllusioner;

    @Override
    public String getDescription()
    {
        return "With the Totem of Shielding in your offhand (shield) slot, any damage you take is absorbed by the totem instead.";
    }

    @Override
    public void setupConfig()
    {
        damageMultiplier = propDouble(
            "Damage multiplier",
            "The incoming player damage is multiplied by this amount before being transferred to the totem.",
            0.75D
        );
        maxHealth = propInt(
            "Maximum Health",
            "Maximum durability of the totem.",
            100
        );
        addToLoot = propBoolean(
            "Add to loot",
            "Add the totem to mansions, pyramids and jungle temple loot.",
            true
        );
        addToIllusioner = propBoolean(
            "Add as Illusioner drop",
            "If Illusioners are enabled, add the totem as a possible drop.",
            true
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        totem = new ItemTotemOfShielding();
        ItemHelper.availableTotems.add(new ItemStack(totem));
    }

    @SubscribeEvent
    public void onDamage(LivingDamageEvent event)
    {
        if (!event.isCanceled() && event.getEntityLiving() instanceof EntityPlayer) {

            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            ItemStack stack = player.getHeldItemOffhand();

            if (stack.getItem() instanceof ItemTotemOfShielding) {
                double totemDamage = event.getAmount() * damageMultiplier;
                boolean totemDead = stack.attemptDamageItem((int) Math.ceil(totemDamage), player.world.rand, (EntityPlayerMP) player);
                if (totemDead) {
                    stack.shrink(1);
                    player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ITEM_TOTEM_USE, SoundCategory.BLOCKS, 0.8f, 1f);
                } else {
                    player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1f, 1.25f);
                }

                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event)
    {
        if (!addToLoot) return;
        int weight = 0;
        int quality = 0;
        LootFunction[] functions = new LootFunction[0];
        LootCondition[] conditions = new LootCondition[0];

        if (event.getName().equals(LootTableList.CHESTS_WOODLAND_MANSION)) { weight = 12; }
        if (event.getName().equals(LootTableList.CHESTS_DESERT_PYRAMID)) { weight = 10; }
        if (event.getName().equals(LootTableList.CHESTS_JUNGLE_TEMPLE)) { weight = 10; }
        if (event.getName().equals(CharmLootTables.VILLAGE_PRIEST)) { weight = 2; }
        if (event.getName().equals(CharmLootTables.TREASURE_VALUABLE)) { weight = 12; }
        if (event.getName().equals(CharmLootTables.TREASURE_RARE)) { weight = 16; }

        if (weight > 0) {
            LootHelper.addToLootTable(event.getTable(), totem, weight, quality, functions, conditions);
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}