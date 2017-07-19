package io.github.voleye.intellij.magento2plugin;

import com.intellij.psi.impl.source.xml.XmlAttributeValueImpl;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.util.xml.Attribute;
import com.intellij.util.xml.Convert;
import com.jetbrains.php.lang.psi.elements.PhpClass;

interface Service extends com.intellij.util.xml.DomElement {

    @Attribute("some-class")
    @Convert(MyConverter.class)
    MyGenericAttributeValue<PhpClass> getMyAttributeValue();


}
