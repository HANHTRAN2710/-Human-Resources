package com.demo.controller.admin;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.demo.entities.Nhanvien;
import com.demo.entities.Role;
import com.demo.helpers.FileHelper;
import com.demo.services.AccountService;
import com.demo.services.RoleService;

@Controller 
@RequestMapping({"admin/taikhoan", "account","", "/" })
public class AccountController {
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private AccountService accountService;

	@Autowired RoleService roleService;
	
	@RequestMapping(value = { "", "login", "/" }, method = RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error,
		@RequestParam(value = "logout", required = false) String logout,
		ModelMap modelMap) {
		if (error != null) {
			modelMap.put("msg", "Tai khoan khong hop le");
		}
		if(logout != null) {
			modelMap.put("msg", "Dang xuat thanh cong");
		}
		return "account/login";
	}
	
	@RequestMapping(value = { "register"}, method = RequestMethod.GET)
	public String register(ModelMap modelMap) {
	
		Nhanvien account = new Nhanvien();

		modelMap.put("nhanvien", account);
		modelMap.put("roles", roleService.findRole());
		return "admin/taikhoan/register";
	}
	
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public String register(@ModelAttribute("nhanvien") Nhanvien account, RedirectAttributes redirectAttributes,
			@RequestParam("file") MultipartFile file) {
		account.setKichhoat(true);
		account.setPassword(encoder.encode(account.getPassword()));
		try {
			if (file != null && file.getSize() > 0) {

				File folderimage = new File(new ClassPathResource(".").getFile().getPath() + "/static/images");
				String filename = FileHelper.generateFileName(file.getOriginalFilename());
				Path path = Paths.get(folderimage.getAbsolutePath() + File.separator + filename);
				System.out.println(folderimage.getAbsolutePath() + File.separator + filename);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				account.setHinhanh(filename);
			} else {
				account.setHinhanh("no-image.jpg");
			}
			if (accountService.save(account)) {
				redirectAttributes.addFlashAttribute("msg", "ok");
			} else {
				redirectAttributes.addFlashAttribute("msg", "Fail");
				return "redirect:/admin/taikhoan/register";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/admin/taikhoan/register";

	}


}
