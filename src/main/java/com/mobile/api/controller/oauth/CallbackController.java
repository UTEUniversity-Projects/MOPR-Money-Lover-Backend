package com.mobile.api.controller.oauth;

import com.mobile.api.controller.base.BaseController;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Hidden
public class CallbackController extends BaseController {

    @GetMapping("/callback")
    public ResponseEntity<Map<String, Object>> handleCallback(
            @RequestParam(name = "code", required = false) String authorizationCode,
            @RequestParam(name = "state", required = false) String state) {

        Map<String, Object> response = new HashMap<>();
        if (authorizationCode == null) {
            response.put("success", false);
            response.put("message", "Authorization code is missing");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("success", true);
        response.put("authorization_code", authorizationCode);
        response.put("state", state);

        return ResponseEntity.ok(response);
    }
}
