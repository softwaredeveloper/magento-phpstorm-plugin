package io.github.voleye.intellij.magento2plugin.index;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.*;
import com.intellij.util.indexing.FileBasedIndex;
import io.github.voleye.intellij.magento2plugin.index.extension.PluginIndexExtension;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class PluginIndex {

    /**
     * Index instance
     */
    private static PluginIndex instance;

    /**
     * Project instance
     */
    private Project project;


    @NotNull
    public static PluginIndex getInstance(@NotNull Project project) {
        if (instance == null) {
            instance = new PluginIndex();
        }
        instance.project = project;
        return instance;
    }

    /**
     * Get PsiElements of plugin types
     *
     * @param name Name of type which has plugins
     * @return Collection of plugin types psi elements
     */
    public Collection<XmlAttributeValue> getElements(String name) {
        Collection<XmlAttributeValue> result = new ArrayList<>();

        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(PluginIndexExtension.KEY, name,
                        GlobalSearchScope.allScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            XmlFile xmlFile = (XmlFile) PsiManager.getInstance(project).findFile(virtualFile);
            if (xmlFile != null) {
                XmlDocument xmlDocument = xmlFile.getDocument();
                if (xmlDocument != null) {
                    XmlTag xmlRootTag = xmlDocument.getRootTag();
                    if (xmlRootTag != null) {
                        for (XmlTag typeTag : xmlRootTag.findSubTags("type")) {
                            String typTagName = typeTag.getAttributeValue("name");
                            if (typTagName != null && typTagName.equals(name)) {
                                for (XmlTag pluginTag : typeTag.findSubTags("plugin")) {
                                    XmlAttribute pluginTypeAttribute = pluginTag.getAttribute("type");
                                    if (pluginTypeAttribute != null && pluginTypeAttribute.getValueElement() != null) {
                                        result.add(pluginTypeAttribute.getValueElement());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }
}
