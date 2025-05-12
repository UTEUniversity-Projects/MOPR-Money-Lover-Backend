package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.event.EventDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.BusinessException;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.event.CreateEventForm;
import com.mobile.api.form.event.UpdateEventCompletedForm;
import com.mobile.api.form.event.UpdateEventForm;
import com.mobile.api.mapper.EventMapper;
import com.mobile.api.model.criteria.EventCriteria;
import com.mobile.api.model.entity.Event;
import com.mobile.api.model.entity.File;
import com.mobile.api.model.entity.User;
import com.mobile.api.model.entity.Wallet;
import com.mobile.api.repository.jpa.*;
import com.mobile.api.utils.ApiMessageUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/event")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EventController extends BaseController {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private FileRepository fileRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private EventMapper eventMapper;

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<EventDto>> getEventList(
            @Valid @ModelAttribute EventCriteria eventCriteria,
            Pageable pageable
    ) {
        eventCriteria.setUserId(getCurrentUserId());
        Specification<Event> specification = eventCriteria.getSpecification();
        Page<Event> page = eventRepository.findAll(specification, pageable);

        PaginationDto<EventDto> responseDto = new PaginationDto<>(
                eventMapper.fromEntitiesToEventDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List events successfully");
    }

    @GetMapping(value = "/client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<EventDto> getEvent(@PathVariable Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EVENT_NOT_FOUND));

        return ApiMessageUtils.success(eventMapper.fromEntityToEventDto(event), "Get event successfully");
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> createEvent (@Valid @RequestBody CreateEventForm createEventForm) {
        Event event = eventMapper.fromCreateEventFormToEntity(createEventForm);

        if (createEventForm.getStartDate().isAfter(createEventForm.getEndDate())) {
            throw new BusinessException(ErrorCode.EVENT_INVALID_DATE_RANGE);
        }
        event.setStartDate(createEventForm.getStartDate());
        event.setEndDate(createEventForm.getEndDate());

        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        event.setUser(user);

        Wallet wallet = walletRepository.findById(createEventForm.getWalletId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WALLET_NOT_FOUND));
        event.setWallet(wallet);

        File icon = fileRepository.findById(createEventForm.getIconId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
        event.setIcon(icon);

        eventRepository.save(event);
        return ApiMessageUtils.success(null, "Create event successfully");
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateEvent (@Valid @RequestBody UpdateEventForm updateEventForm) {
        Event event = eventRepository.findById(updateEventForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EVENT_NOT_FOUND));

        if (!Objects.equals(event.getStartDate(), updateEventForm.getStartDate())
        || !Objects.equals(event.getEndDate(), updateEventForm.getEndDate())) {
            if (updateEventForm.getStartDate().isAfter(updateEventForm.getEndDate())) {
                throw new BusinessException(ErrorCode.EVENT_INVALID_DATE_RANGE);
            }
            event.setStartDate(updateEventForm.getStartDate());
            event.setEndDate(updateEventForm.getEndDate());
        }

        File icon = fileRepository.findById(updateEventForm.getIconId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.FILE_NOT_FOUND));
        event.setIcon(icon);

        eventMapper.updateFromUpdateEventForm(event, updateEventForm);
        eventRepository.save(event);
        return ApiMessageUtils.success(null, "Create event successfully");
    }

    @PutMapping(value = "/client/update-completed", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateEventCompleted(
            @Valid @RequestBody UpdateEventCompletedForm updateEventCompletedForm
    ) {
        int result = eventRepository.updateIsCompletedById(
                updateEventCompletedForm.getId(),
                updateEventCompletedForm.getIsCompleted()
        );
        if (result == 0) {
            throw new ResourceNotFoundException(ErrorCode.EVENT_NOT_FOUND);
        }

        return ApiMessageUtils.success(null, "Update event completed successfully");
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Operation(summary = "Attention", description = "This API will delete the event and its associated bills")
    public ApiMessageDto<Void> deleteEvent(@PathVariable Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EVENT_NOT_FOUND));

        // Detach event from all bills
        billRepository.deleteAllByEventId(event.getId());
        // Delete event
        eventRepository.delete(event);
        return ApiMessageUtils.success(null, "Delete event successfully");
    }
}
