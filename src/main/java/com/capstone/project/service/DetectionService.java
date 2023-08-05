package com.capstone.project.service;


import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.VocabularyTokenizer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONObject;
import org.languagetool.language.Japanese;
import org.languagetool.language.identifier.LanguageIdentifier;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.languagetool.JLanguageTool;
import org.languagetool.Language;
import org.languagetool.rules.RuleMatch;
import org.languagetool.language.identifier.LanguageIdentifier;

public interface DetectionService {

    public List<VocabularyTokenizer> detectVocabulary(String text);

    public String detectGrammar(String text, String to) throws Exception;

    public String grammarCheck(String text) throws Exception;

}
