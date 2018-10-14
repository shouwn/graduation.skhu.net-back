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
}