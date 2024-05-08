package com.avi6.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.avi6.entity.GuestBook;
import com.avi6.guestbook.dto.GuestBookDTO;
import com.avi6.guestbook.dto.PageRequestDTO;
import com.avi6.guestbook.dto.PageResultDTO;
import com.avi6.service.GuestBookService;


@SpringBootTest
public class GuestBookServiceTests {

   
   @Autowired
   private GuestBookService guestBookService;  //@Autowired 선언,, 하위타입 구현객체 생성 및 자동주입됨
   
   
   @Test
   public void testList() {
      
      PageRequestDTO pageReqDTO = PageRequestDTO.builder()
                                       .page(1)
                                       .size(10)
                                    .build();
      
      PageResultDTO<GuestBookDTO, GuestBook> resultDTO = guestBookService.getList(pageReqDTO);
      
      System.out.println(resultDTO);
      System.out.println("prev: " + resultDTO.isPrev());
      System.out.println("next: " + resultDTO.isNext());
      System.out.println("total: " + resultDTO.getTotalPage());
      
      System.out.println("-------------------------");
      
      for(GuestBookDTO guestBookDTO : resultDTO.getDtoList()) {
         
         System.out.println(guestBookDTO);
         
      }
         
      System.out.println("-------------------------");   
         
      
      resultDTO.getPageList().forEach(i -> System.out.println(i));
         
      
      
      
      
      
   }
   
   
      
   //@Test
   public void registerTest() {
      
      GuestBookDTO dto = GuestBookDTO.builder()
                                 .title("title")
                                 .content("layertest")
                                 .writer("kilzza")
                              .build();
      
      
      //guestBookService.register()
      System.out.println(guestBookService.register(dto));
      
      
   }
   
   
}
