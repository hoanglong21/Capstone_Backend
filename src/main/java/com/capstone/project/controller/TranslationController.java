package com.capstone.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class TranslationController {

    @Autowired
    private MessageSource messageSource;

    @PostMapping("/translations")
    public Map<String, String> getTranslations(@RequestHeader("lang") String lang) {
        Locale locale = new Locale(lang);
        Map<String, String> translations = new HashMap<>();
        translations.put("home", messageSource.getMessage("home", null, locale));
        translations.put("dictionary", messageSource.getMessage("dictionary", null, locale));
        translations.put("translate", messageSource.getMessage("translate", null, locale));
        translations.put("library", messageSource.getMessage("library", null, locale));
        translations.put("discovery", messageSource.getMessage("discovery", null, locale));
        translations.put("studyset", messageSource.getMessage("studyset", null, locale));
        translations.put("classroom", messageSource.getMessage("classroom", null, locale));
        translations.put("joinclass", messageSource.getMessage("joinclass", null, locale));
        translations.put("vocabulary", messageSource.getMessage("vocabulary", null, locale));
        translations.put("kanji", messageSource.getMessage("kanji", null, locale));
        translations.put("grammar", messageSource.getMessage("grammar", null, locale));
        translations.put("profile", messageSource.getMessage("profile", null, locale));
        translations.put("setting", messageSource.getMessage("setting", null, locale));
        translations.put("helpcenter", messageSource.getMessage("helpcenter", null, locale));
        translations.put("logout", messageSource.getMessage("logout", null, locale));
        translations.put("login", messageSource.getMessage("login", null, locale));
        translations.put("signup", messageSource.getMessage("signup", null, locale));
        translations.put("welcomback", messageSource.getMessage("welcomeback", null, locale));
        translations.put("logincontinue", messageSource.getMessage("logincontinue", null, locale));
        translations.put("username", messageSource.getMessage("username", null, locale));
        translations.put("typeuser", messageSource.getMessage("typeuser", null, locale));
        translations.put("password", messageSource.getMessage("password", null, locale));
        translations.put("typepass", messageSource.getMessage("typepass", null, locale));
        translations.put("forgot", messageSource.getMessage("forgot", null, locale));
        translations.put("newuser", messageSource.getMessage("newuser", null, locale));
        translations.put("forgothelp", messageSource.getMessage("forgothelp", null, locale));
        translations.put("useremail", messageSource.getMessage("useremail", null, locale));
        translations.put("submit", messageSource.getMessage("submit", null, locale));
        translations.put("getstart", messageSource.getMessage("getstart", null, locale));
        translations.put("createacc", messageSource.getMessage("createacc", null, locale));
        translations.put("typeyourusername", messageSource.getMessage("typeyourusername", null, locale));
        translations.put("firstname", messageSource.getMessage("firstname", null, locale));
        translations.put("typefirstname", messageSource.getMessage("typefirstname", null, locale));
        translations.put("lastname", messageSource.getMessage("lastname", null, locale));
        translations.put("typelastname", messageSource.getMessage("typelastname", null, locale));
        translations.put("email", messageSource.getMessage("email", null, locale));
        translations.put("typeemail", messageSource.getMessage("typeemail", null, locale));
        translations.put("enterpass", messageSource.getMessage("enterpass", null, locale));
        translations.put("role", messageSource.getMessage("role", null, locale));
        translations.put("leaner_role", messageSource.getMessage("leaner_role", null, locale));
        translations.put("tutor_role", messageSource.getMessage("tutor_role", null, locale));
        translations.put("register", messageSource.getMessage("register", null, locale));
        translations.put("haveaccount", messageSource.getMessage("haveaccount", null, locale));
        translations.put("whatisnihongo", messageSource.getMessage("whatisnihongo", null, locale));
        translations.put("define_nlu", messageSource.getMessage("define_nlu", null, locale));
        translations.put("nludictionary", messageSource.getMessage("nludictionary", null, locale));
        translations.put("finddef", messageSource.getMessage("finddef", null, locale));
        translations.put("search", messageSource.getMessage("search", null, locale));
        translations.put("translates", messageSource.getMessage("translates", null, locale));
        translations.put("grammarcheck", messageSource.getMessage("grammarcheck", null, locale));
        translations.put("analysis", messageSource.getMessage("analysis", null, locale));
        translations.put("openai", messageSource.getMessage("openai", null, locale));
        translations.put("all", messageSource.getMessage("all", null, locale));
        translations.put("studysets", messageSource.getMessage("studysets", null, locale));
        translations.put("classes", messageSource.getMessage("classes", null, locale));
        translations.put("users", messageSource.getMessage("users", null, locale));
        translations.put("viewall", messageSource.getMessage("viewall", null, locale));
        translations.put("noset", messageSource.getMessage("noset", null, locale));
        translations.put("nosetnote", messageSource.getMessage("nosetnote", null, locale));
        translations.put("createset", messageSource.getMessage("createset", null, locale));
        translations.put("classcode", messageSource.getMessage("classcode", null, locale));
        translations.put("join", messageSource.getMessage("join", null, locale));
        //.....
        return translations;
    }
}
