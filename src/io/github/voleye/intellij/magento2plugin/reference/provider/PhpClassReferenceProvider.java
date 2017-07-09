package io.github.voleye.intellij.magento2plugin.reference.provider;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import io.github.voleye.intellij.magento2plugin.reference.PhpPolyVariantReference;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PhpClassReferenceProvider extends PsiReferenceProvider {

    /**
     * Php class name pattern (magento specific)
     */
    private static final String PHP_CLASS_NAME_REGEX = "[A-Z_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]*(\\\\[A-Z_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]*)*";

    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {

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
                    origValue.lastIndexOf(classFQN),
                    origValue.lastIndexOf(classFQN) + classFQN.length()
            );
            return new PsiReference[] {new PhpPolyVariantReference(element, range, phpClasses)};
        }

        return new PsiReference[0];
    }
}
