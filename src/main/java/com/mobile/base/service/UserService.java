package com.mobile.base.service;

import com.mobile.base.constant.BaseConstant;
import com.mobile.base.enumeration.ErrorCode;
import com.mobile.base.exception.BusinessException;
import com.mobile.base.exception.ResourceNotFoundException;
import com.mobile.base.form.user.CreateUserForm;
import com.mobile.base.mapper.UserMapper;
import com.mobile.base.model.entity.Account;
import com.mobile.base.model.entity.Group;
import com.mobile.base.model.entity.User;
import com.mobile.base.repository.AccountRepository;
import com.mobile.base.repository.GroupRepository;
import com.mobile.base.repository.UserRepository;
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
