package com.capstone.project.service.impl;

import com.capstone.project.service.KanjivgFinderService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class KanjivgFinderServiceImpl implements KanjivgFinderService {
    public String getSvgFile(char kanji) {
        int kcode = String.valueOf(kanji).codePointAt(0);
        String hex = Integer.toHexString(kcode);
        int zeros = 5 - hex.length();
        hex = "0".repeat(zeros) + hex; // Get Unicode code point as hexadecimal string
        String fileName = "kanji/" + hex + "-animated.svg";

        // Get the classloader for the current thread
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        // Use the classloader to get an input stream to the resource
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        if (inputStream == null) {
            return "SVG file not found.";
        }

        try {
            String svgData = new String(inputStream.readAllBytes());
            return svgData;
        } catch (IOException e) {
            return "Error reading SVG file: " + e.getMessage();
        }
    }
}
