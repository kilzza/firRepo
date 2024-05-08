package com.avi6.guestbook.dto;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/*
   사용자가 list 에서 페이지 요청 시 페이지 값을 담는 DTO
*/

@Builder
@AllArgsConstructor    //파라미터를 받는 생성자를 생성해주는 annotation
@Data
public class PageRequestDTO {
   
   private int page;   //페이지번호
   private int size;   //페이지당 목록 수
   private String type; // 조건 검색의 타입(제목 or 내용 or 작성자)
   private String keyword;//검색어 
   
   
   public PageRequestDTO() {
      
      this.page = 1;
      this.size = 10;      
      
   }
   
   
   //나중에 Page 처리할 Pageable 을 리턴하는 메서드 정의
   //정렬객체를 파라미터로 받아서 처리함
   public Pageable getPageable(Sort sort) {
      
      return PageRequest.of(page-1, size, sort);
      //페이지는 0부터 시작하기 때문에 위에서 초기값인 1에서 -1  하여 0 리턴
      
   }
   
   
   
   

}
