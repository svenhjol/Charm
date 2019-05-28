package svenhjol.charm.base;

import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import svenhjol.meson.asm.ClassNameMap;
import svenhjol.meson.asm.MesonClassTransformer;

public class CharmClassTransformer extends MesonClassTransformer
{
    private static final String ASM_HOOKS = "svenhjol/charm/base/ASMHooks";

    static {
        CLASS_MAPPINGS = new ClassNameMap(
            "net/minecraft/block/BlockDoor", "aqa",
            "net/minecraft/client/renderer/entity/layers/LayerArmorBase", "cbp",
            "net/minecraft/entity/Entity", "vg",
            "net/minecraft/entity/EntityLivingBase", "vp",
            "net/minecraft/entity/player/EntityPlayer", "aed",
            "net/minecraft/entity/player/EntityPlayer$SleepResult", "aed$a",
            "net/minecraft/entity/player/EntityPlayerMP", "oq",
            "net/minecraft/entity/player/InventoryPlayer", "aec",
            "net/minecraft/init/Blocks", "aox",
            "net/minecraft/inventory/ContainerBrewingStand", "afu",
            "net/minecraft/inventory/ContainerBrewingStand$Potion", "afu$c",
            "net/minecraft/inventory/ContainerFurnace", "agd",
            "net/minecraft/inventory/ContainerRepair", "afs",
            "net/minecraft/inventory/ContainerRepair$2", "afs$2",
            "net/minecraft/inventory/EntityEquipmentSlot", "vl",
            "net/minecraft/inventory/IInventory", "tv",
            "net/minecraft/inventory/SlotShulkerBox", "agq",
            "net/minecraft/item/ItemChorusFruit", "ahk",
            "net/minecraft/item/ItemStack", "aip",
            "net/minecraft/potion/Potion", "uz",
            "net/minecraft/tileentity/TileEntityBeacon", "avh",
            "net/minecraft/tileentity/TileEntityFurnace", "avu",
            "net/minecraft/tileentity/TileEntityShulkerBox", "awb",
            "net/minecraft/util/math/AxisAlignedBB", "bhb",
            "net/minecraft/util/math/BlockPos", "et",
            "net/minecraft/world/gen/structure/MapGenVillage$Start", "bca$a",
            "net/minecraft/world/gen/structure/StructureBoundingBox", "bbg",
            "net/minecraft/world/gen/structure/StructureComponent", "bbx",
            "net/minecraft/world/gen/structure/StructureStart", "bby",
            "net/minecraft/world/gen/structure/StructureVillagePieces$Start", "bcb$k",
            "net/minecraft/world/gen/structure/StructureVillagePieces$Village", "bcb$n",
            "net/minecraft/world/World", "amu"
        );

//        LINENUMBER 188 L15
//        ALOAD 0
//        ICONST_0
//        PUTFIELD net/minecraft/inventory/ContainerRepair.materialCost : I

        transformers.put("net.minecraftforge.common.brewing.BrewingRecipeRegistry", CharmClassTransformer::transformBrewingRecipeRegistry);
        transformers.put("net.minecraft.inventory.ContainerFurnace", CharmClassTransformer::transformContainerFurnace);
        transformers.put("net.minecraft.inventory.ContainerRepair", CharmClassTransformer::transformContainerRepair);
        transformers.put("net.minecraft.inventory.ContainerRepair$2", CharmClassTransformer::transformContainerRepair2);
        transformers.put("net.minecraft.inventory.SlotShulkerBox", CharmClassTransformer::transformSlotShulkerBox);
        transformers.put("net.minecraft.entity.player.EntityPlayer", CharmClassTransformer::transformEntityPlayer);
        transformers.put("net.minecraft.item.ItemChorusFruit", CharmClassTransformer::transformItemChorusFruit);
        transformers.put("net.minecraft.client.renderer.entity.layers.LayerArmorBase", CharmClassTransformer::transformLayerArmorBase);
        transformers.put("net.minecraft.world.gen.structure.StructureStart", CharmClassTransformer::transformStructureStart);
        transformers.put("net.minecraft.world.gen.structure.StructureVillagePieces$Village", CharmClassTransformer::transformStructureVillagePiecesVillage);
        transformers.put("net.minecraft.tileentity.TileEntityBeacon", CharmClassTransformer::transformTileEntityBeacon);
        transformers.put("net.minecraft.tileentity.TileEntityFurnace", CharmClassTransformer::transformTileEntityFurnace);
        transformers.put("net.minecraft.tileentity.TileEntityShulkerBox", CharmClassTransformer::transformTileEntityShulkerBox);
    }

