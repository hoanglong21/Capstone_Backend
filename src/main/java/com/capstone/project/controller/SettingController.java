package com.capstone.project.controller;

import com.capstone.project.exception.ResourceNotFroundException;
import com.capstone.project.model.Answer;
import com.capstone.project.model.Setting;
import com.capstone.project.service.SettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/")
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping("settings")
    public ResponseEntity<?> getAllSetting(){
        return ResponseEntity.ok(settingService.getAllSetting());
    }

    @GetMapping("/settings/{id}")
    public ResponseEntity<?> getSettingById(@PathVariable int id){
        try {
            return ResponseEntity.ok(settingService.getSettingById(id));
        }catch(ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("settings")
    public ResponseEntity<?> createSetting(@RequestBody Setting setting){
        try{
            return ResponseEntity.ok(settingService.createSetting(setting));
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/settingss/{id}")
    public ResponseEntity<?> updateSetting(@RequestBody Setting setting,@PathVariable int id){
        try {
            return ResponseEntity.ok(settingService.updateSetting(setting,id));
        }catch (ResourceNotFroundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/settings/{id}")
    public ResponseEntity<?> deleteSetting(@PathVariable int id) {
        try {
            return ResponseEntity.ok(settingService.deleteSetting(id));
        } catch (ResourceNotFroundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
