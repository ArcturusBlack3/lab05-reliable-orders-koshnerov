package by.bseu.pp.lab05;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/** Готовый перехват журнала для тестов; восстанавливает настройки после проверки. */
final class LogCapture implements AutoCloseable {
    private final Logger logger = Logger.getLogger(OrderService.class.getName());
    private final Level previousLevel = logger.getLevel();
    private final boolean previousParents = logger.getUseParentHandlers();
    private final List<LogRecord> records = new ArrayList<>();
    private final Handler handler = new Handler() {
        @Override public void publish(LogRecord record) { records.add(record); }
        @Override public void flush() { }
        @Override public void close() { }
    };

    LogCapture() {
        handler.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        logger.addHandler(handler);
    }

    List<LogRecord> records() { return records; }
    void clear() { records.clear(); }

    @Override
    public void close() {
        logger.removeHandler(handler);
        logger.setUseParentHandlers(previousParents);
        logger.setLevel(previousLevel);
    }
}
