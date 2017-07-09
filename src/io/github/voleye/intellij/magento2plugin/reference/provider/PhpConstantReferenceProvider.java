package io.github.voleye.intellij.magento2plugin.reference.provider;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.ProcessingContext;
import com.jetbrains.php.lang.psi.elements.Field;
import com.jetbrains.php.lang.psi.stubs.indexes.PhpClassConstantIndex;
import io.github.voleye.intellij.magento2plugin.reference.PhpPolyVariantReference;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PhpConstantReferenceProvider extends PsiReferenceProvider {

    /**
     * Php constant pattern in xml declaration
     */
    private static final String PHP_CONSTANT_REGEX = "::([a-zA-Z_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]*)";

    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {

        String origValue = element.getText();
        String fixedOrigValue = element.getText().replace("\"", "");

        Pattern pattern = Pattern.compile(PHP_CONSTANT_REGEX);
        Matcher matcher = pattern.matcher(origValue);
        if (!matcher.find()) {
            return PsiReference.EMPTY_ARRAY;
        }

        String constName = matcher.group(1);
        String constFQN = (fixedOrigValue.startsWith("\\") ? fixedOrigValue : "\\" + fixedOrigValue)
                .replace("::", ".");

        Collection<Field> constants = StubIndex.getElements(
                PhpClassConstantIndex.KEY,
                constName,
                element.getProject(),
                GlobalSearchScope.allScope(element.getProject()),
                Field.class
        );

        constants.removeIf(c -> !c.getFQN().equals(constFQN));
        if (constants.size() > 0) {
            TextRange range = new TextRange(
                    origValue.indexOf(constName),
                    origValue.indexOf(constName) + constName.length()
            );
            return new PsiReference[] {new PhpPolyVariantReference(element, range, constants)};
        }

        return PsiReference.EMPTY_ARRAY;
    }
}