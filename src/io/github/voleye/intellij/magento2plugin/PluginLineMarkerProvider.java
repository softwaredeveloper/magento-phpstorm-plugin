package io.github.voleye.intellij.magento2plugin;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.ide.util.DefaultPsiElementCellRenderer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.xml.XmlAttributeValue;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import io.github.voleye.intellij.magento2plugin.index.PluginIndex;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.List;

public class PluginLineMarkerProvider implements LineMarkerProvider {

    @Nullable
    @Override
    public LineMarkerInfo getLineMarkerInfo(@NotNull PsiElement psiElement) {
        return null;
    }

    @Override
    public void collectSlowLineMarkers(@NotNull List<PsiElement> psiElements, @NotNull Collection<LineMarkerInfo> result) {
        for (PsiElement element : psiElements) {
            if (element instanceof PhpClass) {
                Collection<XmlAttributeValue> targets = PluginIndex.getInstance(element.getProject()).getElements(
                        ((PhpClass) element).getFQN().substring(1));
                if (targets.size() > 0) {
                    result.add(
                        NavigationGutterIconBuilder.create(AllIcons.Nodes.Plugin)
                            .setTargets(targets)
                            .setTooltipText("Navigate to plugins declaration")
                            .setCellRenderer(new DefaultPsiElementCellRenderer(){
                                protected Icon getIcon(final PsiElement element) {
                                    return AllIcons.FileTypes.Xml;
                                }
                            })
                            .createLineMarkerInfo(element)
                    );
                }
            }
        }
    }
}