    private static byte[] transformContainerFurnace(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config, "ContainerFurnace")) return basicClass;
        log("Transforming ContainerFurnace");

        MethodSignature init = new MethodSignature("<init>", "<init>", "", "(Lnet/minecraft/entity/player/InventoryPlayer;Lnet/minecraft/inventory/IInventory;)V");

        byte[] transClass = basicClass;

        transClass = transform(transClass, Pair.of(init, combine(
                (AbstractInsnNode node) -> node.getOpcode() == Opcodes.NEW,
                (MethodNode method, AbstractInsnNode node) -> {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new TypeInsnNode(Opcodes.NEW, "svenhjol/charm/smithing/inventory/SlotFurnaceInput"));
                    method.instructions.insert(node, newInstructions);
                    method.instructions.remove(node);
                    return true;
                }
        )));

        transClass = transform(transClass, Pair.of(init, combine(
                (AbstractInsnNode node) -> node.getOpcode() == Opcodes.INVOKESPECIAL && node.getNext().getOpcode() == Opcodes.INVOKEVIRTUAL,
                (MethodNode method, AbstractInsnNode node) -> {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "svenhjol/charm/smithing/inventory/SlotFurnaceInput", "<init>", "(Lnet/minecraft/inventory/IInventory;III)V", false));
                    method.instructions.insert(node, newInstructions);
                    method.instructions.remove(node);
                    return true;
                }
        )));

        return transClass;
    }

    private static byte[] transformTileEntityFurnace(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"TileEntityFurnace")) return basicClass;
        log("Transforming TileEntityFurnace");

        MethodSignature smeltItem = new MethodSignature("smeltItem", "func_145949_j", "o", "()V");
        MethodSignature isItemValidForSlot = new MethodSignature("isItemValidForSlot", "func_94041_b", "b", "(ILnet/minecraft/item/ItemStack;)Z");
        byte[] transClass = basicClass;

        transClass = transform(transClass, Pair.of(smeltItem, combine(
                (AbstractInsnNode node) -> node.getOpcode() == Opcodes.ASTORE && ((VarInsnNode)node).var == 2,
                (MethodNode method, AbstractInsnNode node) -> {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "changeSmeltingResult", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;", false));
                    newInstructions.add(new VarInsnNode(Opcodes.ASTORE, 2));
                    method.instructions.insert(node, newInstructions);
                    return true;
                }
        )));

        transClass = transform(transClass, Pair.of(isItemValidForSlot, combine(
                (AbstractInsnNode node) -> node.getOpcode() == Opcodes.ILOAD && ((VarInsnNode)node).var == 1,
                (MethodNode method, AbstractInsnNode node) -> {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 1));
                    LabelNode label = new LabelNode();
                    newInstructions.add(new JumpInsnNode(Opcodes.IFNE, label));
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "canSmelt", "(Lnet/minecraft/item/ItemStack;)Z", false));
                    newInstructions.add(new InsnNode(Opcodes.IRETURN));
                    newInstructions.add(label);
                    method.instructions.insertBefore(node, newInstructions);
                    return true;
                }
        )));

        return transClass;
    }

    private static byte[] transformItemChorusFruit(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"ItemChorusFruit")) return basicClass;
        log("Transforming ItemChorusFruit");

        MethodSignature onItemUseFinish = new MethodSignature("onItemUseFinish", "func_77654_b", "a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Lnet/minecraft/item/ItemStack;");

        byte[] transClass = basicClass;

        transClass = transform(transClass, Pair.of(onItemUseFinish, combine(
                (AbstractInsnNode node) -> node.getOpcode() == Opcodes.IFNE,
                (MethodNode method, AbstractInsnNode node) -> {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "onChorusFruitEaten", "(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLivingBase;)Z", false));
                    newInstructions.add(new JumpInsnNode(Opcodes.IFNE, ((JumpInsnNode)node).label));
                    method.instructions.insert(node, newInstructions);
                    return true;
                }
        )));

        return transClass;
    }

    private static int countTransformTileEntityBeacon;

    private static byte[] transformTileEntityBeacon(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"TileEntityBeacon")) return basicClass;
        log("Transforming TileEntityBeacon");

        MethodSignature addEffectsToPlayers = new MethodSignature("addEffectsToPlayers", "func_146000_x", "E", "()V");

        byte[] transClass = basicClass;

        transClass = transform(transClass, Pair.of(addEffectsToPlayers, combine(
                (AbstractInsnNode node) -> node.getOpcode() == Opcodes.ALOAD && ((VarInsnNode)node).var == 9 && ++countTransformTileEntityBeacon == 2,
                (MethodNode method, AbstractInsnNode node) -> {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityBeacon", "world", "Lnet/minecraft/world/World;"));
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 8));
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityBeacon", "primaryEffect", "Lnet/minecraft/potion/Potion;"));
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/TileEntityBeacon", "secondaryEffect", "Lnet/minecraft/potion/Potion;"));
                    newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 4));
                    newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 3));
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "addBeaconEffect", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/AxisAlignedBB;Lnet/minecraft/potion/Potion;Lnet/minecraft/potion/Potion;II)V", false));
                    method.instructions.insertBefore(node, newInstructions);
                    return true;
                }
        )));

        return transClass;
    }

    private static byte[] transformTileEntityShulkerBox(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"TileEntityShulkerBox")) return basicClass;
        log("Transforming TileEntityShulkerBox");

        MethodSignature canInsertItem = new MethodSignature("canInsertItem", "func_180462_a", "a", "(ILnet/minecraft/item/ItemStack;Lnet/minecraft/util/EnumFacing;)Z");

        byte[] transClass = basicClass;

        transClass = transform(transClass, Pair.of(canInsertItem, combine(
            (AbstractInsnNode node) -> node.getOpcode() == Opcodes.ALOAD && ((VarInsnNode)node).var == 2,
            (MethodNode method, AbstractInsnNode node) -> {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "canInsertItemIntoShulkerBox", "(Lnet/minecraft/item/ItemStack;)Z", false));
                newInstructions.add(new InsnNode(Opcodes.IRETURN));
                method.instructions = newInstructions;
                return true;
            }
        )));

        return transClass;
    }

    private static byte[] transformSlotShulkerBox(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"SlotShulkerBox")) return basicClass;
        log("Transforming SlotShulkerBox");

        MethodSignature isItemValid = new MethodSignature("isItemValid", "func_180462_a", "a", "(Lnet/minecraft/item/ItemStack;)Z");

        byte[] transClass = basicClass;

        transClass = transform(transClass, Pair.of(isItemValid, combine(
            (AbstractInsnNode node) -> node.getOpcode() == Opcodes.ALOAD && ((VarInsnNode)node).var == 1,
            (MethodNode method, AbstractInsnNode node) -> {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "canInsertItemIntoShulkerBox", "(Lnet/minecraft/item/ItemStack;)Z", false));
                newInstructions.add(new InsnNode(Opcodes.IRETURN));
                method.instructions = newInstructions;
                return true;
            }
        )));

