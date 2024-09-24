package com.mariuszilinskas.eshopapi;

import com.mariuszilinskas.eshopapi.controller.CartController;
import com.mariuszilinskas.eshopapi.controller.ProductController;
import com.mariuszilinskas.eshopapi.repository.CartRepository;
import com.mariuszilinskas.eshopapi.repository.ProductRepository;
import com.mariuszilinskas.eshopapi.service.CartServiceImpl;
import com.mariuszilinskas.eshopapi.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the Spring application context and bean configuration in the EshopApi application.
 */
@SpringBootTest
class EshopApiApplicationTests {

	@Autowired
	private CartServiceImpl cartService;

	@Autowired
	private ProductServiceImpl productService;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartController cartController;

	@Autowired
	private ProductController productController;

	@Test
	void contextLoads() {
	}

	@Test
	void cartServiceBeanLoads() {
		assertNotNull(cartService, "Cart Service should have been auto-wired by Spring Context");
	}

	@Test
	void productServiceBeanLoads() {
		assertNotNull(productService, "Product Service should have been auto-wired by Spring Context");
	}

	@Test
	void cartRepositoryBeanLoads() {
		assertNotNull(cartRepository, "Cart Repository should have been auto-wired by Spring Context");
	}

	@Test
	void productRepositoryBeanLoads() {
		assertNotNull(productRepository, "Product Repository should have been auto-wired by Spring Context");
	}

	@Test
	void cartControllerBeanLoads() {
		assertNotNull(cartController, "Cart Controller should have been auto-wired by Spring Context");
	}

	@Test
	void productControllerBeanLoads() {
		assertNotNull(productController, "Product Controller should have been auto-wired by Spring Context");
	}

}
