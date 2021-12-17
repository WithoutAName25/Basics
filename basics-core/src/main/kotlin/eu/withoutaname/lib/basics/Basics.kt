package eu.withoutaname.lib.basics

import java.util.function.Consumer
import kotlin.reflect.KClass
import kotlin.reflect.full.allSuperclasses

class Basics {
    
    val listeners = mutableListOf<Any>()
    private val listener = mutableMapOf<KClass<*>, MutableList<Consumer<Event>>>()
    
    fun registerListener(listener: Any) {
        listeners.add(listener)
        val listenerClass = listener::class
        listenerClass.members.forEach {
            val params = it.parameters
            if (params.size == 2) {
                val classifier = params[1].type.classifier
                if (classifier is KClass<*> && classifier.allSuperclasses.contains(Event::class)) {
                    for (annotation in it.annotations) {
                        if (annotation is EventHandler) {
                            this.listener.getOrPut(classifier, ::mutableListOf).add { event ->
                                it.call(listener, event)
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