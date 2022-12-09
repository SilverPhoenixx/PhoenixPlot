package net.royalguardians.phoenixplot.api;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.UUID;

public class UUIDCatcher {

    public static UUID getUUID(String name) {
        try {
            URLConnection connection = new URL("https://playerdb.co/api/player/minecraft/" + name).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            connection.connect();
            BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONParser parser = new JSONParser();
            JSONObject myresponse = (JSONObject) parser.parse(jsonText);
            JSONObject data = (JSONObject) myresponse.get("data");
            JSONObject player = (JSONObject) data.get("player");
            UUID uuid = UUID.fromString((String) player.get("id"));
            return uuid;
        } catch (IOException | ParseException ex) {
            return null;
        }
    }

    private static String readAll(Reader rd) {
        try {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        } catch (IOException ex ){
            ex.printStackTrace();
            return null;
        }
    }
}