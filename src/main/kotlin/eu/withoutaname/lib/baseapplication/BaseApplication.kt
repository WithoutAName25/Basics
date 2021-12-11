package eu.withoutaname.lib.baseapplication

import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

class BaseApplication {
    
    val applicationParts = mutableListOf<ApplicationPart>()
    private val listener = mutableMapOf<KClass<*>, MutableList<Consumer<Event>>>()
    
    fun registerPart(applicationPart: ApplicationPart) {
        applicationParts.add(applicationPart)
        val moduleClass = applicationPart::class
        moduleClass.members.forEach {
            val params = it.parameters
            if (params.size == 2) {
                val classifier = params[1].type.classifier
                if (classifier is KClass<*> && classifier.allSuperclasses.contains(Event::class)) {
                    for (annotation in it.annotations) {
                        if (annotation is EventHandler) {
                            listener.getOrPut(classifier, ::mutableListOf).add { event ->
                                it.call(applicationPart, event)
                            }
                            break
                        }
                    }
                }
            }
        }
    }
    
    fun fireEvent(event: Event) {
        listener.getOrDefault(event::class, null)?.forEach {
            it.accept(event)
        }
    }
}