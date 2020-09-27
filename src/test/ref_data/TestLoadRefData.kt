package test.ref_data

import gamelogic.territory.mapobjects.StructureType
import org.junit.Test

class TestLoadRefData {

    @Test
    fun testStructureType(){
        StructureType.loadStructureTypes()
    }

}