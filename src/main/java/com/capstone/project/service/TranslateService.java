package com.capstone.project.service;

import com.capstone.project.exception.ResourceNotFroundException;
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

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;

public interface TranslateService {

    public String translateClients5(String text, String to) throws Exception;

    public String translateGoogleapis(String text, String to) throws Exception;

    public String translateMymemory(String text, String to) throws Exception;
}