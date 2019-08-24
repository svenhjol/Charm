function initializeCoreMod() {

    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    return {

        /*
         * Add hook to BrewingRecipeRegistry::isValidInput
         * so that potion stacks can be added to brewing stand slots.
         */
        'BrewingRecipeRegistry': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraftforge.common.brewing.BrewingRecipeRegistry',
                'methodName': 'isValidInput',
                'methodDesc': '(Lnet/minecraft/item/ItemStack;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i)
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.IF_ICMPEQ) {
                        var label = new LabelNode();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "checkBrewingStandStack", "(Lnet/minecraft/item/ItemStack;)Z", false));
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new InsnNode(Opcodes.ICONST_1));
                        newInstructions.add(new InsnNode(Opcodes.IRETURN));
                        newInstructions.add(label);

                        method.instructions.insertBefore(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }

                if (didThing) {
                    print("Transformed BrewingRecipeRegistry");
                } else {
                    print("Failed to transform BrewingRecipeRegistry")
                }

                return method;
            }
        },

        /*
         * Add hook to PotionItem so glint can be disabled in config.
         */
        'PotionItem': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.item.PotionItem',
                'methodName': 'hasEffect',
                'methodDesc': '(Lnet/minecraft/item/ItemStack;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.ALOAD) {
                        var label = new LabelNode();
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "removePotionGlint", "()Z", false));
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new InsnNode(Opcodes.ICONST_0));
                        newInstructions.add(new InsnNode(Opcodes.IRETURN));
                        newInstructions.add(label);

                        method.instructions.insertBefore(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }


                if (didThing) {
                    print("Transformed PotionItem");
                } else {
                    print("Failed to transform PotionItem")
                }

                return method;
            }
        },

        /*
         * Add hook to RepairContainer to be able to set an anvil XP cost of zero
         */
        'RepairContainer': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.inventory.container.RepairContainer$2',
                'methodName': 'canTakeStack',
                'methodDesc': '(Lnet/minecraft/entity/player/PlayerEntity;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.IFLE) {
                        var label = instruction.label;
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "getMinimumRepairCost", "()I", false));
                        newInstructions.add(new JumpInsnNode(Opcodes.IF_ICMPLE, label));

                        method.instructions.insert(instruction, newInstructions);
                        method.instructions.remove(instruction);
                        didThing = true;
                        break;
                    }
                }

                if (didThing) {
                    print("Transformed RepairContainer");
                } else {
                    print("Failed to transform RepairContainer")
                }

                return method;
            }
        }
    }
}