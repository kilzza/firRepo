package com.avi6;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.avi6.entity.QGuestBook;

@SpringBootTest
public class QDSLTest {

	@Test
	public void dslTest() {
		QGuestBook qGeustBook = QGuestBook.guestBook;
	}
}
