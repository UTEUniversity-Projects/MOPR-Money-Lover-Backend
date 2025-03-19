package com.mobile.api.controller.oauth;

import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.form.user.CreateUserForm;
import com.mobile.api.service.UserService;
import com.mobile.api.security.ClientRegistrationService;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RegistrationController {
    @Autowired
    private UserService userService;
    @Autowired
    private ClientRegistrationService clientRegistrationService;
    
    @PostMapping("/register")
    @Transactional
    public ApiMessageDto<Object> registerUser(
            @Valid @RequestBody CreateUserForm createUserForm,
            BindingResult bindingResult
    ) {
        userService.createUser(createUserForm);
        clientRegistrationService.registerClientForUser(createUserForm.getUsername(), createUserForm.getPassword());

        return ApiMessageUtils.success(null, "Registered successfully");
    }
}
