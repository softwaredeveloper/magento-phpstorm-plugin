package io.github.voleye.intellij.magento2plugin;

import com.intellij.psi.*;
import com.intellij.psi.xml.*;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.PhpClass;

import io.github.voleye.intellij.magento2plugin.index.VirtualTypeIndex;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class XmlPolyVariantReference extends PsiPolyVariantReferenceBase<PsiElement> {

    /**
     * Matched value
     */
    private String classFQN;

    /**
     * Constructor
     *
     * @param element PsiElement
     */
    XmlPolyVariantReference(PsiElement element, String value) {
        super(element);
        this.classFQN = value;
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode) {
        List<ResolveResult> results = new ArrayList<>();

        PhpIndex phpIndex = PhpIndex.getInstance(getElement().getProject());
        VirtualTypeIndex virtualTypeIndex = VirtualTypeIndex.getInstance(getElement().getProject());

        classFQN = classFQN.startsWith("\\") ? classFQN.substring(1) : classFQN;

        // file php class
        for (PhpClass phpClass : phpIndex.getClassesByFQN(classFQN)) {
            results.add(new PsiElementResolveResult(phpClass));
        }

        // find php interface
        for (PhpClass phpClass : phpIndex.getInterfacesByFQN(classFQN)) {
            results.add(new PsiElementResolveResult(phpClass));
        }

        // find virtual type name
        for (XmlAttributeValue virtualTypeName : virtualTypeIndex.getNames(classFQN)) {
            results.add(new PsiElementResolveResult(virtualTypeName));
        }

        return results.toArray(new ResolveResult[results.size()]);
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }
}
