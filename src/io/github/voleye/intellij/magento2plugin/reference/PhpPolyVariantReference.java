package io.github.voleye.intellij.magento2plugin.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PhpPolyVariantReference extends PsiPolyVariantReferenceBase<PsiElement> {

    /**
     * Target elements
     */
    private Collection<? extends PsiElement> targets;

    /**
     * Constructor
     *
     * @param element PsiElement
     */
    public PhpPolyVariantReference(PsiElement element, TextRange range, Collection<? extends PsiElement> targets) {
        super(element, range);
        this.targets = targets;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        List<ResolveResult> result = new ArrayList<>();
        for (PsiElement target : targets) {
            result.add(new PsiElementResolveResult(target));
        }
        return result.toArray(new ResolveResult[result.size()]);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
