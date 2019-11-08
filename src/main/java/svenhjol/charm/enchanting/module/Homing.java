package svenhjol.charm.enchanting.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.enchanting.enchantment.HomingEnchantment;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.EnchantmentsHelper;
import svenhjol.meson.helper.PlayerHelper;
import svenhjol.meson.helper.WorldHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

@Module(mod = Charm.MOD_ID, category = CharmCategories.ENCHANTING, hasSubscriptions = true,
    description = "A tool with the Homing enchantment is attracted to ore/wood/stone of the same type that make up the head of the tool.\n" +
        "Right click underground and if you hear a sound, you can follow it to the source.")
public class Homing extends MesonModule
{
    public static HomingEnchantment enchantment;
    public static HashMap<IItemTier, List<Block>> matches = new HashMap<>();

    @Config(name = "Detection range", description = "Range (in blocks) that the enchanted tool will detect ore/wood/stone.")
    public static int range = 8;

    @Config(name = "Damage multiplier", description = "Percentage (where 1.0 is 100%) of damage given to the tool every time it detects a block.")
    public static double damageMultiplier = 0.015D;

    @Override
    public void init()
    {
        enchantment = new HomingEnchantment(this);

        addHomingBlock(ItemTier.WOOD,
            Blocks.ACACIA_PLANKS,
            Blocks.BIRCH_PLANKS,
            Blocks.DARK_OAK_PLANKS,
            Blocks.JUNGLE_PLANKS,
            Blocks.OAK_PLANKS,
            Blocks.SPRUCE_PLANKS);

        addHomingBlock(ItemTier.STONE,
            Blocks.COBBLESTONE);

        addHomingBlock(ItemTier.IRON,
            Blocks.IRON_ORE);

        addHomingBlock(ItemTier.GOLD,
            Blocks.GOLD_ORE);

        addHomingBlock(ItemTier.DIAMOND,
            Blocks.DIAMOND_ORE);
    }

    public static void addHomingBlock(ItemTier tier, Block... blocks)
    {
        if (!matches.containsKey(tier)) matches.put(tier, new ArrayList<>());
        matches.get(tier).addAll(Arrays.asList(blocks));
    }

    @SubscribeEvent
    public void onBlockInteract(RightClickBlock event)
    {
        ItemStack held = event.getPlayer().getHeldItem(event.getHand());

        if (held.getItem() instanceof TieredItem && EnchantmentsHelper.hasEnchantment(enchantment, held)) {
            World world = event.getWorld();
            PlayerEntity player = event.getPlayer();
            Hand hand = event.getHand();
            BlockPos pos = event.getPos();
            TieredItem item = (TieredItem)held.getItem();
            List<Block> blocks = matches.get(item.getTier());
            if (blocks.isEmpty()) return;

            AtomicReference<Double> distance = new AtomicReference<>((double) 0);
            Stream<BlockPos> inRange = BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range));

            inRange.forEach(p -> {
                if (blocks.contains(world.getBlockState(p).getBlock())) {
                    double d = WorldHelper.getDistanceSq(pos, p);
                    if (distance.get() == 0 || d < distance.get()) distance.set(d);
                }
            });

            if (distance.get() != 0) {
                if (!world.isRemote) {
                    double damage = held.getMaxDamage() * damageMultiplier;
                    PlayerHelper.damageHeldItem(player, hand, held, (int)Math.ceil(damage));
                }

                if (world.isRemote) {
                    double vol = 1 - (Math.min(distance.get(), 100) / 100);
                    double pitch = Math.max(0.5D, 1 - (Math.min(distance.get(), 100) / 100));
                    player.swingArm(event.getHand());
                    world.playSound(player, player.getPosition(), CharmSounds.HOMING, SoundCategory.BLOCKS, (float)vol, (float)pitch);
                }
            }
        }
    }
}
