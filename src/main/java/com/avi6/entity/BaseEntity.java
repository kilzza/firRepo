package com.avi6.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

		/*
		 *BaseEntity는 하위의 엔티티를 리스너들이 관리하도록 정의된 것
		 *따라서 BaseEntity 자체가 table에 매핑하려는것이 아니라, 하위의 애들이 매핑의 대상이 된다.
		 *이러한 목적으로 선언되는 어노테이션은 아래와 같다. 
		 */
@MappedSuperclass
//다음엔 이 하위 엔티티들의 변경 이벤트가 발생하면 알려줄 리스너(main클래스에 선언한)를 등록한다.
@EntityListeners(value= {AuditingEntityListener.class})
@Getter

public abstract class BaseEntity {
		
		@CreatedDate	
		@Column(name="regdate", updatable =false)
		
		private LocalDateTime regDate;
		
		@LastModifiedDate
		@Column(name = "moddate")
		
		private LocalDateTime modDate;
}
