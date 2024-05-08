package com.avi6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.avi6.guestbook.dto.GuestBookDTO;
import com.avi6.guestbook.dto.PageRequestDTO;
import com.avi6.service.GuestBookService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/guestbook")//localhost/guestbook(context)list(path)
@Log4j2
@RequiredArgsConstructor // 클래스 내부의 필드 중 private final로 선언된 인터페이스의 하위객체를 자동 주입 시키는 어노테이션.
public class GuestBookController {

    
   // service 선언 해야함.. 반드시 private final 로 선언해서 annotation 으로 객체 주입하도록 합니다.
   // 반드시 service interface 를 선언해야합니다... 그럼 스프링이 호출할 때, 이를 구현한 구현객체의
   // 오버라이드 된 메서드를 자동 호출해줍니다.
   private final GuestBookService guestBookService;
   
   
    // 글 삭제 메서드, 글 번호만 받아서 service에 넘기고, 결과 메세지를 담아서 list페이지로 넘긴다. 
    @PostMapping("/remove")
    public String remove(@RequestParam("gno") Long gno, RedirectAttributes redirectAttributes) {
    	
    	guestBookService.remove(gno);
    	
    	redirectAttributes.addFlashAttribute("msg", gno + " 글이 삭제되었습니다.");
    	
    	return "redirect:/guestbook/list";
    }
    
   
   
   
   /*
    * 글 수정을 처리하는 메서드 정의. 요청 이름은 아래와 같이 modify로 오지만 method가 post이기에  post mapping을 하면
   	  글 수정 메서드가 호출이 된다.
   	  1.모든 폼 데이터는 DTO로 매핑됩니다.
   	  2.list로 리다이렉트 하고, 페이지 정보와 추가정보를 넘긴다.
   	  2-1. 추가정보는 gno를 넘겨서 list에서 모달창에 띄운다.
    */
   @PostMapping("/modify")
   public String modify(GuestBookDTO guestBookDTO, @ModelAttribute("pageReqDTO") PageRequestDTO pageReqDTO, RedirectAttributes redirectAttributes) {
	   log.info("수정메서드 실행됨 --> DTO 정보 : " + guestBookDTO);
	   
	   guestBookService.modify(guestBookDTO);
	   
	   //추가정보 보내기
	   redirectAttributes.addFlashAttribute("page",pageReqDTO.getPage());
	   redirectAttributes.addFlashAttribute("gno",guestBookDTO.getGno());
	   
	   
	   return "redirect:/guestbook/list";
	      
   }
   
   
   //글 상세 요청을 처리하는 메서드 정의
   //글상세 요청 처리 메서드
   @GetMapping({"/read", "/modify"})
   public void read(@RequestParam("gno") Long gno, @ModelAttribute("pageRequestDTO") PageRequestDTO pageRequestDTO, Model model) {
      
      log.info("요청된 글번호: " + gno);
      
      
      //service 를 통해 gno 에 해당하는 DTO 를 얻어냅니다
      GuestBookDTO guestBookDTO = guestBookService.read(gno);
      log.info("글내용: " + guestBookDTO);
      
      
      //model : dto --> service??? viewer??? 로 보내주는 역할
      model.addAttribute("dto", guestBookDTO);
      
      
   }
   
   
   @GetMapping("/register")
   // 얘는 폼만 띄우는 역할만 함
   public void register() {
      log.info("신규 글 등록 get 요청됨");
   }
   
