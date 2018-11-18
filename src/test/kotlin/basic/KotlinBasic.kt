package basic

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class KotlinBasic{

    @Test
    fun isNullOrBlankTest() {
        val s1 = arrayOf("    ", null, "")

        Assertions.assertTrue(s1[0].isNullOrBlank())
        Assertions.assertTrue(s1[1].isNullOrBlank())
        Assertions.assertTrue(s1[2].isNullOrBlank())
    }

    @Test
    fun localDateTime(){
        println(LocalDateTime.now())
    }

    @Test
    fun regexTest(){
        val regex1 = "교양계.*".toRegex()
        val regex2 = "[^(교양계)].*".toRegex()

        Assertions.assertTrue(regex1.matches("교양계 교양필수"))
        Assertions.assertTrue(regex2.matches("공학계 소프트웨어공학과"))

        Assertions.assertFalse(regex2.matches("교양계 교양필수"))
        Assertions.assertFalse(regex1.matches("공학계 소프트웨어공학과"))

    }

    @Test
    fun alsoVsApply(){
        println("TEST").apply {
            println("let!!!!")
        }

        println("TEST").also {
            println("also!!!!")
        }
    }

    @Test
    fun copyTest(){
        data class Test (
                val a: Int,
                val b: Int
        ){
            var c: Int? = null
        }

        println(Test(10, 20).apply { c = 100 }.copy(a = 20).c)
    }

    @Test
    fun nullSafe(){
        println(TestBox().text?.let { "TEST" })
    }

    private inner class TestBox{
        var text: String? = null
    }
}