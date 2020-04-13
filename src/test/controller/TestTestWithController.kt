package test.controller

import org.junit.Test

class TestTestWithController {

    @Test
    fun testit(){
        TestWithController {assert(true)}.doit()
    }

}