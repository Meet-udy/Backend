package com.api.meetudy.study.group.controller;

import com.api.meetudy.global.response.ApiResponse;
import com.api.meetudy.member.service.MemberService;
import com.api.meetudy.study.group.dto.StudyGroupApplicantDto;
import com.api.meetudy.study.group.dto.StudyGroupDto;
import com.api.meetudy.study.group.service.GroupJoinService;
import com.api.meetudy.study.group.service.GroupManagementService;
import com.api.meetudy.study.group.service.GroupRetrievalService;
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

    private final MemberService memberService;
    private final GroupJoinService groupJoinService;
    private final GroupManagementService groupManagementService;
    private final GroupRetrievalService groupRetrievalService;

    @Operation(summary = "스터디 그룹 가입 요청 API")
    @PostMapping("/{groupId}")
    public ResponseEntity<ApiResponse<String>> requestJoinGroup(@PathVariable Long groupId,
                                                                Principal principal) {
        String message = groupJoinService.requestJoinGroup(groupId, memberService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "스터디 그룹 생성 API")
    @PostMapping
    public ResponseEntity<ApiResponse<String>> createStudyGroup(@Valid @RequestBody StudyGroupDto studyGroupDto,
                                                                Principal principal) {
        String response = groupManagementService.createStudyGroup(studyGroupDto, memberService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 가입 요청 승인 API")
    @PutMapping("/{groupMemberId}/approval")
    public ResponseEntity<ApiResponse<String>> approveJoinRequest(@PathVariable Long groupMemberId) {
        String response = groupManagementService.approveJoinRequest(groupMemberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹 가입 요청 거절 API")
    @PutMapping("/{groupMemberId}/rejection")
    public ResponseEntity<ApiResponse<String>> rejectJoinRequest(@PathVariable Long groupMemberId) {
        String response = groupManagementService.rejectJoinRequest(groupMemberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "스터디 그룹에 대한 가입 요청 조회 API")
    @GetMapping("/{groupId}")
    public ResponseEntity<ApiResponse<List<StudyGroupApplicantDto>>> getJoinRequests(@PathVariable Long groupId) {
        List<StudyGroupApplicantDto> response = groupJoinService.getJoinRequests(groupId);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "사용자가 리더로 있는 스터디 그룹 조회 API")
    @GetMapping("/created-groups")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> getCreatedStudyGroups(Principal principal) {
        List<StudyGroupDto> response = groupRetrievalService.getCreatedStudyGroups(memberService.getCurrentMember(principal));
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @Operation(summary = "사용자가 멤버로 있는 스터디 그룹 조회 API")
    @GetMapping("/joined-groups")
    public ResponseEntity<ApiResponse<List<StudyGroupDto>>> getJoinedStudyGroups(Principal principal) {
        List<StudyGroupDto> response = groupRetrievalService.getJoinedStudyGroups(memberService.getCurrentMember(principal)
        );
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

}