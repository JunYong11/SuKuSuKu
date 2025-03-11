package com.web.sukusuku.controller;

import com.web.sukusuku.service.StudyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
//@SpringBootTest
@RequiredArgsConstructor
//@Controller
//@Transactional // 트렌젝셔널 어노테이션 :비즈니스 로직이 수행되는 Service 계층에 쓰는 게 원칙
@RestController//(@Controller + @ResponseBody)
//@RequestMapping("/api/word")
public class StudyController {
	private final StudyService studyService;

//	@GetMapping("/")
//	public String getMethodName() {
//	studyService.WordRepositoryTest();
//
//
////		return "studies/study";
//		return "studies/levelChoice";
//
//	}
@GetMapping("/")
public String getMethodName() {
	studyService.WordRepositoryTest();
	log.info("test 완료");

	// return "studies/study"; -> RestController면 반환 값이 JSON/String 이다.
	return "테스트 완료!";  // 단순히 문자열 반환, 뷰 리턴하려면 @Controller 사용해야 함
}

	
}
