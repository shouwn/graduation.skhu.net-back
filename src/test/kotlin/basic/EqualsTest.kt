package basic

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class EqualsTest{

    @Test
    fun equalsTest(){
        Assertions.assertEquals(hashSetOf("1", "2"), hashSetOf("1", "2"))
    }

    @Test
    fun enumEqualsTest(){
        Assertions.assertEquals(TestEnum.TEST, TestEnum.TEST)
    }

    @Test
    fun dataClazzEqualsTest(){
        data class Test(val a: Int)

        Assertions.assertEquals(Test(1), Test(1))
    }
}

enum class TestEnum{
    TEST;
}