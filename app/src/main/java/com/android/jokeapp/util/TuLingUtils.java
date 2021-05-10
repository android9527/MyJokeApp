package com.android.jokeapp.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class TuLingUtils {

    public static final String APIKEY = "215f991c74c93189913fc5177d5906c5";
    public static final String URL = "http://api.turingos.cn/turingos/api/v2";
    private static final int CONNECT_TIMEOUT = 5000;

    private static final int READ_TIMEOUT = 5000;

    private static final String ENCODING = "utf-8";

    private static final String UUID = java.util.UUID.randomUUID().toString();

    private static String buildBody(String input) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        //请求参数
        try {
            String data = "{\"content\": [{" +
                    "\"data\": \"" + input + "\"" +
                    "}], \"userInfo\": {\"uniqueId\" : \"" + UUID + "\"}}";

            JSONObject dataObject = new JSONObject(data);
            JSONObject jsonParam = new JSONObject();
            jsonParam.put("key", APIKEY);
            jsonParam.put("timestamp", timestamp);
            jsonParam.put("data", dataObject);
            return jsonParam.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param param
     * @return
     */
    public static String httpPost(String param) throws Exception {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            conn.connect();

            out = new OutputStreamWriter(conn.getOutputStream(), ENCODING);
            out.write(buildBody(param));
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), ENCODING));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toString();
    }
}
