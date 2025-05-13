package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.notification.NotificationDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.mapper.NotificationMapper;
import com.mobile.api.model.criteria.NotificationCriteria;
import com.mobile.api.model.entity.Notification;
import com.mobile.api.repository.jpa.NotificationRepository;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notification")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NotificationController extends BaseController {
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private NotificationMapper notificationMapper;

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<NotificationDto>> getNotificationList(
            @Valid @ModelAttribute NotificationCriteria notificationCriteria,
            Pageable pageable
    ) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "createdDate")
            );
        }

        notificationCriteria.setUserId(getCurrentUserId());
        Specification<Notification> specification = notificationCriteria.getSpecification();
        Page<Notification> page = notificationRepository.findAll(specification, pageable);

        PaginationDto<NotificationDto> responseDto = new PaginationDto<>(
                notificationMapper.fromEntitiesToNotificationDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List notifications successfully");
    }

    @PutMapping(value = "/client/mark-as-read/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> markNotificationAsRead(@PathVariable Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.NOTIFICATION_NOT_FOUND));
        notification.setIsRead(true);
        notificationRepository.save(notification);
        return ApiMessageUtils.success(null, "Mark notification as read successfully");
    }

    @PutMapping(value = "/client/mark-all-as-read", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiMessageDto<Void> markAllNotificationAsRead() {
        notificationRepository.markAllAsReadByUserId(getCurrentUserId());
        return ApiMessageUtils.success(null, "Mark notification as read successfully");
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> deleteNotification(@PathVariable Long id) {
        notificationRepository.deleteById(id);
        return ApiMessageUtils.success(null, "Delete notification successfully");
    }
}
