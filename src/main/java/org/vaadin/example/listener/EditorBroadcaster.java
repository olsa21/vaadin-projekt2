package org.vaadin.example.listener;

import com.vaadin.flow.shared.Registration;
import org.vaadin.example.model.EditorBcValue;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Broadcaster um registrierte Listener zu benachrichtigen.
 * Es wird speziell für die Editor-Funktion verwendet.
 */
public class EditorBroadcaster {
    static Executor executor = Executors.newSingleThreadExecutor();

    static LinkedList<Consumer<EditorBcValue>> listeners = new LinkedList<>();

    /**
     * Registriert einen Listener.
     * @param listener
     * @return
     */
    public static synchronized Registration register(
            Consumer<EditorBcValue> listener) {
        listeners.add(listener);

        return () -> {
            synchronized (EditorBroadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    /**
     * Benachrichtigt alle registrierten Listener.
     * @param values String-Array aus: ProjektOID, currentchapter, Username, componentOID
     */
    public static synchronized void broadcast(EditorBcValue values) {
        for (Consumer<EditorBcValue> listener : listeners) {
            executor.execute(() -> listener.accept(values));
        }
    }
}