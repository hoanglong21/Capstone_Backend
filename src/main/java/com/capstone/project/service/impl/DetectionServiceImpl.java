package com.capstone.project.service.impl;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.capstone.project.model.VocabularyTokenizer;
import com.capstone.project.service.DetectionService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DetectionServiceImpl implements DetectionService {
    public List<VocabularyTokenizer> detectVocabulary(String text) {
        // Create a tokenizer instance
        Tokenizer tokenizer = new Tokenizer.Builder().build();

        // Tokenize the text
        Iterable<Token> tokens = tokenizer.tokenize(text);

        // Iterate through the tokens and print the details
        List<VocabularyTokenizer> response = new ArrayList<>();
        for (Token token : tokens) {
            VocabularyTokenizer vocabularyTokenizer = new VocabularyTokenizer();
            vocabularyTokenizer.setWord(token.getSurface());

            List<String> partOfSpeech = new ArrayList<>();
            partOfSpeech.add(token.getPartOfSpeechLevel1());
            partOfSpeech.add(token.getPartOfSpeechLevel2());
            partOfSpeech.add(token.getPartOfSpeechLevel3());
            partOfSpeech.add(token.getPartOfSpeechLevel4());
            partOfSpeech.removeAll(Collections.singleton("*"));
            vocabularyTokenizer.setPartOfSpeech(partOfSpeech);

            vocabularyTokenizer.setDictionaryForm(token.getBaseForm());

            response.add(vocabularyTokenizer);
        }
        return response;
    }

    public String detectGrammar(String text, String to) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String url = "https://api.openai.com/v1/chat/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + dotenv.get("OPENAI_API_KEY"));

        JSONObject data = new JSONObject();
        data.put("model", "gpt-3.5-turbo");
//        data.put("max_tokens", 100);
        data.put("temperature", 1.0);

        // Create a new JSONArray for messages
        JSONArray messages = new JSONArray();

        // Create a new JSONObject for the user message
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", "Please perform a grammar check on the following Japanese input, " +
                "translate the following Japanese text into English and provide a detailed analysis of the grammar used:\n" +
                "Japanese Text: [" + text + "])");

        // Add the user message to the messages array
        messages.put(userMessage);
        // Add the messages array to the data object
        data.put("messages", messages);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();
        String resultInEnglish = new JSONObject(output).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        if(!to.toLowerCase().equals("english")) {
            return ConvertDetectGrammar(resultInEnglish);
        } else {
            return resultInEnglish;
        }
    }

    private String ConvertDetectGrammar(String text) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String url = "https://api.openai.com/v1/chat/completions";
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Bearer " + dotenv.get("OPENAI_API_KEY"));

        JSONObject data = new JSONObject();
        data.put("model", "gpt-3.5-turbo");
//        data.put("max_tokens", 100);
        data.put("temperature", 1.0);

        // Create a new JSONArray for messages
        JSONArray messages = new JSONArray();

        // Create a new JSONObject for the user message
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", " \"" + text + "\"\n translate everything into Vietnamese except Japanese text no more other words");

        // Add the user message to the messages array
        messages.put(userMessage);
        // Add the messages array to the data object
        data.put("messages", messages);

        con.setDoOutput(true);
        con.getOutputStream().write(data.toString().getBytes());

        String output = new BufferedReader(new InputStreamReader(con.getInputStream())).lines()
                .reduce((a, b) -> a + b).get();

        return new JSONObject(output).getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
    }

    public String grammarCheck(String text) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String preUrl = "https://api.sapling.ai/api/v1/edits";
        URL url = new URL(preUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        connection.setDoOutput(true); // Enable output for POST request
        connection.setRequestProperty("Content-Type", "application/json");

        String requestBody = "{\n" +
                "    \"key\":\"" + dotenv.get("SAPLING_API_KEY") + "\", \n" +
                "    \"text\":\"" + text + "\", \n" +
                "    \"lang\": \"jp\",\n" +
                "    \"session_id\": \"test session\"\n" +
                "}";
        //TODO EACH MONTH CHANGE SAPLING_API_KEY AND MAKE IT RUN WITH MULTIPLE KEY (5)
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.getBytes());
        outputStream.flush();
        outputStream.close();

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
            JsonArray edits = jsonResponse.getAsJsonArray("edits");

            StringBuilder result = new StringBuilder();

            for (int i=0; i<edits.size(); i++) {
                JsonObject edit = edits.get(i).getAsJsonObject();
                int start = Integer.parseInt(edit.get("start").getAsString());
                int end = Integer.parseInt(edit.get("end").getAsString());
                String replacement = edit.get("replacement").getAsString();
                String sentence = edit.get("sentence").getAsString();
                String outcome = sentence.substring(0, start) + replacement + sentence.substring(end);
                if(result.toString().equals("")) {
                    result.append(outcome);
                } else {
                    result.append(outcome + "\n");
                }
            }

            if(!result.toString().equals("")){
                return result.toString();
            } else {
                return "No grammar error";
            }
        } else {
            throw new Exception("GET request failed with response code: " + responseCode);
        }
    }
}
