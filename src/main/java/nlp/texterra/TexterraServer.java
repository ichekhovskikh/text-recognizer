package nlp.texterra;

import java.io.IOException;

public class TexterraServer {
    private static Boolean isStarted = false;

    public static void start() {
        if (isStarted)
            return;

        final String command = "cmd /c start cmd.exe /K texterra-russian\\bin\\texterra server start";
        try {
            Process proc = Runtime.getRuntime().exec(command);
            proc.waitFor();
            proc.destroy();
            isStarted = true;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            isStarted = false;
        }
    }

    public static Boolean getStatus() {
        return isStarted;
    }
}
