package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Setting;
import com.capstone.project.model.UserSetting;
import com.capstone.project.service.UserSettingService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class UserSettingController {

    private final UserSettingService userSettingService;

    public UserSettingController(UserSettingService userSettingService) {
        this.userSettingService = userSettingService;
    }

    @GetMapping("usersettings")
    public ResponseEntity<?> getAllUserSetting() {
        return ResponseEntity.ok(userSettingService.getAllUserSetting());
    }

    @GetMapping("/usersettings/{id}")
    public ResponseEntity<?> getUserSettingById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(userSettingService.getUserSettingById(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usersettingsbyuserid/{id}")
    public ResponseEntity<?> getUserSettingByUserId(@PathVariable int id) {
        return ResponseEntity.ok(userSettingService.getUserSettingByUserId(id));
    }

    @GetMapping("/usersettingsbysettingid/{id}")
    public ResponseEntity<?> getUserSettingBySettingId(@PathVariable int id) {
        return ResponseEntity.ok(userSettingService.getUserSettingBySettingId(id));
    }

    @PostMapping("usersettings")
    public ResponseEntity<?> createUserSetting(@RequestBody UserSetting usersetting) {
        try {
            return ResponseEntity.ok(userSettingService.createUserSetting(usersetting));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/usersettingss/{id}")
    public ResponseEntity<?> updateUserSetting(@RequestBody UserSetting usersetting, @PathVariable int id) {
        try {
            return ResponseEntity.ok(userSettingService.updateUserSetting(usersetting, id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/usersettings/{id}")
    public ResponseEntity<?> deleteUserSetting(@PathVariable int id) {
        try {
            return ResponseEntity.ok(userSettingService.deleteUserSetting(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/customsettings/{id}")
    public ResponseEntity<?> customSettings(@PathVariable int id) {
        try {
            return ResponseEntity.ok(userSettingService.CustomGetUserSettingByUserId(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/customsettings")
    public ResponseEntity<?> updateCustomSettings(@RequestParam("userid") int userId,
                                                  @RequestParam("settingid") int settingId,
                                                  @RequestParam(value = "value", required = false) String value) {
        try {
            userSettingService.saveUserSettingCustom(userId, settingId, value);
            return ResponseEntity.ok("Update successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/studyremindmail")
    public ResponseEntity<?> sendReminderMail() {
        try {
            userSettingService.sendStudyReminderMails();
            return ResponseEntity.ok("Successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
