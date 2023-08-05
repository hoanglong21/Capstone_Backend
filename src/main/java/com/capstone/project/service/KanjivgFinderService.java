package com.capstone.project.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

public interface KanjivgFinderService {
    public String getSvgFile(char kanji);
}
