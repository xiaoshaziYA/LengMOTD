package org.leng;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class OneLine {
    private final LengMOTD plugin;

    public OneLine(LengMOTD plugin) {
        this.plugin = plugin;
    }

    public String getOneLine() {
        try {
            URL url = new URL("https://v1.hitokoto.cn/?encode=text");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                StringBuilder response = new StringBuilder();
                while (scanner.hasNext()) {
                    response.append(scanner.nextLine());
                }
                scanner.close();
                return response.toString();
            }
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to fetch one-line from hitokoto.cn: " + e.getMessage());
        }
        return null;
    }
}