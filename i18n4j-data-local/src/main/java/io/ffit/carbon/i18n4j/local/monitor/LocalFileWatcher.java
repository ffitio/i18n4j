package io.ffit.carbon.i18n4j.local.monitor;

import com.sun.nio.file.SensitivityWatchEventModifier;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Local File Watcher
 *
 * @author Lay
 * @date 2022/9/7
 */
public class LocalFileWatcher implements Runnable {
    private final AtomicBoolean running;
    private final Thread t;
    private final WatchService watchService;

    private final Map<String, FileChangedListener> listeners;

    private final Map<Path, WatchKey> paths;

    public LocalFileWatcher() {
        running = new AtomicBoolean(false);
        t = new Thread(this);
        listeners = new ConcurrentHashMap<>();
        paths = new ConcurrentHashMap<>();
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    running.set(false);
                    watchService.close();
                } catch (IOException ignored) {}
            }));
        } catch (IOException e) {
            throw new RuntimeException("local index monitor start failed:", e);
        }
    }

    public void register(Path path) {
        try {
            WatchKey key = path.register(watchService, new WatchEvent.Kind[] {StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY}, SensitivityWatchEventModifier.HIGH);
            paths.put(path, key);
        } catch (IOException ignored) {}
    }

    public void unregister(Path path) {
        Optional.ofNullable(paths.get(path)).ifPresent(WatchKey::cancel);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void addListener(String filename, FileChangedListener listener) {
        listeners.put(filename, listener);
    }

    public void removeListener(String filename) {
        listeners.remove(filename);
    }

    public Set<Path> paths() {
        return paths.keySet();
    }

    @Override
    public void run() {
        WatchKey key;
        while (running.get()) {
            try {
                key = watchService.take();
                key.pollEvents().forEach(e -> {
                    String filename = e.context().toString();
                    FileChangedListener listener = listeners.get(filename);
                    if (listener != null) {
                        listener.onModified(filename);
                    }
                });
                key.reset();
            } catch (Exception ignored) {}
        }
    }

    public void start() {
        if (running.compareAndSet(false, true)) {
            t.start();
        }
    }

    public void stop() {
        running.compareAndSet(true, false);
    }

    public interface FileChangedListener {
        /**
         * on file modified
         * @param filename
         */
        void onModified(String filename);
    }
}
