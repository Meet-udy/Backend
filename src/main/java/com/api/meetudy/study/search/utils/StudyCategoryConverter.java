package com.api.meetudy.study.search.utils;

import com.api.meetudy.study.group.enums.StudyCategory;

public class StudyCategoryConverter {

    public static StudyCategory fromKoreanToEnum(String koreanCategory) {
        switch (koreanCategory) {
            case "어학":
                return StudyCategory.LANGUAGE;
            case "자격증":
                return StudyCategory.CERTIFICATION;
            case "인문학":
                return StudyCategory.HUMANITIES;
            case "사회과학":
                return StudyCategory.SOCIAL_SCIENCES;
            case "디자인":
                return StudyCategory.DESIGN;
            case "과학":
                return StudyCategory.SCIENCE;
            case "프로그래밍":
                return StudyCategory.PROGRAMMING;
            case "취업/커리어":
                return StudyCategory.CAREER;
            case "고시/공무원":
                return StudyCategory.QUALIFICATION_EXAM;
            case "취미":
                return StudyCategory.HOBBY;
            case "기타":
                return StudyCategory.OTHERS;
            default:
                return null;
        }
    }

}
