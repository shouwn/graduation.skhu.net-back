//package com.shouwn.graduation.utils.processor
//
//import com.google.auto.service.AutoService
//import com.shouwn.graduation.utils.annotation.Specifications
//import java.io.IOException
//import java.util.*
//import javax.annotation.processing.*
//import javax.lang.model.element.Element
//import javax.lang.model.element.ElementKind
//import javax.lang.model.element.TypeElement
//import javax.lang.model.util.Elements
//import javax.lang.model.util.Types
//import javax.tools.Diagnostic
//
//@AutoService(Processor::class)
//class SpecificationsProcessor : AbstractProcessor() {
//    private lateinit var typeUtils: Types
//    private lateinit var elementUtils: Elements
//    private lateinit var filer: Filer
//    private lateinit var messager: Messager
//    private val classList = ArrayList<SpecificationsAnnotatedClass>()
//
//    @Synchronized
//    override fun init(processingEnv: ProcessingEnvironment) {
//        super.init(processingEnv)
//        typeUtils = processingEnv.typeUtils
//        elementUtils = processingEnv.elementUtils
//        filer = processingEnv.filer
//        messager = processingEnv.messager
//    }
//
//    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
//
//        for (annotatedElement in roundEnv.getElementsAnnotatedWith(Specifications::class.java)) {
//
//            if (annotatedElement.kind != ElementKind.CLASS)
//                return this.error(annotatedElement,
//                        "Only classes can be annotated with @%s",
//                        Specifications::class.java.simpleName)
//
//            val typeElement = annotatedElement as TypeElement
//
//            classList.add(SpecificationsAnnotatedClass(typeElement))
//        }
//
//        classList.forEach { clazz ->
//            try {
//                clazz.generateCode(this.filer)
//            } catch (ignored: IOException) { }
//        }
//
//        return true
//    }
//
//    private fun error(e: Element, msg: String, vararg args: Any): Boolean {
//        messager.printMessage(
//                Diagnostic.Kind.ERROR,
//                String.format(msg, *args),
//                e
//        )
//
//        return true
//    }
//
//}