//        LINENUMBER 21 L0
//        ALOAD 1
//        INVOKEVIRTUAL net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
//        INVOKESTATIC net/minecraft/block/Block.getBlockFromItem (Lnet/minecraft/item/Item;)Lnet/minecraft/block/Block;
//        INSTANCEOF net/minecraft/block/BlockShulkerBox
//        IFNE L1
//        ICONST_1
//        GOTO L2

        return transClass;
    }

    private static byte[] transformBrewingRecipeRegistry(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"BrewingRecipeRegistry")) return basicClass;
        log("Transforming BrewingRecipeRegistry");

        MethodSignature getSlotStackLimit = new MethodSignature("isValidInput", "isValidInput", "", "(Lnet/minecraft/item/ItemStack;)Z");

        byte[] transClass = basicClass;

        transClass = transform(transClass, Pair.of(getSlotStackLimit, combine(
                (AbstractInsnNode node) -> node.getOpcode() == Opcodes.IF_ICMPEQ,
                (MethodNode method, AbstractInsnNode node) -> {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new JumpInsnNode(Opcodes.IF_ICMPGE, ((JumpInsnNode)node).label));
                    method.instructions.insertBefore(node, newInstructions);
                    method.instructions.remove(node);
                    return true;
                }
        )));

        return transClass;
    }

    private static int countTransformContainerRepair;

    /**
     * ContainerRepair: Class transformer
     * - allow for >1 material cost by default
     *
     * @param basicClass Class to transform
     * @return Transformed class
     */
    private static byte[] transformContainerRepair(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"ContainerRepair")) return basicClass;
        log("Transforming ContainerRepair");

        MethodSignature updateRepairOutput = new MethodSignature("updateRepairOutput", "func_82848_d", "e", "()V");
        byte[] transClass = basicClass;

        transClass = transform(transClass, Pair.of(updateRepairOutput, combine(
            (AbstractInsnNode node) -> node.getOpcode() == Opcodes.ICONST_0
                && node.getPrevious().getOpcode() == Opcodes.ALOAD
                && ((VarInsnNode)node.getPrevious()).var == 0
                && ++countTransformContainerRepair == 2,
            (MethodNode method, AbstractInsnNode node) -> {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new InsnNode(Opcodes.ICONST_1));
                method.instructions.insert(node, newInstructions);
                method.instructions.remove(node);
                return true;
            }
        )));

        return transClass;
    }

    /**
     * ContainerRepair$2: Class transformer
     * - allow for zero XP cost
     *
     * @param basicClass Class to transform
     * @return Transformed class
     */
    private static byte[] transformContainerRepair2(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"ContainerRepair2")) return basicClass;
        log("Transforming ContainerRepair2");

        MethodSignature canTakeStack = new MethodSignature("canTakeStack", "func_82869_a", "a", "(Lnet/minecraft/entity/player/EntityPlayer;)Z");
        byte[] transClass = basicClass;

        transClass = transform(transClass, Pair.of(canTakeStack, combine(
            (AbstractInsnNode node) -> node.getOpcode() == Opcodes.IFLE,
            (MethodNode method, AbstractInsnNode node) -> {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new JumpInsnNode(Opcodes.IFLT, ((JumpInsnNode)node).label));
                method.instructions.insertBefore(node, newInstructions);
                method.instructions.remove(node);
                return true;
            }
        )));

        return transClass;
    }

    /**
     * LayerArmorBase: Class transformer
     * - check if the armour is leather and the player is invisible, prevents renderArmorLayer from rendering the armour if true.
     *
     * @param basicClass Class to transform
     * @return Transformed class
     */
    private static byte[] transformLayerArmorBase(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"LayerArmorBase")) return basicClass;
        log("Transforming LayerArmorBase");

        MethodSignature renderArmorLayer = new MethodSignature("renderArmorLayer", "func_188361_a", "a", "(Lnet/minecraft/entity/EntityLivingBase;FFFFFFFLnet/minecraft/inventory/EntityEquipmentSlot;)V");
        byte[] transClass = basicClass;

        // skip render if armor is flagged as invisible
        transClass = transform(transClass, Pair.of(renderArmorLayer, combine(
            (AbstractInsnNode node) -> node.getOpcode() == Opcodes.ASTORE,
            (MethodNode method, AbstractInsnNode node) -> {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 10));
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "isArmorInvisible", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Z", false));
                LabelNode label = new LabelNode();
                newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                newInstructions.add(new InsnNode(Opcodes.RETURN));
                newInstructions.add(label);

                method.instructions.insert(node, newInstructions);
                return true;
            }
        )));

        return transClass;
    }

    /**
     * EntityPlayer: Class transformer
     * - prevents detection of player if they are invisible and wearing leather armour
     *
     * @param basicClass Class to transform
     * @return Transformed class
     */
    private static byte[] transformEntityPlayer(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"EntityPlayer")) return basicClass;
        log("Transforming EntityPlayer");

        MethodSignature getArmorVisibility = new MethodSignature("getArmorVisibility", "func_82243_bO", "cW", "()F");

        byte[] transClass = basicClass;

        // don't increase mob visibility if armor is flagged as invisible
        transClass = transform(transClass, Pair.of(getArmorVisibility, combine(
            (AbstractInsnNode node) -> node.getOpcode() == Opcodes.IINC,
            (MethodNode method, AbstractInsnNode node) -> {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "isArmorInvisible", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Z", false));
                LabelNode label = new LabelNode();
                newInstructions.add(new JumpInsnNode(Opcodes.IFNE, label));
                newInstructions.add(new IincInsnNode(1, 1));
                newInstructions.add(label);

                method.instructions.insertBefore(node, newInstructions);
                method.instructions.remove(node);
                return true;
            }
        )));

        return transClass;
    }

    /**
     * StructureVillagePieces$Village: Class transformer
     * - adds a GetVillageBlockID event fire to the `biomeDoor` method so that the correct door wood can be used when building villages.
     *
     * @param basicClass Class to transform
     * @return Transformed class
     */
    private static byte[] transformStructureVillagePiecesVillage(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"StructureVillagePieces")) return basicClass;
        log("Transforming StructureVillagePieces$Village");

        MethodSignature biomeDoor = new MethodSignature("biomeDoor", "func_189925_i", "i", "()Lnet/minecraft/block/BlockDoor;");
        return transform(basicClass, Pair.of(biomeDoor, combine(
            (AbstractInsnNode node) -> node.getOpcode() == Opcodes.ALOAD,
            (MethodNode method, AbstractInsnNode node) -> {
                InsnList newInstructions = new InsnList();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/world/gen/structure/StructureVillagePieces$Village", "startPiece", "Lnet/minecraft/world/gen/structure/StructureVillagePieces$Start;"));
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "villageDoorsForBiome", "(Lnet/minecraft/world/gen/structure/StructureVillagePieces$Start;)Lnet/minecraft/block/BlockDoor;", false));
                newInstructions.add(new InsnNode(Opcodes.ARETURN));
                method.instructions = newInstructions;
                return true;
            }
        )));
    }

    /**
     * StructureStart: Class transformer
     * - change the addComponentParts method so we can hook into it while rendering is happening.
     *
     * @param basicClass Class to transform
     * @return Transformed class
     */
    private static byte[] transformStructureStart(byte[] basicClass)
    {
        if (!checkTransformers(CharmLoadingPlugin.config,"StructureStart")) return basicClass;
        log("Transforming StructureStart");

        byte[] transformClass = basicClass;
        MethodSignature generateStructure = new MethodSignature(
                "generateStructure",
                "func_75068_a",
                "a",
                "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/gen/structure/StructureBoundingBox;)V"
        );
        // replace the addComponentParts() call with custom version so we can call the items after generating a structure
        transformClass = transform(transformClass, Pair.of(generateStructure, combine(
                (AbstractInsnNode node) -> node.getOpcode() == Opcodes.INVOKEVIRTUAL
                    && checkDesc(((MethodInsnNode)node).desc, "(Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/gen/structure/StructureBoundingBox;)Z")
                ,
                (MethodNode method, AbstractInsnNode node) -> {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "addComponentParts", "(Lnet/minecraft/world/gen/structure/StructureComponent;Lnet/minecraft/world/World;Ljava/util/Random;Lnet/minecraft/world/gen/structure/StructureBoundingBox;)Z", false));
                    method.instructions.insert(node, newInstructions);
                    method.instructions.remove(node);
                    return true;
                }
        )));
        return transformClass;
    }
}