package com.capstone.project.controller;

import com.capstone.project.model.Grammar;
import com.capstone.project.model.Kanji;
import com.capstone.project.model.Vocabulary;
import com.capstone.project.service.KanjivgFinderService;
import com.capstone.project.startup.ApplicationStartup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class DictionaryController {

    @Autowired
    private KanjivgFinderService kanjivgFinder;


    @Autowired
    private ApplicationStartup applicationStartup;

    @GetMapping("/radical/{number}")
    public String getRadical(@PathVariable int number) {
        char kanjiCharacter = (char) (0x2F00 + number - 1);
        return String.valueOf(kanjiCharacter);
    }

    @GetMapping("/kanjivg/{char}")
    public String getKanjivg(@PathVariable("char") char character) {
        return kanjivgFinder.getSvgFile(character);
    }

    @GetMapping("/kanji")
    public ResponseEntity<?> getKanji(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "3") int size,
                                   @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(applicationStartup.getKanjiService().searchAndPaginate(search, page, size));
    }

    @GetMapping("/vocabulary")
    public ResponseEntity<?> getVocabulary( @RequestParam(defaultValue = "1") int page,
                                           @RequestParam(defaultValue = "3") int size,
                                           @RequestParam(defaultValue = "") String search) {
        return ResponseEntity.ok(applicationStartup.getVocabularyService().searchAndPaginate(search, page, size));
    }

        @GetMapping("/grammar")
    public ResponseEntity<?> getGrammar(@RequestParam(defaultValue = "1") int page,
                                    @RequestParam(defaultValue = "3") int size,
                                    @RequestParam(defaultValue = "") String search,
                                    @RequestParam(defaultValue = "0") int level) {
        return ResponseEntity.ok(applicationStartup.getGrammarService().searchAndPaginate(search, level, page, size));
    }
}