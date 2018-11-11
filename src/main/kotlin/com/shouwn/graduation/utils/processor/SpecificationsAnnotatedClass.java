//package com.shouwn.graduation.utils.processor;
//
//import com.google.common.base.Preconditions;
//import com.squareup.javapoet.JavaFile;
//import com.squareup.javapoet.TypeSpec;
//import lombok.Data;
//
//import javax.annotation.processing.Filer;
//import javax.lang.model.element.*;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class SpecificationsAnnotatedClass {
//
//    private static final String DETAIL_NAME = Detail.class.getCanonicalName();
//    private static final String SUFFIX = "Specifications";
//    private static final String PACKAGE_NAME = "specifications";
//
//    private TypeElement annotatedClassElement;
//    private String simpleTypeName;
//    private String packageName;
//    private List<DetailAnnotatedVariable> annotatedVariables = new ArrayList<>();
//
//    public SpecificationsAnnotatedClass(TypeElement classElement) throws IllegalArgumentException{
//        this.annotatedClassElement = classElement;
//        this.simpleTypeName = classElement.getSimpleName().toString();
//
//        this.packageName = classElement.getQualifiedName().toString()
//                .replace(simpleTypeName, PACKAGE_NAME);
//
//        for(Element element : classElement.getEnclosedElements()){
//
//            for(AnnotationMirror m : element.getAnnotationMirrors()){
//                Name annotationName = ((TypeElement) m.getAnnotationType().asElement()).getQualifiedName();
//
//                if(DETAIL_NAME.contentEquals(annotationName)) {
//                    Preconditions.checkArgument(element.getKind() == ElementKind.FIELD);
//
//                    VariableElement variableElement = (VariableElement) element;
//
//                    annotatedVariables.add(new DetailAnnotatedVariable(variableElement));
//                }
//            }
//        }
//    }
//
//    public TypeSpec typeSpec(){
//
//        return TypeSpec.classBuilder(this.simpleTypeName + SUFFIX)
//                .addAnnotation(Data.class)
//                .addFields(annotatedVariables.stream()
//                        .map(DetailAnnotatedVariable::fieldSpec)
//                        .collect(Collectors.toList()))
//                .addModifiers(Modifier.PUBLIC)
//                .build();
//    }
//
//    public void generateCode(Filer filer) throws IOException{
//        JavaFile.builder(this.packageName, this.typeSpec())
//                .build()
//                .writeTo(filer);
//    }
//}
