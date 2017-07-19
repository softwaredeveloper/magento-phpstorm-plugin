package io.github.voleye.intellij.magento2plugin;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.*;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter extends ResolvingConverter<XmlAttributeValue> implements CustomReferenceConverter {
    private static final String PHP_CLASS_NAME_REGEX = "[A-Z_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]*(\\\\[A-Z_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]*)*";

    @Nullable
    @Override
    public XmlAttributeValue fromString(@Nullable String s, ConvertContext context) {
//        PhpIndex phpIndex = PhpIndex.getInstance(context.getProject());
//        for (PhpClass phpClass:phpIndex.getAnyByFQN(s)) {
//            return phpClass;
//        };
        return null;
    }

    @Nullable
    @Override
    public String toString(@Nullable XmlAttributeValue o, ConvertContext context) {
        return o.getValue();
    }

    @NotNull
    @Override
    public Collection<? extends XmlAttributeValue> getVariants(ConvertContext context) {
        return null;
    }

    @Override
    public void handleElementRename(GenericDomValue<XmlAttributeValue> genericValue, ConvertContext context, String newElementName) {
        super.handleElementRename(genericValue, context, newElementName);
    }

    @Override
    public void bindReference(GenericDomValue<XmlAttributeValue> genericValue, ConvertContext context, PsiElement newTarget) {
        super.bindReference(genericValue, context, newTarget);
    }

    @Nullable
    @Override
    public PsiElement getPsiElement(@Nullable XmlAttributeValue resolvedValue) {
        return super.getPsiElement(resolvedValue);
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element, String stringValue, @Nullable XmlAttributeValue resolveResult, ConvertContext context) {
        return super.isReferenceTo(element, stringValue, resolveResult, context);
    }

    @Override
    public boolean canResolveTo(Class<? extends PsiElement> elementClass) {
        return super.canResolveTo(elementClass);
    }

    @NotNull
    @Override
    public PsiReference[] createReferences(GenericDomValue value, PsiElement element, ConvertContext context) {
        String origValue = element.getText();

        Pattern pattern = Pattern.compile(PHP_CLASS_NAME_REGEX);
        Matcher matcher = pattern.matcher(origValue);
        if (!matcher.find()) {
            return PsiReference.EMPTY_ARRAY;
        }

        String classFQN = matcher.group();
        String className = classFQN.substring(classFQN.lastIndexOf(92) + 1);

        PhpIndex phpIndex = PhpIndex.getInstance(element.getProject());

        Collection<PhpClass> phpClasses = phpIndex.getAnyByFQN(classFQN);
        if (phpClasses.size() > 0) {
            TextRange range = new TextRange(
                    origValue.lastIndexOf(className),
                    origValue.lastIndexOf(className) + className.length()
            );
            return new PsiReference[] {new PolyVariantReference(element, range, phpClasses)};
        }

        return new PsiReference[0];
    }
}
