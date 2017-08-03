package com.model2.mvc.web.puchase;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.model2.mvc.common.CommonUtil;
import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;

@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {

	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	///Constructor
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	@Value("#{commonProperties['pageUnit']}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	int pageSize;
	
	
	//@RequestMapping("/addPurchaseView.do")
	@RequestMapping( value="addPurchase", method=RequestMethod.GET )
	public String addPurchase(@RequestParam("prodNo") String prodNo,
																		Model model ) throws Exception{
		System.out.println("/purchase/addPurchase : GET");
		
		Product product = productService.getProduct(Integer.parseInt(prodNo));
		model.addAttribute("product", product);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}
	
	
	@RequestMapping( value="addPurchase", method=RequestMethod.POST )
	public String addPurchase(@RequestParam("prodNo") String prodNo,
												    	@RequestParam("buyerId") String buyerId,
												    	@ModelAttribute("purchase") Purchase purchase,
												    	Model model) throws Exception{
		System.out.println("/purchase/addPurchase : POST");
		
		purchase.setBuyer(userService.getUser(buyerId));
		int prodNoInt = Integer.parseInt(prodNo);
		purchase.setPurchaseProd(productService.getProduct(prodNoInt));
		purchase.setTranCode("1");
		purchaseService.addPurchase(purchase);
		
		model.addAttribute("prodNo", prodNo);
		model.addAttribute("purchase", purchase);
		model.addAttribute("buyerId", buyerId);
		
		return "forward:/purchase/purchaseComplete.jsp";
	}
	
	@RequestMapping( value="getPurchase", method=RequestMethod.GET )
	public String getPurchase(@RequestParam("tranNo") int tranNo, //string/int?
																		Model model) throws Exception{
		System.out.println("/purchase/getPurchase : GET");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		purchase.setDivyDate(CommonUtil.toDateStr(purchase.getDivyDate()));
		model.addAttribute("purchase", purchase);

		return "forward:/purchase/readPurchase.jsp";
	}
	
	@RequestMapping( value="listPurchase", method=RequestMethod.GET )
	public String listPurchase(@ModelAttribute("search") Search search,
															HttpSession session, Model model) throws Exception{
		System.out.println("/purchase/listPurchase :GET");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		User user = (User) session.getAttribute("user");
		search.setSearchKeyword(user.getUserId());
		Map<String , Object> map=purchaseService.getPurchaseList(search, user.getUserId());
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println("resultPage : " +resultPage);
		
		model.addAttribute("buyerId", user.getUserId());
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);

		return "forward:/purchase/listPurchase.jsp";
	}	
	
	@RequestMapping( value="updatePurchase", method=RequestMethod.GET )
	public String updatePurchase(@RequestParam("tranNo") int tranNo,
																Model model) throws Exception{
		System.out.println("/purchase/updatePurchase : GET");
		
		Purchase purchase=purchaseService.getPurchase(tranNo);
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchase.jsp";
	}
	
	@RequestMapping( value="updatePurchase", method=RequestMethod.POST )
	public String updatePurchase(@RequestParam("tranNo") int tranNo,
															 @ModelAttribute("purchase") Purchase purchase,
																Model model) throws Exception{
		System.out.println("/purchase/updatePurchase : POST");
		
		purchase.setTranNo(tranNo);
		purchaseService.updatePurchase(purchase);
		
		model.addAttribute("tranNo", tranNo);

		return "redirect:/purchase/getPurchase";
	}
	
	@RequestMapping( value="updateTranCode", method=RequestMethod.GET )
	public String updateTranCode(@RequestParam("tranNo") int tranNo,
																@RequestParam("tranCode") String tranCode,
																Model model) throws Exception{
		System.out.println("/purchase/updateTranCode : GET");
		
		Purchase purchase = purchaseService.getPurchase(tranNo);
		purchase.setTranCode(tranCode);
		purchaseService.updateTranCode(purchase);

		return "redirect:/purchase/listProduct";
	}
	
	@RequestMapping( value="updateTranCodeByProd", method=RequestMethod.GET )
	public String updateTranCodeByProd(@RequestParam("prodNo") int prodNo,
																			@RequestParam("tranCode") String tranCode,
																			Model model) throws Exception{
		System.out.println("/purchase/updateTranCodeByProd : GET");
		
		Purchase purchase = purchaseService.getPurchase2(prodNo);
		purchase.setTranCode(tranCode);
		purchaseService.updatePurchase(purchase);
		
		model.addAttribute("menu", "manage");

		return "redirect:/purchase/listProduct";
	}
}
