package org.vaadin.example.listener;

import com.vaadin.flow.shared.Registration;
import org.vaadin.example.model.PojektDetailsBcValue;

import java.util.LinkedList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Broadcaster um registrierte Listener zu benachrichtigen.
 * Es wird speziell für die Projektdetails verwendet.
 */
public class ProjektDetailsBroadcaster {
    static Executor executor = Executors.newSingleThreadExecutor();

    static LinkedList<Consumer<PojektDetailsBcValue>> listeners = new LinkedList<>();

    /**
     * Registriert einen Listener.
     * @param listener
     * @return
     */
    public static synchronized Registration register(
            Consumer<PojektDetailsBcValue> listener) {
        listeners.add(listener);

        return () -> {
            synchronized (EditorBroadcaster.class) {
                listeners.remove(listener);
            }
        };
    }

    /**
     * Benachrichtigt alle registrierten Listener.
     * @param values String-Array aus: ProjektOID, Username
     */
    public static synchronized void broadcast(PojektDetailsBcValue values) {
        for (Consumer<PojektDetailsBcValue> listener : listeners) {
            executor.execute(() -> listener.accept(values));
        }
    }
}