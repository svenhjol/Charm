function initializeCoreMod() {

    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    return {

        // add hook to PotionItem so we can disable the glint
        'PotionItem': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.item.PotionItem',
                'methodName': 'hasEffect',
                'methodDesc': '(Lnet/minecraft/item/ItemStack;)Z'
            },
            'transformer': function(method) {
                var Opcodes = Java.type('org.objectweb.asm.Opcodes');
                var arrayLength = method.instructions.size();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = [];

                    if (instruction.getOpcode() == Opcodes.ALOAD) {
                        var label = new LabelNode();
                        newInstructions.push(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "removePotionGlint", "()Z", false));
                        newInstructions.push(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.push(new InsnNode(Opcodes.ICONST_0));
                        newInstructions.push(new InsnNode(Opcodes.IRETURN));
                        newInstructions.push(label);

                        newInstructions.forEach(function(i) {
                            method.instructions.insertBefore(instruction, i);
                        });

                        print("Transformed PotionItem");
                        break;
                    }
                }

                return method;
            }
        }
    }
}