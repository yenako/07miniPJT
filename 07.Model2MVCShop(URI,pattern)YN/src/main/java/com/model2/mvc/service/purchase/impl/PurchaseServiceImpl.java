package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;

@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService {

	///Field
	@Autowired
	@Qualifier("purchaseDaoImpl")
	private PurchaseDao purchaseDao;
	public void setPurchaseDao(PurchaseDao purchaseDao) {
		this.purchaseDao = purchaseDao;
	}
	
	public int addPurchase(Purchase purchase) throws Exception {
		return purchaseDao.addPurchase(purchase);
	}

	public Purchase getPurchase(int tranNo) throws Exception {
		return purchaseDao.getPurchase(tranNo);
	}
	
	public int updatePurchase(Purchase purchase) throws Exception {
		System.out.println("updatePurchase in purchase S  Impl");
		return purchaseDao.updatePurchase(purchase);
	}

	public Purchase getPurchase2(int prodNo) throws Exception {
		return purchaseDao.getPurchase2(prodNo);
	}

	public Map<String, Object> getPurchaseList(Search search, String buyerId) throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("buyerId", buyerId);
		
		List<Purchase> list = purchaseDao.getPurchaseList(map);
		int totalCount = purchaseDao.getTotalCount(search);
		
		System.out.println("List<Product> 내용 => 2. product list : "+list);
		System.out.println("List<Product> 내용 => 1. totalCount : "+ totalCount);
		
		map.put("list", list);
		map.put("totalCount", totalCount);

		return map;
	}

	public Map<String, Object> getSaleList(Search search) throws Exception {
		
		List<Purchase> listSale = purchaseDao.getSaleList(search);
		int totalCountSale = purchaseDao.getTotalCount(search);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listSale", listSale);
		map.put("totalCountSale", totalCountSale);
	
		return map;
	}

	public int updateTranCode(Purchase purchase) throws Exception {
		return purchaseDao.updateTranCode(purchase);
	}

}
