package com.demo.controller.admin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.entities.Douutien;
import com.demo.entities.Nhanvien;
import com.demo.entities.Yeucau;
import com.demo.helpers.FileHelper;
import com.demo.repositories.AccountRepository;
import com.demo.repositories.DouutienRepository;
import com.demo.repositories.YeucauRepository;
import com.demo.services.AccountService;
import com.demo.services.DouutienService;
import com.demo.services.YeucauService;


// hinh nhu lam nham controler ha chi, cai nay la 
@Controller
@RequestMapping("nhanvien")
public class NhanvienController {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private YeucauRepository yeucauRepository;
	
	@Autowired
	private YeucauService yeucauService;
	
	@Autowired
	private DouutienRepository douutienRepository;
	
	@Autowired
	private DouutienService douutienService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	@GetMapping("index")
	public String index() {
		return "nhanvien/index";
	}
	
	
	@GetMapping("xemyeucau")
	public String xemyeucau(Authentication authentication, ModelMap modelMap) {
		if(getRole(authentication.getName()).equals("NHANVIEN")) {
			modelMap.put("yeucaus", yeucauService.findByMaNhanVien(authentication.getName()));			
		} else if (getRole(authentication.getName()).equals("NHANVIENSUPPORT")) {
			modelMap.put("yeucaus", yeucauService.findByMaNhanVienXuLy(authentication.getName()));			
		}
		addDouutien(modelMap);
		return "nhanvien/xemyeucau";
	}
	
	@GetMapping("xemyeucau/filterByDate")
	private String filterByDate(
			@RequestParam(value = "from", required = false) String from,
			@RequestParam(value = "to", required = false) String to,
			Authentication authentication,
			ModelMap model) throws ParseException {
		SimpleDateFormat simp = new SimpleDateFormat("yyyy-MM-dd");

		List<Yeucau> yeucaus = yeucauService
				.findByDate(simp.parse(from), simp.parse(to))
				.stream()
				.filter(yc -> {
					if(getRole(authentication.getName()).equals("NHANVIEN")) {
						yc.getNhanvienByManvGui().getUsername().equals(authentication.getName());
						return true;
					} else if (getRole(authentication.getName()).equals("NHANVIENSUPPORT")) {
						if(yc.getNhanvienByManvXuly() != null) {
							return yc.getNhanvienByManvXuly().getUsername().equals(authentication.getName());			
						}
						return false;
					}
					return false;
				})
				.toList();;
		model.put("yeucaus", yeucaus);
		addDouutien(model);

		return "nhanvien/xemyeucau";
	}

	@GetMapping("xemyeucau/filterByPriority")
	private String filterByPriority(
			@RequestParam("priorityId") Integer priorityId,
			Authentication authentication,
			ModelMap model) {
		
		List<Yeucau> yeucaus = yeucauService
				.findByPriority(priorityId)
				.stream()
				.filter(yc -> {
					if(getRole(authentication.getName()).equals("NHANVIEN")) {
						yc.getNhanvienByManvGui().getUsername().equals(authentication.getName());
						return true;
					} else if (getRole(authentication.getName()).equals("NHANVIENSUPPORT")) {
						if(yc.getNhanvienByManvXuly() != null) {
							return yc.getNhanvienByManvXuly().getUsername().equals(authentication.getName());			
						}
						return false;
					}
					return false;
				})
				.toList();
		addDouutien(model);
		model.put("yeucaus", yeucaus);

		return "nhanvien/xemyeucau";
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
	
	@GetMapping("guiyeucau")
	public String guiyeucau(ModelMap modelMap) {
		modelMap.put("priorities", douutienRepository.findAll());
		return "nhanvien/guiyeucau";
	}
	
	
	@PostMapping("guiyeucau")
	public String guiyeucau(
			Yeucau yeucau,
			ModelMap modelMap,
			Authentication authentication,
			RedirectAttributes redirectAttributes) {
		yeucau.setNhanvienByManvGui(accountService.find(authentication.getName()));
		yeucau.setNgaygui(new Date());
		yeucauRepository.save(yeucau);
		
		modelMap.put("priorities", douutienRepository.findAll());
		redirectAttributes.addFlashAttribute("msg", "Gui yeu cau thanh cong!");
		return "redirect:/nhanvien/xemyeucau";
	}
	
	@RequestMapping(value = { "update/{username}" }, method = RequestMethod.GET)
	public String update(ModelMap modelMap, @PathVariable("username") String username, Authentication authentication) {
		username = authentication.getName();
		Nhanvien nv = accountService.find(username);
		modelMap.put("nhanvien", nv);
		return "nhanvien/capnhat";

	}

	@RequestMapping(value = { "update" }, method = RequestMethod.POST)
	public String update(@ModelAttribute("nhanvien") Nhanvien nv, RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file) {
		try {
			if (file != null && file.getSize() > 0) {
				File folderImage = new File(new ClassPathResource(".").getFile().getPath() + "/static/images");
				String fileName = FileHelper.generateFileName(file.getOriginalFilename());
				Path path = Paths.get(folderImage.getAbsolutePath() + File.separator + fileName);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				nv.setHinhanh(fileName);
			} else {
				nv.setHinhanh("no-image.jpg");
			}

			//
			if (nv.getPassword().isEmpty()) {
				nv.setPassword(accountService.getpassword(nv.getUsername()));
			} else
				nv.setPassword(encoder.encode(nv.getPassword()));

			if (accountService.save(nv)) {
				redirectAttributes.addFlashAttribute("msg", "ok");

			} else {
				redirectAttributes.addFlashAttribute("msg", "fail");
				return "nhanvien/capnhat";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/nhanvien/index";

	}

	private void setHinhAnhNhanVien(MultipartFile file, Nhanvien nv) throws IOException {
		if (file != null && file.getSize() > 0) {
			File folderImage = new File(new ClassPathResource(".").getFile().getPath() + "/static/images");
			String fileName = FileHelper.generateFileName(file.getOriginalFilename());
			Path path = Paths.get(folderImage.getAbsolutePath() + File.separator + fileName);
			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			nv.setHinhanh(fileName);
		} else {
			if(nv.getHinhanh() == null) {
				nv.setHinhanh("no-image.jpg");				
			} else {
				nv.setHinhanh(nv.getHinhanh());
			}
		}
	}
	
	private void setMatKhauNhanVien(Nhanvien nv) {
		if (nv.getPassword().isEmpty()) {
			nv.setPassword(accountService.getpassword(nv.getUsername()));
		} else
			nv.setPassword(encoder.encode(nv.getPassword()));
	}
	
}
