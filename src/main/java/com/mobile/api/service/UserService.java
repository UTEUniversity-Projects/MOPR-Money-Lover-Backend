package com.mobile.api.service;

import com.mobile.api.constant.BaseConstant;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.user.CreateUserForm;
import com.mobile.api.mapper.UserMapper;
import com.mobile.api.model.entity.Account;
import com.mobile.api.model.entity.Group;
import com.mobile.api.model.entity.User;
import com.mobile.api.repository.AccountRepository;
import com.mobile.api.repository.GroupRepository;
import com.mobile.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public void createUser(CreateUserForm createUserForm) {
        // Create ACCOUNT
        Optional<Account> existingUser = accountRepository.findByUsernameOrEmail(
                createUserForm.getUsername(), createUserForm.getEmail()
        );
        if (existingUser.isPresent()) {
            if (existingUser.get().getUsername().equals(createUserForm.getUsername())) {
                throw new BusinessException(ErrorCode.ACCOUNT_USERNAME_EXISTED);
            }
            if (existingUser.get().getEmail().equals(createUserForm.getEmail())) {
                throw new BusinessException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
            }
        }

        Account account = new Account();
        account.setUsername(createUserForm.getUsername());
        account.setPassword(passwordEncoder.encode(createUserForm.getPassword()));
        account.setEmail(createUserForm.getEmail());
        account.setPhone(createUserForm.getPhone());
        account.setAvatarPath(createUserForm.getAvatarPath());

        Group group = groupRepository.findFirstByKind(BaseConstant.GROUP_KIND_USER)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.GROUP_NOT_FOUND));
        account.setGroup(group);
        Account savedAccount = accountRepository.save(account);

        // Create USER
        User user = userMapper.fromCreateUserForm(createUserForm);
        user.setAccount(savedAccount);
        userRepository.save(user);
    }
}
