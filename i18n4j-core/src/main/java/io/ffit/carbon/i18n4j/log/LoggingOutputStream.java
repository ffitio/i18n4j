package io.ffit.carbon.i18n4j.log;

import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * LoggingOutputStream
 *
 * @author Lay
 * @date 2022/9/20
 */
public class LoggingOutputStream extends OutputStream {
    public static void redirectSysOutAndSysErr(Logger logger) {
        System.setOut(new PrintStream(new LoggingOutputStream(logger, LogLevel.INFO)));
        System.setErr(new PrintStream(new LoggingOutputStream(logger, LogLevel.ERROR)));
    }

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
    private final Logger logger;
    private final LogLevel level;

    public enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR,
    }

    public LoggingOutputStream(Logger logger, LogLevel level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void write(int b) {
        if (b == '\n') {
            String line = baos.toString();
            baos.reset();

            switch (level) {
                case TRACE:
                    logger.trace(line);
                    break;
                case DEBUG:
                    logger.debug(line);
                    break;
                case ERROR:
                    logger.error(line);
                    break;
                case INFO:
                    logger.info(line);
                    break;
                case WARN:
                    logger.warn(line);
                    break;
            }
        } else {
            baos.write(b);
        }
    }
}
