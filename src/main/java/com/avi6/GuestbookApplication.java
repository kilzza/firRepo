package com.avi6;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
		


		/*BaseEntity : insert/update시에 시간값을 우리가 직접 설정하는 번거로움을 해결해주는 역할을 한다
		즉, 엔티티의 등록과 변경되는 시간을 자동으로 엔티티 내부에 기록하는 역할을 한다.
		BaseEntity를 상속받으면 따로 시간과 관련된 작업을 엔티티에 추가하지 않아도 되는 장점이 생긴다.
		그런데, BaseEntity가 변경되는걸 실시간으로 검사하는 Listner들이 모인 Context가 repo와 영속계층 중간에 
		위치하여, 엔티티의 변경이 일어나면 알려줘야한다. 이를 위해서 main 클래스에 아래처럼 특정 인터페이스를 선언해야한다.
		*/
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class GuestbookApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuestbookApplication.class, args);
	}

}
