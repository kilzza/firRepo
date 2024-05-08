package com.avi6.service;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.avi6.entity.GuestBook;
import com.avi6.entity.QGuestBook;
import com.avi6.guestbook.dto.GuestBookDTO;
import com.avi6.guestbook.dto.PageRequestDTO;
import com.avi6.guestbook.dto.PageResultDTO;
import com.avi6.repository.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;


/*
   컨트롤러에게서 service bean 으로 스캔되기 위해서는
   반드시 자신이 서비스빈임을 annotation 으로 선언해야 함
*/


@Service
@RequiredArgsConstructor   //인터페이스타입의 하위객체를 자동으로 생성(주입)
@Log4j2
public class GuestBookServiceImple implements GuestBookService {

   //필요한 repository 는 필드로 선언해서 주입어노테이션을 추가하면 됨
   //반드시 private final 로 선언되어야 함
   
   private final GuestBookRepository guestBookRepository;
   
   
   //글 수정, 제목, 내용만 수정.
   @Override
	public void modify(GuestBookDTO guestBookDTO) {
		//where gno = dto.gno에 해당하는 것만 수정을 해야한다.
	   //그래서 우선 get부터 하고 내용이 있다면 수정
	   
	  Optional<GuestBook> res = guestBookRepository.findById(guestBookDTO.getGno());
	
	  if(res.isPresent()) {
		  //글번호에 해당하는게 존재하니까 update를 한다.
		  GuestBook guestBook = res.get(); //원본 엔티티 get
		  
		  guestBook.setTitle(guestBookDTO.getTitle());
		  guestBook.setContent(guestBookDTO.getContent());
		  
		  guestBookRepository.save(guestBook);
		 
	  }
	  	
   }
   
   @Override
   public Long register(GuestBookDTO guestBookDTO) {
      
      System.out.println("----- . DTO check -----");
      
      GuestBook entity = dtoToEntity(guestBookDTO);
      
      System.out.println(" entity -->" + entity);
      
      guestBookRepository.save(entity);
      
      return entity.getGno();
   }
   
   
   @Override
   public PageResultDTO<GuestBookDTO, GuestBook> getList(PageRequestDTO requestDTO){
      
      Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
      
      
      //조건 검색을 위한 코드
      BooleanBuilder booleanBuilder = getSearch(requestDTO);
      
      Page<GuestBook> result = guestBookRepository.findAll(booleanBuilder, pageable);
      
      //위에서 얻어낸 ROW 별 엔티티를 최종적으로 사용자에게 보내기 위해서는 GuestBookDTO로 변환해야 합니다.
      //그 처리를 하는 클래스는 PageResultDTO<- 파라미터로 변환될 엔티티를 담고있는 객체와 이를 변환할 기능(function)을 제네릭으로 선언했음.
      //아래의 Function 객체는 엔티티-> dto로 변환하는 기능의 service 
      //interface default 메서드로 매핑을 했음.
      Function<GuestBook, GuestBookDTO> fn = (entity -> entityToDto(entity));
            
      return new PageResultDTO<>(result,fn);
      
   }

   private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
	      
	      String type = requestDTO.getType();
	      
	      BooleanBuilder booleanBuilder = new BooleanBuilder();
	      
	      QGuestBook qGuestBook = QGuestBook.guestBook;
	      
	      String keyword = requestDTO.getKeyword();
	      
	      
	      
	      BooleanExpression exp = qGuestBook.gno.gt(0L);   //1번글 이상부터 검색대상으로 시작함
	      

	            booleanBuilder.and(exp);
	            
	            if(type == null || keyword.length() == 0) {
	               
	               return booleanBuilder;
	               
	            }
	      
	      //검색조건 작성
	      BooleanBuilder booleanBuilder2 = new BooleanBuilder();
	      
	            if(type.equals("t")) {
	               
	               booleanBuilder2.or(qGuestBook.title.contains(keyword));
	               
	            }
	            
	            
	            if(type.equals("c")) {
	               
	               booleanBuilder2.or(qGuestBook.content.contains(keyword));
	               
	            }
	            
	            
	            if(type.equals("w")) {
	               
	               booleanBuilder2.or(qGuestBook.writer.contains(keyword));
	               
	            }
	      
	      
	      //위 조건 모두 통합
	      
	      booleanBuilder.and(booleanBuilder2);
	      
	      return booleanBuilder;
	      

	   }
   
	  



@Override
   public GuestBookDTO read(Long gno) {
	   //findbyId()를 이용해서 하나의 ROW를 엔티티에 담아온다.
	   Optional<GuestBook> result = guestBookRepository.findById(gno);
	   GuestBookDTO bookDTO = null;
	   if(result.isPresent()) {//글 넘버에 해당하는 Row가 존재한다면, 엔티티를 DTO로 변환함.
		   bookDTO = entityToDto(result.get());
	   }
	   
	   return bookDTO;
   }
   
   @Override
	public void remove(Long gno) {
	   guestBookRepository.deleteById(gno);
	   
		
	}
   
}
