package eu.withoutaname.lib.baseapplication

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import java.lang.IllegalStateException

internal class BaseApplicationTest : WithAssertions {
    
    private val baseApplication = BaseApplication()
    
    @Test
    fun registerPart() {
        val fakeModule = FakeApplicationPart()
        baseApplication.registerPart(fakeModule)
        assertThat(baseApplication.applicationParts)
            .hasSize(1)
            .contains(fakeModule)
    }
    
    @Test
    fun fireEvent() {
        var callbackCounter = 0
        val fakeApplicationPart = FakeApplicationPart {
            assertThat(it).isEqualTo(42)
            callbackCounter++
        }
        baseApplication.registerPart(fakeApplicationPart)
    
        baseApplication.fireEvent(FakeEvent(42))
        baseApplication.fireEvent(OtherEvent())
        
        assertThat(callbackCounter).isEqualTo(1)
    }
}

data class FakeEvent(val someRandomValue: Int) : Event
class OtherEvent : Event
annotation class OtherAnnotation

class FakeApplicationPart(val callback: (Int) -> Unit = {}) : ApplicationPart() {
    
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