   // 등록 요청을 했을 때 처리하는 메서드입니다.
   // 등록이 완료되었을 때는 글을 확인할 수 있는 list 로 redirect 시킵니다.
   // 리다이렉트시 글 성공 여부에 대한 메세지를 파라미터로 같이 보낼게요.
   // 리다이렉트에 파람을 추가하기 위해서는 반드시 RedirectAttributes 를 메서드에 선언하고 주입받도록 합니다.
   // 이 메서드가 호출되면, 사용자가 작성한 내용이 모두 DTO 에 자동 매핑되도록 합니다.
   @PostMapping("/register")// 등록이니까 POST
   public String registerPost(GuestBookDTO guestBookDTO, RedirectAttributes redirectAttributes) {
      log.info("신규 글 등록 요청됨 DTO 정보 ----> " + guestBookDTO);
      
      Long gno = guestBookService.register(guestBookDTO);
      redirectAttributes.addFlashAttribute("msg", gno);// insert 후 반환되는 entity 의 글번호를 같이 보냅니다.
      return "redirect:/guestbook/list";
   }
   

   
   
   
   
   
   // localhost/guestboook or ../list 모두 응답요청 할 수 있도록 url 매핑을 2개함.
   
   // 파라미터는 무조건 존재하다고 생각합니다. 
   // 이유는 파라미터가 존재하면, 이 값을 DTO 에 저장 후 service 에 넘겨야 하기 때문임
   // 만약 파라미터가 없으면 , 서비스층에서 설정한 기본페이지(1페이지)를 선택해서
   // 해당 페이지의 글 목록과, 전체 페이지 수를 넘겨서
   // viewer 에게 같이 넘겨야 합니다.
   // 따라서 아래의 getmapping 은 분리되도록 합니다.
   @GetMapping({"/"})
   public String list() {// 데이터를 가지고 오니까 get 방식
      // viewer 를 리턴하도록 string 리턴 메서드 작성
      log.info("list root 요청됨");
      return "redirect:/guestbook/list";
      // 폴더를 guestbook으로 생성 후 이동
      
      
      //글을 완성 --> list.jsp(가장 최신글 => DB --> 가장 큰 번호 --> DESC --> list에서 보여줌) 
         //--> 내가 list 에서 어떤 페이지를 볼건지에 대한 선택권 없음. 즉, 파라미터 없음 
         //--> 서버에서는 파라미터 없이 list 페이지를 요청하면 무조건 최신글을 보여주도록 준비해야함. 즉 1page를 보여줌.
         
         //케이스2 : 내가 새글을 안 쓰고, 글들을 읽을때는 각 글이 속한 페이지 넘버가 발생함.(페이지 index)
         
         //서버는 list 페이지에서 항상 파라미터가 있는지 여부를 생각해야함.(있을 수도 없을 수도 있음)
      
      
   }
   
   // guestbook context root 로 오는 요청을 받은 list 매핑정의합니다.
   // 이 때 파라미터가 있건 없건, 모두 DTO(RequestDTO) 를 생성합니다.
   // 생성방법은 간단, 파라미터에 사용할 dto 이름만 적어주면 객체 주입 및 같은 key 를 가졌다면 속성도 자동 세팅됨
   @GetMapping("/list")
   public void list(PageRequestDTO requestDTO, Model model) {
      log.info("list path 요청완료됨");
      
      // list 글들을 얻어내는 service 의 메서드를 호출해서 항목을 얻어내고
      // 이를 viewer 에게 전달해서 응답을 완료하게 합니다.
      // 뷰어한테 데이터를 넘길때는, 메서드의 파라미터에 model 을 명시하면 됩니다.
      // 이렇게되면, 컨트롤러가 model 객체를 이용해서 view 에게 뭔가를 같이 넘긴다고 판단하고,
      // 이 model 은 우리가 jsp 에서 배웠던, setAttribute 와 같은 addAttribute 가 있습니다.
      // 여기에 key, value 를  셋업하면, 모델에 실려서 viewer 에게 전달되게 되는 내부 로직입니다.
      
      //처리된 결과를 Model객체를 통해서(addAttribute를 이용) viewer에게 보낸다.
      
      log.info("Result---> " + guestBookService.getList(requestDTO));
      model.addAttribute("result", guestBookService.getList(requestDTO));
      System.out.println("model --->" + model);
      
   }
   
}