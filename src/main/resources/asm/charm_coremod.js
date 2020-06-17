function initializeCoreMod() {

    var ASM_HOOKS = "svenhjol/charm/base/CharmAsmHooks";
    var ASM = Java.type('net.minecraftforge.coremod.api.ASMAPI');
    var Opcodes = Java.type('org.objectweb.asm.Opcodes');
    var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
    var InsnList = Java.type('org.objectweb.asm.tree.InsnList');
    var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
    var IincInsnNode = Java.type('org.objectweb.asm.tree.IincInsnNode');
    var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
    var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
    var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
    var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');

    return {

        /*
         * BrewingRecipeRegistry: allow potion stacks to be added to brewing stand slots.
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
                var newInstructions = new InsnList();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i)

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
                    print("[Charm ASM] Transformed BrewingRecipeRegistry");
                } else {
                    print("[Charm ASM] Failed to transform BrewingRecipeRegistry")
                }

                return method;
            }
        },

        /*
         * PotionItem: disable glint
         */
        'PotionItem': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.item.PotionItem',
                'methodName': 'func_77636_d', // hasEffect
                'methodDesc': '(Lnet/minecraft/item/ItemStack;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();
                var newInstructions = new InsnList();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);

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
                    print("[Charm ASM] Transformed PotionItem");
                } else {
                    print("[Charm ASM] Failed to transform PotionItem")
                }

                return method;
            }
        },

        /*
         * RepairContainer: allow an anvil XP cost of zero.
         */
        'RepairContainer': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.inventory.container.RepairContainer$2',
                'methodName': 'func_82869_a', // canTakeStack
                'methodDesc': '(Lnet/minecraft/entity/player/PlayerEntity;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();
                var newInstructions = new InsnList();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);

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
                    print("[Charm ASM] Transformed RepairContainer");
                } else {
                    print("[Charm ASM] Failed to transform RepairContainer")
                }

                return method;
            }
        },

        /*
         * ArmorLayer: skip rendering of armor if player is invisible.
         */
        'ArmorLayer': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.entity.layers.ArmorLayer',
                'methodName': 'func_229129_a_', // renderArmorPart
                'methodDesc': '(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;Lnet/minecraft/entity/LivingEntity;FFFFFFLnet/minecraft/inventory/EquipmentSlotType;I)V'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();
                var newInstructions = new InsnList();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);

                    if (instruction.getOpcode() == Opcodes.ASTORE) {
                        var label = new LabelNode();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 12));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "isArmorInvisible", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Z", false));
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new InsnNode(Opcodes.RETURN));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }

                if (didThing) {
                    print("[Charm ASM] Transformed ArmorLayer");
                } else {
                    print("[Charm ASM] Failed to transform ArmorLayer")
                }

                return method;
            }
        },

        /*
         * LivingEntity: skip adding armor visibility if leather item
         */
        'LivingEntity': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.LivingEntity',
                'methodName': 'func_213343_cS', // func_213343_cS
                'methodDesc': '()F'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();
                var newInstructions = new InsnList();
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);

                    if (instruction.getOpcode() == Opcodes.IINC) {
                        var label = new LabelNode();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 5));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "isArmorInvisible", "(Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Z", false));
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new IincInsnNode(3, -1));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }

                if (didThing) {
                    print("[Charm ASM] Transformed LivingEntity");
                } else {
                    print("[Charm ASM] Failed to transform LivingEntity")
                }

                return method;
            }
        },

        /*
         * BeaconTileEntity: handle other mobs in area.
         */
        'BeaconTileEntity': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.tileentity.BeaconTileEntity',
                'methodName': 'func_146000_x', // addEffectsToPlayers
                'methodDesc': '()V'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();
                var instruction = method.instructions.get(0);
                var newInstructions = new InsnList();

                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_145850_b"), "Lnet/minecraft/world/World;")); // world
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_146012_l"), "I")); // levels
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_174879_c"), "Lnet/minecraft/util/math/BlockPos;")); // pos
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_146013_m"), "Lnet/minecraft/potion/Effect;")); // primaryEffect
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/tileentity/BeaconTileEntity", ASM.mapField("field_146010_n"), "Lnet/minecraft/potion/Effect;"));
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "mobsInBeaconRange", "(Lnet/minecraft/world/World;ILnet/minecraft/util/math/BlockPos;Lnet/minecraft/potion/Effect;Lnet/minecraft/potion/Effect;)V", false));

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] Transformed BeaconTileEntity");

                return method;
            }
        },

        /*
         * ItemStack: check item damage.
         * Hook directly after this.setDamage(l).
         */
        'ItemStack': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.item.ItemStack',
                'methodName': 'func_96631_a', // attemptDamageItem
                'methodDesc': '(ILjava/util/Random;Lnet/minecraft/entity/player/ServerPlayerEntity;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.INVOKEVIRTUAL
                        && (instruction.name == "func_196085_b" || instruction.name == "setDamage")
                    ) {
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        newInstructions.add(new VarInsnNode(Opcodes.ILOAD, 4));
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "itemDamaged", "(Lnet/minecraft/item/ItemStack;ILnet/minecraft/entity/player/ServerPlayerEntity;)V", false));

                        method.instructions.insert(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }

                if (didThing) {
                    print("[Charm ASM] Transformed ItemStack");
                } else {
                    print("[Charm ASM] Failed to transform ItemStack");
                }

                return method;
            }
        },

        /*
         * HuskEntity: don't check skylight for spawning.
         */
        'HuskEntity': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.monster.HuskEntity',
                'methodName': 'func_223334_b',
                'methodDesc': '(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/IWorld;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Ljava/util/Random;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.INVOKEINTERFACE) {
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "isSkyLightMax", "(Lnet/minecraft/world/IWorld;Lnet/minecraft/util/math/BlockPos;)Z", false));

                        method.instructions.insert(instruction, newInstructions);
                        method.instructions.remove(instruction);
                        didThing = true;
                        break;
                    }
                }

                if (didThing) {
                    print("[Charm ASM] Transformed HuskEntity");
                } else {
                    print("[Charm ASM] Failed to transform HuskEntity");
                }

                return method;
            }
        },

        /*
         * Composter: allow alternative output handling
         */
        'ComposterBlock': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.block.ComposterBlock',
                'methodName': 'func_225533_a_', // onBlockActivated
                'methodDesc': '(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/math/BlockRayTraceResult;)Lnet/minecraft/util/ActionResultType;'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.INVOKEVIRTUAL
                        && (instruction.name == "func_184133_a" || instruction.name == "playSound")
                    ) {
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 3));
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "composterOutput", "(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;)V", false));

                        method.instructions.insert(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }

                if (didThing) {
                    print("[Charm ASM] Transformed ComposterBlock");
                } else {
                    print("[Charm ASM] Failed to transform ComposterBlock");
                }

                return method;
            }
        },


        /*
         * MusicTicker: override music ticker tick
         */
        'MusicTickerTick': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.client.audio.MusicTicker',
                'methodName': 'func_73660_a', // tick
                'methodDesc': '()V'
            },
            transformer: function(method) {
                var didThing = false;
                var instruction = method.instructions.get(0);
                var newInstructions = new InsnList();

                var label = new LabelNode();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                newInstructions.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/audio/MusicTicker", ASM.mapField("field_147678_c"), "Lnet/minecraft/client/audio/ISound;")); // currentMusic
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "handleMusicTick", "(Lnet/minecraft/client/audio/ISound;)Z", false));
                newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                newInstructions.add(new InsnNode(Opcodes.RETURN));
                newInstructions.add(label);

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] Transformed MusicTicker tick");

                return method;
            }
        },

        /*
         * MusicTicker: override music ticker stop
         */
        'MusicTickerStop': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.client.audio.MusicTicker',
                'methodName': 'func_209200_a', // stop
                'methodDesc': '()V'
            },
            transformer: function(method) {
                var didThing = false;
                var instruction = method.instructions.get(0);
                var newInstructions = new InsnList();

                var label = new LabelNode();
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "handleMusicStop", "()Z", false));
                newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                newInstructions.add(new InsnNode(Opcodes.RETURN));
                newInstructions.add(label);

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] Transformed MusicTicker stop");

                return method;
            }
        },

        /*
         * MusicTicker: override music ticker isPlaying
         */
        'MusicTickerPlaying': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.client.audio.MusicTicker',
                'methodName': 'func_209100_b', // isPlaying
                'methodDesc': '(Lnet/minecraft/client/audio/MusicTicker$MusicType;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var instruction = method.instructions.get(0);
                var newInstructions = new InsnList();

                var label = new LabelNode();
                newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "handleMusicPlaying", "(Lnet/minecraft/client/audio/MusicTicker$MusicType;)Z", false));
                newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                newInstructions.add(new InsnNode(Opcodes.ICONST_1));
                newInstructions.add(new InsnNode(Opcodes.IRETURN));
                newInstructions.add(label);

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] Transformed MusicTicker isPlaying");

                return method;
            }
        },

        /*
         * PlayerEntity: override music ticker isPlaying
         */
        'PlayerEntity': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.player.PlayerEntity',
                'methodName': 'func_192030_dh', // spawnShoulderEntities
                'methodDesc': '()V'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.IFGE) {
                        var label = new LabelNode();
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "stayOnShoulder", "()Z", false));
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new InsnNode(Opcodes.RETURN));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] Transformed PlayerEntity spawnShoulderEntities");

                return method;
            }
        },

        /*
         * ParrotEntity: add extra goals
         */
        'ParrotEntityRegisterGoals': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.passive.ParrotEntity',
                'methodName': 'func_184651_r', // registerGoals
                'methodDesc': '()V'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.RETURN) {
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "addParrotGoals", "(Lnet/minecraft/entity/passive/ParrotEntity;)V", false));

                        method.instructions.insertBefore(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] Transformed ParrotEntity registerGoals");

                return method;
            }
        },


        /*
         * ParrotEntity: configurable mimic sound delay
         */
        'ParrotEntityPlayMimicSound': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.passive.ParrotEntity',
                'methodName': 'func_192006_b', // playMimicSound
                'methodDesc': '(Lnet/minecraft/world/World;Lnet/minecraft/entity/Entity;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();

                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.BIPUSH) {
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "parrotMimicDelayChance", "()I", false));

                        method.instructions.insert(instruction, newInstructions);
                        method.instructions.remove(instruction);
                        didThing = true;
                        break;
                    }
                }

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] Transformed ParrotEntity playMimicSound");

                return method;
            }
        },

        /*
         * InventoryTransferHandler: horrible hack to allow crates and bookshelf chests to get Quark's transfer. I'm really sorry.
         */
        'InventoryTransferHandler': {
            target: {
                'type': 'METHOD',
                'class': 'vazkii.quark.base.handler.InventoryTransferHandler',
                'methodName': 'accepts', // accepts
                'methodDesc': '(Lnet/minecraft/inventory/container/Container;Lnet/minecraft/entity/player/PlayerEntity;)Z'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();

                var j = 0;
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.ALOAD
                        && instruction.var == 0
                        && ++j == 3
                    ) {
                        var label = new LabelNode();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "containersAcceptTransfer", "(Lnet/minecraft/inventory/container/Container;)Z", false));
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new InsnNode(Opcodes.ICONST_1));
                        newInstructions.add(new InsnNode(Opcodes.IRETURN));
                        newInstructions.add(label);

                        method.instructions.insertBefore(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] Transformed InventoryTransferHandler accepts");

                return method;
            }
        },

        /*
         * GameData: this hack gets around Forge's blockstate prop check. It's definitely evil and I'm going to the Nether for this.
         * It's needed to add waterloggable props to lanterns (and possibly other blocks).
         */
        'GameData$BlockCallbacks': {
            target: {
                'type': 'METHOD',
                'class': 'net.minecraftforge.registries.GameData$BlockCallbacks',
                'methodName': 'onAdd', // accepts
                'methodDesc': '(Lnet/minecraftforge/registries/IForgeRegistryInternal;Lnet/minecraftforge/registries/RegistryManager;ILnet/minecraft/block/Block;Lnet/minecraft/block/Block;)V'
            },
            transformer: function(method) {
                var didThing = false;
                var arrayLength = method.instructions.size();

                var j = 0;
                for (var i = 0; i < arrayLength; ++i) {
                    var instruction = method.instructions.get(i);
                    var newInstructions = new InsnList();

                    if (instruction.getOpcode() == Opcodes.IFNE) {
                        var label = new LabelNode();
                        newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 4));
                        newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, ASM_HOOKS, "bypassStateCheck", "(Lnet/minecraft/block/Block;)Z", false));
                        newInstructions.add(new JumpInsnNode(Opcodes.IFEQ, label));
                        newInstructions.add(new InsnNode(Opcodes.RETURN));
                        newInstructions.add(label);

                        method.instructions.insert(instruction, newInstructions);
                        didThing = true;
                        break;
                    }
                }

                method.instructions.insertBefore(instruction, newInstructions);
                print("[Charm ASM] Transformed GameData$BlockCallbacks onAdd");

                return method;
            }
        }
    }
}