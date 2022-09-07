package diginamic.gdm.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import diginamic.gdm.repository.NatureRepository;

@SpringBootTest
class NatureTest {

	@Autowired
	private NatureRepository natureRepository;
	
	@Test
	void test() {
		Nature testNat = new Nature();
		natureRepository.save(testNat);
		assertEquals(natureRepository.findByEndOfValidityIsNull().size(), 1);
	}

}
