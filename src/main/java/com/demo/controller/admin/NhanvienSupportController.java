package com.demo.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.entities.Douutien;
import com.demo.entities.Nhanvien;
import com.demo.entities.Yeucau;
import com.demo.services.AccountService;
import com.demo.services.DouutienService;
import com.demo.services.YeucauService;

@Controller
@RequestMapping("nhanviensupport")
public class NhanvienSupportController {
	
	@Autowired
	private YeucauService yeucauService;
	
	@Autowired
	private AccountService nhanvienService;
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private DouutienService douutienService;

	@GetMapping("index")
	public String index(ModelMap modelMap) {
		modelMap.put("yeucaunvsp",yeucauService.findAll());
		return "nhanviensupport/index";
	}
	@GetMapping("xemyeucau")
	public String xemyeucau(Authentication authentication, ModelMap modelMap) {
		if(getRole(authentication.getName()).equals("NHANVIEN")) {
			modelMap.put("yeucaus", yeucauService.findByMaNhanVien(authentication.getName()));			
		} else if (getRole(authentication.getName()).equals("NHANVIENSUPPORT")) {
			modelMap.put("yeucaus", yeucauService.findByMaNhanVienXuLy(authentication.getName()));			
		}
		addDouutien(modelMap);
		return "nhanviensupport/xemyeucau";
	}
	private String getRole(String username) {
		String role = null;
		Nhanvien nv = accountService.find(username);
		if(nv != null) {
			role = nv.getRole().getName();
		}
		return role;
	}
	private void addDouutien(ModelMap modelMap) {
		List<Douutien> douutiens = StreamSupport
				.stream(douutienService.findAll().spliterator(), false)
				.collect(Collectors.toList());
		
		modelMap.put("priorities", douutiens);
	}
	
	
	@GetMapping("filterByDate")
	private String filterByDate(
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			ModelMap model) throws ParseException {
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");

		List<Yeucau> yeucaunvsp = yeucauService.findByDate(simp.parse(from), simp.parse(to));
		List<Douutien> douutiens = StreamSupport.stream(douutienService.findAll().spliterator(), false).collect(Collectors.toList());
		model.put("priorities", douutiens);
		model.put("yeucaunvsp", yeucaunvsp);
		addNhanVienSupportToModelMap(model);

		return "nhanviensupport/index";
	}
	
	@GetMapping("filterByPriority")
	private String filterByPriority(@RequestParam("priorityId") Integer priorityId, ModelMap model) {
		
		List<Yeucau> yeucaunvsp = yeucauService.findByPriority(priorityId);
		List<Douutien> douutiens = StreamSupport.stream(douutienService.findAll().spliterator(), false).collect(Collectors.toList());
		System.out.println(douutiens);
		model.put("priorities", douutiens);
		model.put("yeucaunvsp", yeucaunvsp);
		addNhanVienSupportToModelMap(model);

		return "nhanviensupport/index";
	}
	
	private void addNhanVienSupportToModelMap(ModelMap modelMap) {
		modelMap.put("nvSupports", nhanvienService.findAllNhanVienSupport());
	}
	

}
