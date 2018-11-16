package modelmapperTest

import org.junit.jupiter.api.Test
import org.modelmapper.Conditions
import org.modelmapper.ModelMapper

class ModelmapperTest{

    private val modelmapper = ModelMapper()
            .apply {
                configuration.propertyCondition = Conditions.isNotNull()
            }

    @Test
    fun mapTest(){
        val testBox1 =  TestBox1(1, 2, 3)
        val testBox = TestBox(3, null, 4)
        println(testBox1.test4)
        modelmapper.map(testBox, testBox1)
        println(testBox1)
        println(testBox1.test4)
    }
}

data class TestBox (
        var test1: Long?,
        var test2: Long?,
        var test4: Long?
)

data class TestBox1 (
        var test1: Long,
        var test2: Long,
        var test3: Long
): TestAbstract()

abstract class TestAbstract{
    var test4: Long? = 5
}