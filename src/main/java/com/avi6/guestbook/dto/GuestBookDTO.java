package com.avi6.guestbook.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 *클라이언트로부터 전송된 파람이 서비스 레이어로 넘어갈 때, 해당 데이터를 담거나 
 *반대로, 클라이언트로 전송될 데이터를 엔티티로부터 담겨질 대상을 DTO에 함. 
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GuestBookDTO {
	
	private Long gno;
	private String title;
	private String content;
	private String writer;
	private LocalDateTime regDate, modDate;
}
