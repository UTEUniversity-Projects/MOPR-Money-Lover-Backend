package com.mobile.base.controller.oauth;

import com.mobile.base.dto.ApiMessageDto;
import com.mobile.base.form.user.CreateUserForm;
import com.mobile.base.service.UserService;
import com.mobile.base.security.ClientRegistrationService;
import com.mobile.base.utils.ApiMessageUtils;
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
