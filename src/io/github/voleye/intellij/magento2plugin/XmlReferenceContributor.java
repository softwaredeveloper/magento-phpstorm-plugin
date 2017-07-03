package io.github.voleye.intellij.magento2plugin;

import com.intellij.patterns.XmlPatterns;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlTokenType;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.XmlPatterns.*;

public class XmlReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {

        // <someXmlTag someAttribute="Some\Php\ClassName" />
        registrar.registerReferenceProvider(
            XmlPatterns.xmlAttributeValue()
                .withValue(string().matches("(\\\\?[A-Za-z]+\\\\[A-Za-z]+)+"))
                .inFile(xmlFile().withName(string().endsWith(".xml")))
                .andNot(
                    XmlPatterns.xmlAttributeValue()
                        .withParent(
                            XmlPatterns.xmlAttribute().withLocalName("name")
                                .withParent(
                                    XmlPatterns.xmlTag().withLocalName("virtualType")
                                )
                        )
                ),
            new PsiReferenceProvider() {
                @NotNull
                @Override
                public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                             @NotNull ProcessingContext context) {

                    return new PsiReference[] {
                        new XmlPolyVariantReference(element, ((XmlAttributeValue) element).getValue()),
                    };
                }
            }
        );

        // <someXmlTag>Some\Php\ClassName</someXmlTag>
        registrar.registerReferenceProvider(
            XmlPatterns
                .psiElement(XmlTokenType.XML_DATA_CHARACTERS)
                .withText(string().matches("(\\\\?[A-Za-z]+\\\\[A-Za-z]+)+"))
                .inFile(xmlFile().withName(string().endsWith(".xml"))),
            new PsiReferenceProvider() {
                @NotNull
                @Override
                public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                             @NotNull ProcessingContext context) {
                    return new PsiReference[]{new XmlPolyVariantReference(element, element.getText())};
                }
            }
        );
    }
}
