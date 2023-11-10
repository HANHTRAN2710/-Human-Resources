package com.demo.controller.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.entities.Douutien;
import com.demo.entities.Nhanvien;
import com.demo.entities.Yeucau;
import com.demo.repositories.YeucauRepository;
import com.demo.services.AccountService;
import com.demo.services.DouutienService;
import com.demo.services.YeucauService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("admin/yeucau")
public class YeuCauController {
	
	@Autowired
	private YeucauRepository yeucauRepository;
	
	@Autowired
	private YeucauService yeucauService;
	
	@Autowired
	private DouutienService douutienService;
	
	@Autowired
	private AccountService nhanvienService;
	
//	cau 1e
	@RequestMapping(value = "yeucauu", method = RequestMethod.GET)
	public String findAll(ModelMap model) {
		model.put("yeucauss", yeucauService.findAll());
		model.put("priorities", StreamSupport.stream(douutienService.findAll().spliterator(), false).collect(Collectors.toList()));
		addNhanVienSupportToModelMap(model);
		return "admin/yeucau/yeucauu";
	}
	
	@GetMapping("filterByDate")
	private String filterByDate(
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			ModelMap model) throws ParseException {
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");

		List<Yeucau> yeucaus = yeucauService.findByDate(simp.parse(from), simp.parse(to));
		List<Douutien> douutiens = StreamSupport.stream(douutienService.findAll().spliterator(), false).collect(Collectors.toList());
		model.put("priorities", douutiens);
		model.put("yeucauss", yeucaus);
		addNhanVienSupportToModelMap(model);

		return "admin/yeucau/yeucauu";
	}

	@GetMapping("filterByPriority")
	private String filterByPriority(@RequestParam("priorityId") Integer priorityId, ModelMap model) {
		
		List<Yeucau> yeucaus = yeucauService.findByPriority(priorityId);
		List<Douutien> douutiens = StreamSupport.stream(douutienService.findAll().spliterator(), false).collect(Collectors.toList());
		
		model.put("priorities", douutiens);
		model.put("yeucauss", yeucaus);
		addNhanVienSupportToModelMap(model);

		return "admin/yeucau/yeucauu";
	}
	
	@GetMapping("filterByMaNhanVien")
	private String filterByMaNhanVien(@RequestParam("maNhanVien") String maNhanVien, ModelMap model) {
		
		List<Yeucau> yeucaus = yeucauService.findByMaNhanVien(maNhanVien);
		List<Douutien> douutiens = StreamSupport.stream(douutienService.findAll().spliterator(), false).collect(Collectors.toList());
		System.out.println(douutiens);
		model.put("priorities", douutiens);
		model.put("yeucauss", yeucaus);
		addNhanVienSupportToModelMap(model);
		return "admin/yeucau/yeucauu";
	}
	
	@PostMapping("assignTheMaNhanVienSupport")
	private String assignTheMaNhanVienSupport(Integer maYeuCau, String maNhanVienSupport, ModelMap model) {
		
		yeucauService.assignToNhanVienSupport(yeucauRepository.findById(maYeuCau).get(), maNhanVienSupport);
		model.put("yeucauss", yeucauService.findAll());
		model.put("priorities", StreamSupport.stream(douutienService.findAll().spliterator(), false).collect(Collectors.toList()));
		addNhanVienSupportToModelMap(model);
		 
		return "admin/yeucau/yeucauu";
	}
	
	private void addNhanVienSupportToModelMap(ModelMap modelMap) {
		modelMap.put("nvSupports", nhanvienService.findAllNhanVienSupport());
	}
	
}
