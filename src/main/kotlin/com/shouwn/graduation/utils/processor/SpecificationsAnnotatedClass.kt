//package com.shouwn.graduation.utils.processor
//
//import com.google.common.base.Preconditions
//import com.shouwn.graduation.utils.annotation.Detail
//import com.squareup.javapoet.FieldSpec
//import com.squareup.kotlinpoet.FunSpec
//import com.squareup.kotlinpoet.KModifier
//import com.squareup.kotlinpoet.TypeSpec
//import lombok.Data
//import java.io.IOException
//import java.util.ArrayList
//import java.util.stream.Collectors
//import javax.annotation.processing.Filer
//import javax.lang.model.element.ElementKind
//import javax.lang.model.element.Modifier
//import javax.lang.model.element.TypeElement
//import javax.lang.model.element.VariableElement
//
//class SpecificationsAnnotatedClass
//constructor(annotatedClassElement: TypeElement) {
//    private val simpleTypeName: String = annotatedClassElement.simpleName.toString()
//    private val packageName: String
//    private val annotatedVariables = ArrayList<DetailAnnotatedVariable>()
//
//    companion object {
//
//        private val DETAIL_NAME: String = Detail::class.java.canonicalName
//        private const val SUFFIX = "Specifications"
//        private const val PACKAGE_NAME = "specifications"
//    }
//
//    init {
//
//        this.packageName = annotatedClassElement.qualifiedName.toString()
//                .replace(simpleTypeName, PACKAGE_NAME)
//
//        for (element in annotatedClassElement.enclosedElements) {
//
//            for (m in element.annotationMirrors) {
//                val annotationName = (m.annotationType.asElement() as TypeElement).qualifiedName
//
//                if (DETAIL_NAME.contentEquals(annotationName)) {
//                    Preconditions.checkArgument(element.kind == ElementKind.FIELD)
//
//                    val variableElement = element as VariableElement
//
//                    annotatedVariables.add(DetailAnnotatedVariable(variableElement))
//                }
//            }
//        }
//    }
//
//    fun typeSpec(): TypeSpec = TypeSpec.classBuilder(this.simpleTypeName + SUFFIX)
//            .addModifiers(KModifier.DATA)
//            .primaryConstructor(FunSpec.constructorBuilder()
//                    .addParameter())
//
//                .addFields(annotatedVariables.stream()
//                        .map<FieldSpec>(Function<DetailAnnotatedVariable, FieldSpec> { it.fieldSpec() })
//                        .collect<List<FieldSpec>, Any>(Collectors.toList<FieldSpec>()))
//                .addModifiers(Modifier.PUBLIC)
//                .build()
//}