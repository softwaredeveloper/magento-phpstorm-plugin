package io.github.voleye.intellij.magento2plugin.reference.provider;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeReferenceProvider extends PsiReferenceProvider {

    private List<PsiReferenceProvider> providers = new ArrayList<>();

    public CompositeReferenceProvider() {
        providers.add(new PhpClassReferenceProvider());
        providers.add(new PhpConstantReferenceProvider());
    }

    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
        List<PsiReference> result = new ArrayList<>();

        for (PsiReferenceProvider provider : providers()) {
            result.addAll(Arrays.asList(provider.getReferencesByElement(element, context)));
        }

        return result.toArray(new PsiReference[result.size()]);
    }

    private List<PsiReferenceProvider> providers() {
        return providers;
    }
}
