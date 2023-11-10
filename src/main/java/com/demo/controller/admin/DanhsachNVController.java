package com.demo.controller.admin;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.demo.entities.Nhanvien;
import com.demo.repositories.AccountRepository;
import com.demo.repositories.YeucauRepository;
import com.demo.services.AccountService;
import com.demo.services.YeucauService;

@Controller
@RequestMapping("admin/danhsachnv")
public class DanhsachNVController {
	
	@Autowired
	private AccountService accountService;
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private YeucauService yeucauService;
	
//  cau 1b - y 1
 	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index(ModelMap modelMap) {
		modelMap.put("nhanviens",accountService.findAllNv());
		return "admin/danhsachnv/index";
	}
	
//	cau 1b - y 2
	@RequestMapping(value = { "details/{nhanvienByManvGui}" }, method = RequestMethod.GET)
	public String DanhsachYeucau(ModelMap modelMap,@PathVariable("nhanvienByManvGui") String nhanvienByManvGui) {
		modelMap.put("yeucaus", yeucauService.findByMaNhanVien(nhanvienByManvGui));
		return "admin/danhsachnv/details";
	}

//	
	@RequestMapping(value = "details/{username}/date", method = RequestMethod.GET)
	public String date(ModelMap modelMap) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
			Date from = simpleDateFormat.parse("08/10/2003");
			Date to = simpleDateFormat.parse("12/12/2023");
			modelMap.put("dates",yeucauService.findByDate(from, to));
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return "admin/danhsachnv/details";
	}


}
