//package com.shouwn.graduation.utils.processor;
//
//import com.google.common.base.CaseFormat;
//import com.google.common.base.Preconditions;
//import com.google.common.base.Strings;
//import com.squareup.javapoet.ClassName;
//import com.squareup.javapoet.FieldSpec;
//import com.squareup.javapoet.ParameterizedTypeName;
//import com.squareup.javapoet.TypeName;
//
//import javax.lang.model.element.Modifier;
//import javax.lang.model.element.VariableElement;
//import javax.lang.model.type.TypeMirror;
//
//public class DetailAnnotatedVariable {
//
//    private VariableElement annotatedVariableElement;
//    private String simpleVariableName;
//    private TypeName type;
//
//    public DetailAnnotatedVariable(VariableElement variableElement)
//            throws IllegalArgumentException {
//
//        this.annotatedVariableElement = variableElement;
//        Detail annotation = variableElement.getAnnotation(Detail.class);
//        String annotationType = annotation.type();
//
//        Preconditions.checkArgument(!Strings.isNullOrEmpty(annotationType));
//
//        TypeMirror typeMirror = variableElement.asType();
//
//        this.simpleVariableName = variableElement.getSimpleName().toString();
//
//        switch (annotationType){
//            case Detail.IDS:
//                ClassName list = ClassName.get("java.util", "List");
//                this.type = ParameterizedTypeName.get(list, ClassName.get(Long.class));
//                this.simpleVariableName = "IdsOf" +
//                        CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, this.simpleVariableName);
//                break;
//            case Detail.ID:
//                this.type = TypeName.get(Long.class);
//                this.simpleVariableName = "IdOf" +
//                        CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, this.simpleVariableName);
//                break;
//            case Detail.ROW:
//                this.type = TypeName.get(variableElement.asType());
//                break;
//        }
//    }
//
//    public FieldSpec fieldSpec(){
//        return FieldSpec.builder(type, simpleVariableName)
//                .addModifiers(Modifier.PRIVATE)
//                .build();
//    }
//}
