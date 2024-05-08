package com.avi6.guestbook.repository;

import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties.Build;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.avi6.entity.GuestBook;
import com.avi6.entity.QGuestBook;

import com.avi6.repository.GuestBookRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

@SpringBootTest
public class GuestBookRepoTests {
   
   @Autowired
   private GuestBookRepository bookRepository;
   
   
  // @Test
   public void insertDummy() {
      
      IntStream.rangeClosed(1, 300).forEach(i -> {
         
         GuestBook guestBook = GuestBook.builder()
                                             .title("title" + i)
                                             .content("content" + i)
                                             .writer("writer" + (i % 10))
                                          .build();
         
         System.out.println(bookRepository.save(guestBook));
         
      });
   }
   
   
   
  // @Test
   public void updateTest() {
      
      //특정 gno 를 찾아서 해당 row 의 제목과 내용을 수정 후 update 합니다
      //이때 수정날짜도 변경되는지 확인 예정
      
      Optional<GuestBook> res = bookRepository.findById(300L);
      
      if(res.isPresent()) {
         
         GuestBook guestBook = res.get();
         
         guestBook.setTitle("changed title");
         guestBook.setContent("changed content");
         
         
         //repository 의 쿼리메서드 중 save (insert or update)
         bookRepository.save(guestBook);
         
      }

   }
   

/*
   QueryDSL
   
   insert, update, delete 에 비해서
   select 는 여러 조건 (join, or, and, gt, ge, ..) 이 붙기 때문에 일반 repository 의 메서드만으로는 select 가 불가함
   이에 QueryMethod 와 @Query(쿼리문)을 학습하였음
   
   외부 lib 중에 select 를 조금 더 쉽게 처리하도록 해주는 것들이 있음. QueryDSL 도 이 중 하나임
   
   내부 API 를 이용해서 entity 를 대상으로 문자열 메서드를 이용하듯이
   QueryDSL 은 특정 값이 존재하는지 여부를 직관적으로 표현해주도록 함
   따라서 쿼리를 작성할 때 매우 편함
   
   또한 조건 (join, or, and, gt, ge, ..) 이 연속으로 나열되는 것들도
   or, and 등의 메서드를 이용해서 조건을 결합시킬 수 있음
   
   GuestBookRepository 인터페이스는 JPA 와 QueryDSL 을 extends 받았기에
   조건을 이용한 Select 는 QueryDSL 이 진행하고
   리턴되는 Entity 의 페이징처리 등은 JPA 가 하도록 결합한 것임
   
   
   
   << QueryDSL 사용방법 >>
   
   0. Q-Entity 객체 생성
   
   1. BooleanBuilder 객체 생성
   
   2. BooleanBuilder 에 사용될 select 구문을 담고 있는 Predecate 타입 객체 생성
      entity 의 컬럼을 대상으로 select 하려는 값을 메서드로써 호출함
   
   3. 위에서 처리된 결과는 QueryDSL 의 Predecate.... type 객체로 리턴되는데
      대표적인 것이 BooleanExpression 임
   
   4. 3에서 리턴된 것을 BooleanBuilder 를 통해 필요시 조건(and, or 등등) 을 추가함
   
   5. 최종 정리된 builder 를 JPA 의 일반 select 메서드에 파라미터로 넘겨서 최종 select 처리하고
      리턴은 Page 등으로 받은
   
   6. 필요시 정렬과 관련된 부분도 5번에서 파라미터로 같이 넘길 수 있음

*/   

   
   
   @Test
   public void qTest() {   
      //페이징에 필요한 Pageable 객체 생성. 일반 JPA 임
      Pageable pageable = PageRequest.of(0, 10, Sort.by("gno"));
      
      
      /* QueryDSL 사용 시작 */
      
      //1. Q-Entity 객체 생성
      QGuestBook qGuestBook = QGuestBook.guestBook;
      
      
      //2. BooleanBuilder 객체 생성
      BooleanBuilder Builder = new BooleanBuilder();
      
      
      //3. Q-객체의 특정 메서드를 통해서 select 값을 얻어낸다. 리턴타입은 Predecate...
      //스트링 메서드...???
   
      String key = "1";
      BooleanExpression exTitle = qGuestBook.title.contains(key);
   
      BooleanExpression exContent = qGuestBook.title.contains(key);

      //위처럼 찾는 컬럼이 하나 이상인 경우, 조건을 나열 후 나열된 BooleanExp를 하나로 합칠 수 있음.
      
      BooleanExpression all = exTitle.or(exContent);
    
      Builder.and(all);
      
      //다시 조건을 추가 할수도 있다. where에 해당하는 조건은 queryDSL 객체의 메서드를 이용할 수 있다.
      Builder.and(qGuestBook.gno.gt(290L)); //gno가 10이상인 조건 추가.      
      
      Page<GuestBook> res = bookRepository.findAll(Builder, pageable);
      
      res.forEach(entity -> {
         
         System.out.println(entity);
   
      });
      
   }
   
   
   

}
