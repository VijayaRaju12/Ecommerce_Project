package com.jsp.ecom_proj.comtroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jsp.ecom_proj.dto.Product;
import com.jsp.ecom_proj.repository.ProductRepository;
import com.jsp.ecom_proj.service.ProductService;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import lombok.Delegate;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProductController {
	
	@Autowired
	private ProductService service;
	@Autowired
	ProductRepository repo;

	@GetMapping("product/{id}")
	public ResponseEntity<Product> getProduct(@PathVariable int id)
	{
		Product product=service.getProductById(id);
		if(product!=null)
		{
			return new ResponseEntity<Product>(product,HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	@GetMapping("/products")
	public ResponseEntity<List<Product>> getAllproducts()
	{
		return new ResponseEntity<List<Product>>(service.getAllProducts(),HttpStatus.OK);
	}
	@PostMapping("/product")
	public ResponseEntity<?> addProduct(@RequestPart Product product,@RequestPart MultipartFile imageFile)
	{
		try {
			Product product1 = service.addProduct(product,imageFile);
			System.out.println(product);
			return new ResponseEntity<>(product1,HttpStatus.CREATED);
		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/product/{productId},image")
	public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId)
	{
		Product product=service.getProductById(productId);
		byte[] imageFile =product.getImageDate();
		return ResponseEntity.ok()
				.contentType(MediaType.valueOf(product.getImageType()))
				.body(imageFile);
	}
	@PutMapping("/product/{id}")
	public ResponseEntity<String> updateProduct(@PathVariable int id,@RequestPart Product product,@RequestPart MultipartFile imageFile)
	{
		Product product1=null;
		try {
			 product1= service.updateProduct(id,product,imageFile);

		} catch (Exception e) {
			// TODO: handle exception
			return new ResponseEntity<String>("Failed to Update",HttpStatus.BAD_REQUEST);

		}
		if(product1!=null)
		{
			return new ResponseEntity<String>("Updated",HttpStatus.OK);
			
		}
		else {
			return new ResponseEntity<String>("Failed to Update",HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/product/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable int id)
	{
		Product product=service.getProductById(id);
		if(product!=null)
		{
			service.deleteProduct(id);
			return new ResponseEntity<String>("Deleted",HttpStatus.OK);
		}
		else {
			return  new ResponseEntity<String>("Product not found",HttpStatus.NOT_FOUND);
		}
	}
	@GetMapping("/products/search")
	public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword)
	{
		List<Product> products=service.searchProducts(keyword);
		return new ResponseEntity<List<Product>>(products,HttpStatus.OK);
	}
}
