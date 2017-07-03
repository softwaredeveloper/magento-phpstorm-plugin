package io.github.voleye.intellij.magento2plugin.index;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.*;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class VirtualTypeIndex {

    /**
     * Index instance
     */
    private static VirtualTypeIndex instance;

    /**
     * Project instance
     */
    private Project project;


    @NotNull
    public static VirtualTypeIndex getInstance(@NotNull Project project) {
        if (instance == null) {
            instance = new VirtualTypeIndex();
        }
        instance.project = project;
        return instance;
    }

    /**
     * Get all PsiElements of virtual type name
     *
     * @param name Name of virtual type
     * @return collection of PsiElements for virtual type name
     */
    public Collection<XmlAttributeValue> getNames(String name) {
        Collection<XmlAttributeValue> result = new ArrayList<>();

        // find virtual type name
        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(VirtualTypeIndexExtension.KEY, name,
                        GlobalSearchScope.allScope(project));

        for (VirtualFile virtualFile : virtualFiles) {
            XmlFile xmlFile = (XmlFile) PsiManager.getInstance(project).findFile(virtualFile);
            XmlDocument xmlDocument = ((XmlFile) xmlFile).getDocument();
            if (xmlDocument != null) {
                XmlTag xmlRootTag = xmlDocument.getRootTag();
                if (xmlRootTag != null) {
                    for (XmlTag virtualTypeTag : xmlRootTag.findSubTags("virtualType")) {
                        XmlAttribute xmlAttribute = virtualTypeTag.getAttribute("name");
                        if (xmlAttribute != null) {
                            XmlAttributeValue valueElement = xmlAttribute.getValueElement();
                            if (valueElement != null && valueElement.getValue().equals(name)) {
                                result.add(valueElement);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
