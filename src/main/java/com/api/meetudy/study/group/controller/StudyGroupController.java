package com.api.meetudy.study.group.controller;

import com.api.meetudy.auth.service.AuthenticationService;
import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.study.group.dto.StudyGroupApplicantDto;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.service.StudyGroupService;
import com.api.meetudy.study.group.service.GroupManagementService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study-groups")
public class StudyGroupController {

    private final AuthenticationService authenticationService;
    private final StudyGroupService studyGroupService;
    private final GroupManagementService groupManagementService;

    @Operation(summary = "스터디 그룹 가입 요청 API")
    @PostMapping("/{groupId}")
    public ResponseEntity<ApiResponse<String>> requestJoinGroup(@PathVariable Long groupId,
                                                                Principal principal) {
        String message = studyGroupService.requestJoinGroup(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "스터디 그룹 생성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createStudyGroup(@Valid @RequestBody StudyGroupDto studyGroupDto,
                                                                Principal principal) {
        String response = groupManagementService.createStudyGroup(studyGroupDto, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 가입 요청 승인 API")
    @PutMapping("/{groupMemberId}/approval")
    public ResponseEntity<ApiResponse<String>> approveJoinRequest(@PathVariable Long groupMemberId,
                                                                  Principal principal) {
        String response = groupManagementService.approveJoinRequest(groupMemberId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 가입 요청 거절 API")
    @PutMapping("/{groupMemberId}/rejection")
    public ResponseEntity<ApiResponse<String>> rejectJoinRequest(@PathVariable Long groupMemberId,
                                                                 Principal principal) {
        String response = groupManagementService.rejectJoinRequest(groupMemberId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 인원 모집 완료 API")
    @PutMapping("/{groupId}/closure")
    public ResponseEntity<ApiResponse<String>> closeRecruitment(@PathVariable Long groupId,
                                                                Principal principal) {
        String response = groupManagementService.closeRecruitment(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹에 대한 가입 요청 조회 API")
    @GetMapping("/{groupId}")
    public ResponseEntity<ApiResponse<List<StudyGroupApplicantDto>>> getJoinRequests(@PathVariable Long groupId,
                                                                                     Principal principal) {
        List<StudyGroupApplicantDto> response = groupManagementService.getJoinRequests(groupId, authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "사용자가 리더로 있는 스터디 그룹 조회 API")
    @GetMapping("/created-groups")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> getCreatedStudyGroups(Principal principal) {
        List<StudyGroupDto> response = studyGroupService.getCreatedStudyGroups(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "사용자가 멤버로 있는 스터디 그룹 조회 API")
    @GetMapping("/joined-groups")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> getJoinedStudyGroups(Principal principal) {
        List<StudyGroupDto> response = studyGroupService.getJoinedStudyGroups(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "가입 요청을 보낸 스터디 그룹 조회 API")
    @GetMapping("/pending-groups")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> getPendingStudyGroups(Principal principal) {
        List<StudyGroupDto> response = studyGroupService.getPendingStudyGroups(authenticationService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}