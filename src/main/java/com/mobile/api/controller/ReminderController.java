package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.reminder.ReminderDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.reminder.CreateReminderForm;
import com.mobile.api.form.reminder.UpdateReminderForm;
import com.mobile.api.mapper.ReminderMapper;
import com.mobile.api.model.criteria.ReminderCriteria;
import com.mobile.api.model.entity.Reminder;
import com.mobile.api.model.entity.User;
import com.mobile.api.repository.jpa.ReminderRepository;
import com.mobile.api.repository.jpa.UserRepository;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reminder")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReminderController extends BaseController {
    @Autowired
    private ReminderRepository reminderRepository;
    @Autowired
    private ReminderMapper reminderMapper;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<ReminderDto>> getReminderList(
            @Valid @ModelAttribute ReminderCriteria reminderCriteria,
            Pageable pageable
    ) {
        reminderCriteria.setUserId(getCurrentUserId());
        Specification<Reminder> specification = reminderCriteria.getSpecification();
        Page<Reminder> page = reminderRepository.findAll(specification, pageable);

        PaginationDto<ReminderDto> responseDto = new PaginationDto<>(
                reminderMapper.fromEntitiesToReminderDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List reminders successfully");
    }

    @GetMapping(value = "/client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ReminderDto> getReminder(@PathVariable Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REMINDER_NOT_FOUND));

        return ApiMessageUtils.success(reminderMapper.fromEntityToReminderDto(reminder), "Get reminder successfully");
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> createReminder(@Valid @RequestBody CreateReminderForm createReminderForm) {
        Reminder reminder = reminderMapper.fromCreateReminderFormToEntity(createReminderForm);

        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        reminder.setUser(user);

        reminderRepository.save(reminder);
        return ApiMessageUtils.success(null, "Create reminder successfully");
    }

    @PutMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateReminder(@Valid @RequestBody UpdateReminderForm updateReminderForm) {
        Reminder reminder = reminderRepository.findById(updateReminderForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REMINDER_NOT_FOUND));

        reminderMapper.updateFromUpdateReminderFormToEntity(reminder, updateReminderForm);

        reminderRepository.save(reminder);
        return ApiMessageUtils.success(null, "Update reminder successfully");
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> deleteReminder(@PathVariable Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.REMINDER_NOT_FOUND));

        reminderRepository.delete(reminder);
        return ApiMessageUtils.success(null, "Delete reminder successfully");
    }
}
