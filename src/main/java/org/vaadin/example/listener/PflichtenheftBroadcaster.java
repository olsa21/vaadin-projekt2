package org.vaadin.example.listener;

import com.vaadin.flow.shared.Registration;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Broadcaster um registrierte Listener zu benachrichtigen.
 * Es wird speziell für die Anzeige offener Pflichtenhefter verwendet.
 */
public class PflichtenheftBroadcaster {
    static Executor executor = Executors.newSingleThreadExecutor();

    static LinkedList<Consumer<String>> listeners = new LinkedList<>();

    /**
     * Registriert einen Listener.
     * @param listener
     * @return
     */
    public static synchronized Registration register(
            Consumer<String> listener) {
        listeners.add(listener);

        return () -> {
            synchronized (PflichtenheftBroadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    /**
     * Benachrichtigt alle registrierten Listener.
     * Da es nur eine Aufforderung zum Aktualisieren ist, wird kein Parameter übergeben.
     */
    public static synchronized void broadcast() {
        for (Consumer<String> listener : listeners) {
            executor.execute(() -> listener.accept(""));
        }
    }
}