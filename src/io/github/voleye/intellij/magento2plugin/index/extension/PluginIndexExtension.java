package io.github.voleye.intellij.magento2plugin.index.extension;

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

public class PluginIndexExtension extends FileBasedIndexExtension<String, Void> {

    public static final ID<String, Void> KEY = ID.create(
            "io.github.voleye.intellij.magento2plugin.xml.di.plugin");
    private final KeyDescriptor<String> myKeyDescriptor = new EnumeratorStringDescriptor();

    @NotNull
    @Override
    public DataIndexer<String, Void, FileContent> getIndexer() {
        return inputData ->  {
            Map<String, Void> map = new THashMap<>();
            PsiFile psiFile = inputData.getPsiFile();

            if (inputData.getFile().getName().equals("di.xml") && psiFile instanceof XmlFile) {
                XmlDocument xmlDocument = ((XmlFile) psiFile).getDocument();
                if (xmlDocument != null) {
                    XmlTag xmlRootTag = xmlDocument.getRootTag();
                    if (xmlRootTag != null) {
                        for (XmlTag typeTag : xmlRootTag.findSubTags("type")) {
                            if (typeTag.findSubTags("plugin").length > 0) {
                                String typeName = typeTag.getAttributeValue("name");
                                if (typeName != null && !typeName.isEmpty()) {
                                    map.put(typeName, null);
                                }
                            }
                        }
                    }
                }
            }
            return map;
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
