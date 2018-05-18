package logging;

import java.io.PrintStream;

public class Logger {
    private static Boolean isEnabled = false;
    private static PrintStream out = null;

    private Logger() {
    }

    public static boolean getStatus() {
        return isEnabled;
    }

    public static void setStatus(Boolean status) {
        isEnabled = status;
    }

    public static void setStream(PrintStream stream) {
        out = stream;
    }

    public static void logging(String log) {
        if (!isEnabled || out == null)
            return;
        out.println(log + "\n");
    }
}
