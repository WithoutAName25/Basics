package eu.withoutaname.lib.basics

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

internal class BasicsTest : WithAssertions {
    
    private val basics = Basics()
    
    @Test
    fun registerPart() {
        val fakeModule = FakeBasicsPart()
        basics.registerPart(fakeModule)
        assertThat(basics.basicsParts)
            .hasSize(1)
            .contains(fakeModule)
    }
    
    @Test
    fun fireEvent() {
        var callbackCounter = 0
        val fakeApplicationPart = FakeBasicsPart {
            assertThat(it).isEqualTo(42)
            callbackCounter++
        }
        basics.registerPart(fakeApplicationPart)
    
        basics.fireEvent(FakeEvent(42))
        basics.fireEvent(OtherEvent())
        
        assertThat(callbackCounter).isEqualTo(1)
    }
}

data class FakeEvent(val someRandomValue: Int) : Event
class OtherEvent : Event
annotation class OtherAnnotation

class FakeBasicsPart(val callback: (Int) -> Unit = {}) : BasicsPart() {
    
    @OtherAnnotation
    @EventHandler
    fun onFakeEvent(event: FakeEvent) {
        callback(event.someRandomValue)
    }
    
    @OtherAnnotation
    @Suppress("unused", "unused_parameter")
    fun notAnEventHandler(event: FakeEvent) {
        throw IllegalStateException("This method should not be called because it is not annotated with @EventHandler!")
    }
    
    @EventHandler
    fun notAValidEventHandler(notAnEvent: Int) {
        throw IllegalStateException("This method should not be called because it has no event parameter!")
    }
}
