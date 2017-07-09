package io.github.voleye.intellij.magento2plugin.reference;

import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlTokenType;
import io.github.voleye.intellij.magento2plugin.reference.provider.CompositeReferenceProvider;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.XmlPatterns.*;

public class XmlReferenceContributor extends PsiReferenceContributor {

    /**
     * according to http://php.net/manual/en/language.oop5.basic.php
     */
    private static final String PHP_ELEMENT_REGEX = "\\\\?[A-Z_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]*(\\\\[A-Z_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]*)+(::[a-zA-Z_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]*)?";

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {

        // <someXmlTag someAttribute="Some\Php\ClassName" />
        registrar.registerReferenceProvider(
            XmlPatterns.xmlAttributeValue()
                .withValue(string().matches(PHP_ELEMENT_REGEX))
                .inFile(xmlFile().withName(string().endsWith(".xml"))),
            new CompositeReferenceProvider()
        );

        // <someXmlTag>Some\Php\ClassName</someXmlTag>
        registrar.registerReferenceProvider(
            XmlPatterns
                .psiElement(XmlTokenType.XML_DATA_CHARACTERS)
                .withText(string().matches(PHP_ELEMENT_REGEX))
                .inFile(xmlFile().withName(string().endsWith(".xml"))),
            new CompositeReferenceProvider()
        );
    }
}
