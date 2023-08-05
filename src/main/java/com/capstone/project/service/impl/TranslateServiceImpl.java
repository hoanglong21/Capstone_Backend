package com.capstone.project.service.impl;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.service.TranslateService;
import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class TranslateServiceImpl implements TranslateService {
    public String translateClients5(String text, String to) throws Exception {
        String preUrl = "https://clients5.google.com/translate_a/single?dj=1&dt=t&dt=sp&dt=ld&dt=bd&client=dict-chrome-ex&sl=auto&tl=" + to +"&q=" +
                URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
        URL url = new URL(preUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JsonParser parser = new JsonParser();
            JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

            // Extract the "trans" value
            JsonArray sentences = jsonResponse.getAsJsonArray("sentences");
            if (sentences.size() > 0) {
                JsonObject firstSentence = sentences.get(0).getAsJsonObject();
                String translation = firstSentence.get("trans").getAsString();
                return translation;
            } else {
                throw new ResourceNotFroundException("No translation found.");
            }
        } else {
            throw new Exception("GET request failed with response code: " + responseCode);
        }
    }

    public String translateGoogleapis(String text, String to) throws Exception {
        String preUrl = "https://translate.googleapis.com/translate_a/single?dj=1&dt=t&dt=sp&dt=ld&dt=bd&client=dict-chrome-ex&sl=auto&tl=" + to +"&q=" +
                URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
        URL url = new URL(preUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JsonParser parser = new JsonParser();
            JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

            // Extract the "trans" value
            JsonArray sentences = jsonResponse.getAsJsonArray("sentences");
            if (sentences.size() > 0) {
                JsonObject firstSentence = sentences.get(0).getAsJsonObject();
                String translation = firstSentence.get("trans").getAsString();
                return translation;
            } else {
                throw new ResourceNotFroundException("No translation found.");
            }
        } else {
            throw new Exception("GET request failed with response code: " + responseCode);
        }
    }

    public String translateMymemory(String text, String to) throws Exception {
        String source = languageDetection(text.substring(0, Math.min(text.length(), 10)));
        String preUrl = "https://api.mymemory.translated.net/get?langpair=" + source + "|" + to +"&q=" +
                URLEncoder.encode(text, StandardCharsets.UTF_8.toString());
        URL url = new URL(preUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response
            JsonParser parser = new JsonParser();
            JsonObject jsonResponse = parser.parse(response.toString()).getAsJsonObject();

            // Extract the "trans" value
            JsonArray matches = jsonResponse.getAsJsonArray("matches");
            JsonObject responseData = jsonResponse.getAsJsonObject("responseData");
            StringBuilder longTranslate = new StringBuilder();
            if (responseData != null) {
                String translation = responseData.get("translatedText").getAsString() + " (match "+ responseData.get("match").getAsString() +")";
                longTranslate.append(translation);
            } if (matches.size() > 0) {
                for (int i=0; i<matches.size(); i++) {
                    JsonObject match = matches.get(i).getAsJsonObject();
                    String translation = match.get("translation").getAsString() + " (match "+ match.get("match").getAsString() +")";
                    if(!longTranslate.toString().contains(translation)) {
                        longTranslate.append("\n" + translation);
                    }
                }
            }

            if(!longTranslate.toString().equals("")){
                return longTranslate.toString();
            } else {
                throw new ResourceNotFroundException("No translation found.");
            }
        } else {
            throw new Exception("GET request failed with response code: " + responseCode);
        }
    }

    private String languageDetection(String source) {
        LanguageDetector detector = LanguageDetectorBuilder.fromAllLanguages().build();
        Language detectedLanguage = detector.detectLanguageOf(source);
        if(detectedLanguage.toString().equals("JAPANESE")) {
            return "ja";
        } else if(detectedLanguage.toString().equals("VIETNAMESE")) {
            return "vi";
        } else {
            return "en";
        }
    }
}
