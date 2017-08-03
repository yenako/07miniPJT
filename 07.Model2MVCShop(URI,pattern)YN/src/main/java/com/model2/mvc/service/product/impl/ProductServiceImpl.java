package com.model2.mvc.service.product.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.common.CommonUtil;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;

@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {

	///Field
	@Autowired
	@Qualifier("productDaoImpl")
	private ProductDao productDao;
	public void setProductDao(ProductDao	productDao) {
		this.productDao = productDao;
	}
	
	///Constructor
	public ProductServiceImpl(){
		System.out.println(this.getClass());
	}
	
	
	public int addProduct(Product product) throws Exception {
		return productDao.addProduct(product);
	}

	
	public Product getProduct(int prodNo) throws Exception {
		return productDao.getProduct(prodNo);
	}

	
	public Map<String, Object> getProductList(Search search) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		
		List<Product> list = productDao.getProductList(search);

		for(int i=0; i<list.size(); i++){
			Product product = list.get(i);			
			product.setManuDate( CommonUtil.toDateStr(product.getManuDate() ));
			product.setPriceAmount( CommonUtil.toAmountStr( ""+product.getPrice() ) );
			list.set(i, product);
		}
		
		int totalCount = productDao.getTotalCount(search);
		
		map.put("list", list);
		map.put("totalCount", totalCount);
		
		return map;
	}

	public int updateProduct(Product product) throws Exception {
		return productDao.updateProduct(product);
	}

}//end of the class
