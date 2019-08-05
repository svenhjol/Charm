package svenhjol.charm.tweaks.feature;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.charm.base.CharmLootTables;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.EnchantmentHelper;
import svenhjol.meson.helper.LootHelper;
import svenhjol.meson.helper.SoundHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnearthItems extends Feature
{
    public static List<Block> validBlocks = new ArrayList<>();
    public static double unearthChance;
    public static int damage;

    @Override
    public String getDescription()
    {
        return "Use a fortune-enchanted shovel on dirt, sand, clat or gravel for a chance to unearth dungeon treasure items.";
    }

    @Override
    public void configure()
    {
        super.configure();

        unearthChance = propDouble(
                "Unearthing item chance",
                "Chance (out of 1.0) of unearthing a treasure item when digging with an enchanted shovel/spade.",
                0.001D
        );

        damage = propInt(
                "Unearthing item damage",
                "Maximum durability cost to the shovel when a treasure item is unearthed.",
                16
        );

        String[] validBlockNames = propStringList(
                "Valid blocks",
                "Blocks that treasure may be unearthed from when digging with an enchanted shovel/spade.",
                new String[]{
                        "sand", "gravel", "clay", "dirt"
                }
        );

        for (String name : validBlockNames) {
            validBlocks.add(Block.getBlockFromName(name));
        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event)
    {
        // check if fortune shovel and valid block type
        ItemStack held = event.getPlayer().getHeldItemMainhand();
        Block checkBlock = event.getState().getBlock();

        if (held.getItem() instanceof ItemSpade
                && EnchantmentHelper.hasEnchantment(Enchantments.FORTUNE, held)
                && validBlocks.contains(checkBlock)
        ) {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            EntityPlayer player = event.getPlayer();
            int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, held);

            Random rand = new Random();
            rand.setSeed(pos.getX() + pos.getZ());

            if (player != null && world.rand.nextFloat() <= unearthChance + (level * 0.05)) {
                // generate loot
                ResourceLocation source = world.rand.nextFloat() <= 0.5f ? LootHelper.getRandomLootTable(LootHelper.RARITY.COMMON, LootHelper.TYPE.MISC) : CharmLootTables.TREASURE_JUNK;

                LootTable table = world.getLootTableManager().getLootTableFromLocation(source);
                LootContext.Builder builder = new LootContext.Builder((WorldServer)world);
                builder.withLuck(player.getLuck());
                LootContext context = builder.build();
                List<ItemStack> list = table.generateLootForPools(world.rand, context);

                if (list.size() > 0) {
                    ItemStack stack = list.get(world.rand.nextInt(list.size()));
                    EntityItem item = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack);

                    if (world.isRemote) {
                        SoundHelper.playerSound(player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.1f, SoundCategory.AMBIENT);
                    }

                    world.spawnEntity(item);
                    held.damageItem(world.rand.nextInt(damage), player);
                } else {
                    Meson.debug("Empty loot table list: ", source);
                }
            }
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return true;
    }
}
