package by.bseu.pp.lab05;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/** Готовая настройка: FINE и более важные события видны в окне Run. */
public final class LoggingSetup {
    private LoggingSetup() { }

    public static void configure() {
        Logger logger = Logger.getLogger(OrderService.class.getName());
        logger.setUseParentHandlers(false);
        for (Handler old : logger.getHandlers()) {
            logger.removeHandler(old);
            old.close();
        }
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.FINE);
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getLevel() + " " + formatMessage(record) + System.lineSeparator();
            }
        });
        logger.addHandler(handler);
        logger.setLevel(Level.FINE);
    }
}
