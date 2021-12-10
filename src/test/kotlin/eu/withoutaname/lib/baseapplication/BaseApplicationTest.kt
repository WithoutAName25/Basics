package eu.withoutaname.lib.baseapplication

import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

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
    
    data class FakeEvent(val someRandomValue: Int) : Event
    class OtherEvent : Event
    
    open class FakeApplicationPart(val callback: (Int) -> Unit = {}) : ApplicationPart() {
        
        @EventHandler
        open fun onFakeEvent(event: FakeEvent) {
            callback(event.someRandomValue)
        }
    }
}
