package svenhjol.charm.base;

import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;
import svenhjol.meson.asm.ClassNameMap;
import svenhjol.meson.asm.MesonClassTransformer;

public class ClassTransformer extends MesonClassTransformer
{
    private static final String ASM_HOOKS = "svenhjol/charm/base/ASMHooks";

    static {
        // find these from mcp-notch.srg
        CLASS_MAPPINGS = new ClassNameMap(
                "net/minecraft/init/Blocks", "aox",
                "net/minecraft/entity/Entity", "vg",
                "net/minecraft/entity/player/EntityPlayerMP", "oq",
                "net/minecraft/entity/player/EntityPlayer$SleepResult", "aed$a",
                "net/minecraft/entity/player/EntityPlayer", "aed",
                "net/minecraft/world/gen/structure/StructureStart", "bby",
                "net/minecraft/world/gen/structure/StructureVillagePieces$Village", "bcb$n",
                "net/minecraft/world/gen/structure/StructureVillagePieces$Start", "bcb$k",
                "net/minecraft/client/renderer/entity/layers/LayerArmorBase", "cbp",
                "net/minecraft/world/gen/structure/MapGenVillage$Start", "bca$a",
                "net/minecraft/inventory/ContainerRepair$2", "afs$2",
                "net/minecraft/entity/EntityLivingBase", "vp",
                "net/minecraft/inventory/EntityEquipmentSlot", "vl",
                "net/minecraft/util/math/BlockPos", "et",
                "net/minecraft/block/BlockDoor", "aqa",
                "net/minecraft/world/World", "amu",
                "net/minecraft/world/gen/structure/StructureBoundingBox", "bbg",
                "net/minecraft/world/gen/structure/StructureComponent", "bbx",
                "net/minecraft/item/ItemStack", "aip",
                "net/minecraft/inventory/ContainerBrewingStand", "afu",
                "net/minecraft/inventory/ContainerBrewingStand$Potion", "afu$c"
        );

        transformers.put("net.minecraft.world.gen.structure.StructureStart",
            ClassTransformer::transformStructureStart);
        transformers.put("net.minecraft.world.gen.structure.StructureVillagePieces$Village",
            ClassTransformer::transformStructureVillagePiecesVillage);
        transformers.put("net.minecraft.entity.player.EntityPlayer",
            ClassTransformer::transformEntityPlayer);
        transformers.put("net.minecraft.client.renderer.entity.layers.LayerArmorBase",
            ClassTransformer::transformLayerArmorBase);
        transformers.put("net.minecraft.inventory.ContainerRepair$2",
            ClassTransformer::transformContainerRepair);
        transformers.put("net.minecraftforge.common.brewing.BrewingRecipeRegistry",
            ClassTransformer::transformBrewingRecipeRegistry);
    }

    private static byte[] transformBrewingRecipeRegistry(byte[] basicClass)
    {
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

    /**
     * ContainerRepair: Class transformer
     * - allow for zero XP cost
     *
     * @param basicClass Class to transform
     * @return Transformed class
     */
    private static byte[] transformContainerRepair(byte[] basicClass)
    {
        log("Transforming ContainerRepair");
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