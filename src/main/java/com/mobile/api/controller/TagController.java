package com.mobile.api.controller;

import com.mobile.api.controller.base.BaseController;
import com.mobile.api.dto.ApiMessageDto;
import com.mobile.api.dto.PaginationDto;
import com.mobile.api.dto.tag.TagDto;
import com.mobile.api.enumeration.ErrorCode;
import com.mobile.api.exception.ResourceNotFoundException;
import com.mobile.api.form.tag.CreateTagForm;
import com.mobile.api.form.tag.UpdateTagForm;
import com.mobile.api.mapper.TagMapper;
import com.mobile.api.model.criteria.TagCriteria;
import com.mobile.api.model.entity.Tag;
import com.mobile.api.model.entity.User;
import com.mobile.api.repository.TagRepository;
import com.mobile.api.repository.UserRepository;
import com.mobile.api.utils.ApiMessageUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tag")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TagController extends BaseController {
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/client/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<PaginationDto<TagDto>> getTagList(
            @Valid @ModelAttribute TagCriteria tagCriteria,
            Pageable pageable
    ) {
        tagCriteria.setUserId(getCurrentUserId());
        Specification<Tag> specification = tagCriteria.getSpecification();
        Page<Tag> page = tagRepository.findAll(specification, pageable);

        PaginationDto<TagDto> responseDto = new PaginationDto<>(
                tagMapper.fromEntitiesToTagDtoList(page.getContent()),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return ApiMessageUtils.success(responseDto, "List categories successfully");
    }

    @GetMapping(value = "/client/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<TagDto> getTag(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TAG_NOT_FOUND));

        return ApiMessageUtils.success(tagMapper.fromEntityToTagDto(tag), "Get tag successfully");
    }

    @PostMapping(value = "/client/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> createTag(@Valid @RequestBody CreateTagForm createTagForm) {
        Tag tag = tagMapper.fromCreateTagFormToEntity(createTagForm);

        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        tag.setUser(user);
        tagRepository.save(tag);

        return ApiMessageUtils.success(null, "Create tag successfully");
    }

    @PutMapping(value = "/client/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> updateTag(@Valid @RequestBody UpdateTagForm updateTagForm) {
        Tag tag = tagRepository.findById(updateTagForm.getId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TAG_NOT_FOUND));

        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.USER_NOT_FOUND));
        tag.setUser(user);

        tagMapper.updateFromUpdateTagForm(tag, updateTagForm);
        tagRepository.save(tag);

        return ApiMessageUtils.success(null, "Update tag successfully");
    }

    @DeleteMapping(value = "/client/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Void> deleteTag(@PathVariable Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TAG_NOT_FOUND));

        tag.getBills().clear();
        tagRepository.save(tag);
        tagRepository.delete(tag);
        return ApiMessageUtils.success(null, "Delete tag successfully");
    }
}
