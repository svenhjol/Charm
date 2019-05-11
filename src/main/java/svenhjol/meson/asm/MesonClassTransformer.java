package svenhjol.meson.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.Printer;
import org.objectweb.asm.util.Textifier;
import org.objectweb.asm.util.TraceMethodVisitor;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.ConfigHelper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static svenhjol.meson.helper.ConfigHelper.TRANSFORMERS;

public abstract class MesonClassTransformer implements IClassTransformer
{
    protected static final Map<String, Transformer> transformers = new HashMap<>();
    protected static ClassNameMap CLASS_MAPPINGS = null;

    public interface Transformer extends Function<byte[], byte[]> { }
    public interface MethodAction extends Predicate<MethodNode> { }
    public interface NodeFilter extends Predicate<AbstractInsnNode> { }
    public interface NodeAction extends BiPredicate<MethodNode, AbstractInsnNode> { }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass)
    {
        if (transformers.containsKey(transformedName)) {
            return transformers.get(transformedName).apply(basicClass);
        }

        return basicClass;
    }

    public static class MethodSignature
    {
        String funcName, srgName, obfName, funcDesc, obfDesc;

        public MethodSignature(String funcName, String srgName, String obfName, String funcDesc)
        {
            this.funcName = funcName;
            this.srgName = srgName;
            this.obfName = obfName;
            this.funcDesc = funcDesc;
            this.obfDesc = obfuscate(funcDesc);
        }

        @Override
        public String toString() {
            return "Names [" + funcName + ", " + srgName + ", " + obfName + "] Descriptor " + funcDesc + " / " + obfDesc;
        }

        public static String obfuscate(String desc)
        {
            for (String s : CLASS_MAPPINGS.keySet())
            {
                int i = 0;
                while (desc.contains(s)) {
                    desc = desc.replace(s, CLASS_MAPPINGS.get(s));
                    if (i++ > 10) Meson.fatal("Nope");
                }
            }
            return desc;
        }
    }

    @SafeVarargs
    public static byte[] transform(byte[] basicClass, Pair<MethodSignature, MethodAction>... methods)
    {
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        boolean didAnything = false;

        for (Pair<MethodSignature, MethodAction> pair : methods) {
            log("Applying Transformation to method (" + pair.getLeft() + ")");
            didAnything |= findMethodAndTransform(node, pair.getLeft(), pair.getRight());
        }

        if (didAnything) {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            node.accept(writer);
            return writer.toByteArray();
        }

        return basicClass;
    }

    public static MethodAction combine(NodeFilter filter, NodeAction action)
    {
        return (MethodNode mnode) -> applyOnNode(mnode, filter, action);
    }

    public static boolean applyOnNode(MethodNode method, NodeFilter filter, NodeAction action)
    {
        Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

        boolean applied = false;
        while (iterator.hasNext()) {
            AbstractInsnNode anode = iterator.next();

            if (filter.test(anode)) {
                log("Located target " + getNodeString(anode));
                applied = true;
                if (action.test(method, anode)) {
                    break;
                }
            }
        }
        return applied;
    }

    public static boolean findMethodAndTransform(ClassNode node, MethodSignature sig, MethodAction pred)
    {
        String funcName = sig.funcName;
        if (MesonLoadingPlugin.runtimeDeobfuscationEnabled) {
            funcName = sig.srgName;
        }

        for (MethodNode method : node.methods) {
            if ((method.name.equals(funcName) || method.name.equals(sig.obfName) || method.name.equals(sig.srgName)) && (method.desc.equals(sig.funcDesc) || method.desc.equals(sig.obfDesc))) {
                log("Located method, patching...");

                boolean finish = pred.test(method);
                log("Patch result: " + finish);

                return finish;
            }
        }

        log("Failed to locate the method!");
        return false;
    }

    public static boolean checkDesc(String desc, String expected)
    {
        return desc.equals(expected) || desc.equals(MethodSignature.obfuscate(expected));
    }

    public static String getNodeString(AbstractInsnNode node)
    {
        Printer printer = new Textifier();

        TraceMethodVisitor visitor = new TraceMethodVisitor(printer);
        node.accept(visitor);

        StringWriter sw = new StringWriter();
        printer.print(new PrintWriter(sw));
        printer.getText().clear();

        return sw.toString().replaceAll("\n", "").trim();
    }

    public static void log(String string)
    {
        System.out.println(String.format("[Meson ASM] %s", string));
    }

    public static boolean checkTransformers(Configuration config, String ...transformers)
    {
        boolean loaded = true;
        for (String transformer : transformers) {
            loaded = loaded && ConfigHelper.propBoolean(config, transformer, TRANSFORMERS, "", true);
        }

        config.setCategoryComment(TRANSFORMERS,
                "This section contains core class patches.\n" +
                        "You may disable these patches in case of compatibility problems.\n" +
                        "Any features that depend on a manually disabled patch will not be enabled."
        );

        if (config.hasChanged()) config.save();

        return loaded;
    }
}
