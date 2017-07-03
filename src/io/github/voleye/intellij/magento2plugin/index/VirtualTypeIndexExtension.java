package io.github.voleye.intellij.magento2plugin.index;

import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.*;
import com.intellij.util.indexing.*;
import com.intellij.util.io.DataExternalizer;
import com.intellij.util.io.EnumeratorStringDescriptor;
import com.intellij.util.io.KeyDescriptor;
import com.intellij.util.xml.impl.DomApplicationComponent;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class VirtualTypeIndexExtension extends FileBasedIndexExtension<String, Void> {

    public static final ID<String, Void> KEY = ID.create(
            "io.github.voleye.intellij.magento2plugin.xml.di.virtual.type.name");
    private final KeyDescriptor<String> myKeyDescriptor = new EnumeratorStringDescriptor();

    @NotNull
    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return new DataIndexer<String, Void, FileContent>() {
            @NotNull
            public Map<String, Void> map(@NotNull FileContent inputData) {
                Map<String, Void> map = new THashMap();
                PsiFile psiFile = inputData.getPsiFile();

                if (inputData.getFile().getName().equals("di.xml") && psiFile instanceof XmlFile) {
                    XmlDocument xmlDocument = ((XmlFile) psiFile).getDocument();
                    if (xmlDocument != null) {
                        XmlTag xmlRootTag = xmlDocument.getRootTag();
                        if (xmlRootTag != null) {
                            for (XmlTag virtualTypeTag : xmlRootTag.findSubTags("virtualType")) {
                                XmlAttribute xmlAttribute = virtualTypeTag.getAttribute("name");
                                if (xmlAttribute != null) {
                                    XmlAttributeValue valueElement = xmlAttribute.getValueElement();
                                    if (valueElement != null) {
                                        map.put(valueElement.getValue(), null);
                                    }
                                }
                            }
                        }
                    }
                }
                return map;
            }
        };
    }

    @NotNull
    @Override
    public ID<String, Void> getName() {
        return KEY;
    }

    @NotNull
    @Override
    public KeyDescriptor<String> getKeyDescriptor() {
        return this.myKeyDescriptor;
    }

    @NotNull
    public DataExternalizer<Void> getValueExternalizer() {
        return ScalarIndexExtension.VOID_DATA_EXTERNALIZER;
    }

    @NotNull
    @Override
    public FileBasedIndex.InputFilter getInputFilter() {
        return file ->
                file.getFileType() == XmlFileType.INSTANCE;
    }

    @Override
    public boolean dependsOnFileContent() {
        return true;
    }

    @Override
    public int getVersion() {
        return DomApplicationComponent.getInstance().getCumulativeVersion(false);
    }
}
