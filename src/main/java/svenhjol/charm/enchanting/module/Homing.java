package svenhjol.charm.enchanting.module;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.command.arguments.EntityAnchorArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.TieredItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Module(mod = Charm.MOD_ID, category = CharmCategories.ENCHANTING, hasSubscriptions = true,
    description = "A tool with the Homing enchantment is attracted to ore/wood/stone of the same type that make up the head of the tool.\n" +
        "Right click underground and if you hear a sound, you can follow it to the source.")
public class Homing extends MesonModule {
    public static HomingEnchantment enchantment;
    public static HashMap<IItemTier, List<Block>> matches = new HashMap<>();

    @Config(name = "Detection range", description = "Range (in blocks) that the enchanted tool will detect ore/wood/stone.")
    public static int range = 8;

    @Config(name = "Damage multiplier", description = "Percentage (where 1.0 is 100%) of damage given to the tool every time it detects a block.")
    public static double damageMultiplier = 0.015D;

    @Override
    public void init() {
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

    public static void addHomingBlock(ItemTier tier, Block... blocks) {
        if (!matches.containsKey(tier)) matches.put(tier, new ArrayList<>());
        matches.get(tier).addAll(Arrays.asList(blocks));
    }

    @SubscribeEvent
    public void onBlockInteract(RightClickBlock event) {
        ItemStack held = event.getPlayer().getHeldItem(event.getHand());

        if (held.getItem() instanceof TieredItem && EnchantmentsHelper.hasEnchantment(enchantment, held)) {
            World world = event.getWorld();
            PlayerEntity player = event.getPlayer();
            Hand hand = event.getHand();
            BlockPos pos = event.getPos();
            TieredItem item = (TieredItem) held.getItem();

            if (!matches.containsKey(item.getTier()))
                return;

            List<Block> matchedBlocks = matches.get(item.getTier());
            if (matchedBlocks.isEmpty())
                return;

            double distance = 128.0D;
            Stream<BlockPos> inRange = BlockPos.getAllInBox(pos.add(-range, -range, -range), pos.add(range, range, range));
            List<BlockPos> positions = inRange.map(BlockPos::toImmutable).collect(Collectors.toList());

            BlockPos foundPos = null;

            for (BlockPos blockPos : positions) {
                if (!matchedBlocks.contains(world.getBlockState(blockPos).getBlock())) continue;
                double d = WorldHelper.getDistanceSq(pos, blockPos);
                if (d < distance) {
                    distance = d;
                    foundPos = blockPos;
                }
            }

            if (foundPos != null) {
                player.lookAt(EntityAnchorArgument.Type.EYES, new Vec3d(foundPos).add(0.5F, 0.5F, 0.5F));

                if (!world.isRemote) {
                    double damage = held.getMaxDamage() * damageMultiplier;
                    PlayerHelper.damageHeldItem(player, hand, held, (int) Math.ceil(damage));
                }

                if (world.isRemote) {
                    double vol = 1 - (Math.min(distance, 100) / 100);
                    double pitch = Math.max(0.5D, 1 - (Math.min(distance, 100) / 100));
                    player.swingArm(event.getHand());
                    world.playSound(player, player.getPosition(), CharmSounds.HOMING, SoundCategory.BLOCKS, (float) vol, (float) pitch);
                }
            }
        }
    }
}
