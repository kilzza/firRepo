package com.avi6.service;

import com.avi6.entity.GuestBook;
import com.avi6.guestbook.dto.GuestBookDTO;
import com.avi6.guestbook.dto.PageRequestDTO;
import com.avi6.guestbook.dto.PageResultDTO;

/*
   이 인터페이스는 Service Layer 에서 Service 빈즈가 해야 할 일을 추상메서드로 선언만 합니다
   선언 시 메서드가 해야 할 일과 리턴타입을 미리 잘 생각해서 설계해야 함
   
   DTO --> Entity ,, Entity --> DTO 로 변환하는 메서드도 정의할 예정

   이 메서드는 모든 Service 구현객체들이 자동으로 상속받도록 하기 위해 default 접근자로 정의할 예정
   
   Function interface 를 이용할 예정인데 추후 설명 예정

*/


public interface GuestBookService {
	
	//글 삭제 메서드 선언, DTO를 받지 않고, 글 번호만 받아서 삭제함.
	void remove(Long gno);
	
	
	//글 수정 메서드 선언, 수정된 결과는 바로 list에서 확인되기 때문에 리턴없이 수정.
	void modify(GuestBookDTO guestBookDTO);

   //신규 글 등록 메서드 선언
   Long register(GuestBookDTO guestBookDTO);
   
   //글 번호에 해당하는 글을 리턴하는 메서드 선언.
   GuestBookDTO read(Long gno);
  
   
   //글 목록 get 메서드
   PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO);
   
   
   
   /* DTO --> Entity 변환 메서드 정의   */
   default GuestBook dtoToEntity(GuestBookDTO guestBookDTO) {
      
      GuestBook guestBook = GuestBook.builder()
                                          .gno(guestBookDTO.getGno())
                                          .title(guestBookDTO.getTitle())
                                          .content(guestBookDTO.getContent())
                                          .writer(guestBookDTO.getWriter())
                                       .build();
      
      return guestBook;
   }
   
   
   /* Entity --> DTO 변환 메서드 */
   
   default GuestBookDTO entityToDto(GuestBook guestBook) {
      
      GuestBookDTO guestBookDTO = GuestBookDTO.builder()
                                       .gno(guestBook.getGno())
                                       .title(guestBook.getTitle())
                                       .content(guestBook.getContent())
                                       .writer(guestBook.getWriter())
                                       .regDate(guestBook.getRegDate())
                                       .modDate(guestBook.getModDate())
                                    .build();
      
      return guestBookDTO;
   }
   
   
   

   

}
