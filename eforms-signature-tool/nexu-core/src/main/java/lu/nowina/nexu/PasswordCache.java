package lu.nowina.nexu;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class PasswordCache {

    Logger logger = LogManager.getLogger(PasswordCache.class);

    private final static int MAX_TIME_IN_MILLS = 10000;
    private final static int TIME_DELAY_IN_MILLS = 1000;
    private final ConcurrentMap<String, char[]> passwordHolder;

    private static PasswordCache instance;

    private PasswordCache() {
        this.passwordHolder = new ConcurrentHashMap<>();
    }

    public static Optional<char[]> getPassword(String key) {
        createInstanceIfNotExist();
        return Optional.ofNullable(instance.getPasswordHolder().get(key));
    }

    public static void storePassword(String key, char[] password) {
        createInstanceIfNotExist();
        instance.addPassword(key, password);
        instance.scheduleClearOperation(key);
    }

    private static void createInstanceIfNotExist() {
        if (instance == null) {
            instance = new PasswordCache();
        }
    }

    private ConcurrentMap<String, char[]> getPasswordHolder () {
        return this.passwordHolder;
    }

    private void addPassword(String key, char[] password) {
        this.passwordHolder.put(key, password);
    }

    private void scheduleClearOperation(String key) {
        CompletableFuture.runAsync(() -> {
            long startTime = System.currentTimeMillis();
            while(true) {
                long currentTime = System.currentTimeMillis();
                if (currentTime > (startTime + MAX_TIME_IN_MILLS)) {
                    synchronized (passwordHolder) {
                        passwordHolder.remove(key);
                    }

                    return;
                }

                try {
                    Thread.sleep(TIME_DELAY_IN_MILLS);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
        });
    }
}
