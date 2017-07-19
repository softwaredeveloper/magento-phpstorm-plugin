package io.github.voleye.intellij.magento2plugin;

import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomFileDescription;

public class DomDescription<T extends DomElement> extends DomFileDescription<RootElement> {

    public DomDescription() {
        super(RootElement.class, "config");
    }

}
