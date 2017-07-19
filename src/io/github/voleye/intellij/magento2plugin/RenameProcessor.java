package io.github.voleye.intellij.magento2plugin;

import com.intellij.psi.PsiElement;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class RenameProcessor extends RenamePsiElementProcessor {
    RenameProcessor() {

    }

    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return (element instanceof PhpClass);
    }


    @Override
    public void prepareRenaming(PsiElement element, String newName, Map<PsiElement, String> allRenames) {
       //...
    }

}
