package org.astropeci.skillbot;

import lombok.SneakyThrows;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

/**
 * A fault-tolerant asynchronous operation, usually involving a 3rd party API.
 */
public interface Task<T> {

    /**
     * Returns the value obtained by the request, or a value default if there was an error.
     * Blocks if it is not available.
     */
    T waitFor();

    /**
     * Adds a consumer to be executed if an error occurs processing the request.
     *
     * @return this instance
     */
    Task<T> onError(Consumer<Throwable> onError);

    /**
     * Adds an error handler via {@link #onError(Consumer)} that sends an error log.
     *
     * @return this instance
     */
    default Task<T> logOnError(Logger log, String message, Object... params) {
        return onError(e -> {
            Object[] extendedParams = new Object[params.length + 1];
            System.arraycopy(params, 0, extendedParams, 0, params.length);
            extendedParams[extendedParams.length - 1] = e;

            log.warn(message, extendedParams);
        });
    }

    /**
     * Constructs a task based on a future and a default value.
     */
    static <T> Task<T> of(CompletableFuture<T> future, T defaultValue) {
        return new Task<>() {

            @Override
            @SneakyThrows({ InterruptedException.class })
            public T waitFor() {
                try {
                    return future.get();
                } catch (ExecutionException e) {
                    return defaultValue;
                }
            }

            @Override
            public Task<T> onError(Consumer<Throwable> onError) {
                future.exceptionally(e -> {
                    onError.accept(e);
                    return null;
                });

                return this;
            }
        };
    }

    /**
     * Constructs a task which has already completed successfully with a value.
     */
    static <T> Task<T> done(T value) {
        return of(CompletableFuture.completedFuture(value), value);
    }
}
