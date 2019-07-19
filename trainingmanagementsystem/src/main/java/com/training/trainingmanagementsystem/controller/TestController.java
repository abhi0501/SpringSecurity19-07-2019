package com.training.trainingmanagementsystem.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.training.trainingmanagementsystem.bean.AddSubject;
import com.training.trainingmanagementsystem.bean.AppRole;
import com.training.trainingmanagementsystem.bean.AppUser;
import com.training.trainingmanagementsystem.bean.Subject;
import com.training.trainingmanagementsystem.bean.User;
import com.training.trainingmanagementsystem.bean.UserRole;
import com.training.trainingmanagementsystem.dao.AddSubjectDAO;
import com.training.trainingmanagementsystem.dao.AppRoleRepository;
import com.training.trainingmanagementsystem.dao.AppUserRepository;
import com.training.trainingmanagementsystem.dao.UserRoleRepository;
import com.training.trainingmanagementsystem.repository.dao.SubjectDescriptionDao;

@Controller
public class TestController {
	
	
	@Autowired
	AppRoleRepository appRoleRepository;
	
	@Autowired
	AppUserRepository appUserRepository;
	
	@Autowired
	UserRoleRepository userRoleRepository;
	
	@Autowired
	SubjectDescriptionDao SubjectDescriptionDao;
	
	@Autowired
	AddSubjectDAO addSubjectDAO;
	
@GetMapping(value="/")
public String defaultPage()
	{
		return "index";
	}
	
	@GetMapping(value="/hello")
	
	public String print()
	{
		return "hello";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
 
        //model.setViewName("loginPage.jsp");
        return  "loginPage";
    }
	
	
	@RequestMapping(value = "/403", method = RequestMethod.GET)
    public String loginPage3(Model model) {
 
        //model.setViewName("loginPage.jsp");
        return  "error";
    }
	
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerPage(Model model) {
 
        //model.setViewName("loginPage.jsp");
        return  "register";
    }
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerPagePost(@ModelAttribute User user) {
 
        //model.setViewName("loginPage.jsp");
		AppRole role=new AppRole();
		
		AppUser users=new AppUser();
		
		users.setUserName(user.getUsername());
		users.setEnabled(0);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		users.setEncrytedPassword(hashedPassword);
		UserRole userRole=new UserRole();
		
			//role.setRoleName(appRoleRepository.findByRoleName("ROLE_"+user.getRole_name().toUpperCase()).get().getRoleName());
			appRoleRepository.saveAndFlush(appRoleRepository.findByRoleName("ROLE_"+user.getRole_name().toUpperCase()).get());	
			userRole.setAppRole(appRoleRepository.findByRoleName("ROLE_"+user.getRole_name().toUpperCase()).get().getRoleId());
		
			
		appUserRepository.save(users);
		
		
		
		
		userRole.setAppUser(users);
		userRoleRepository.save(userRole);
		
		
		
		
		
        return  "register";
    }
	
	@RequestMapping(value = "/userlogin", method = RequestMethod.GET)
    public ModelAndView userlogin(ModelAndView model,HttpSession sesion) {
		if( (String)sesion.getAttribute("username")!=null && ((String)sesion.getAttribute("role")).equalsIgnoreCase("ROLE_USER"))
		{
		model.setViewName("errorPage");	
		return model;
		}
		
		sesion.setAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
      sesion.setAttribute("role",(appRoleRepository.findById(appUserRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId()).get().getRoleName()));
		List<Subject> lists=  SubjectDescriptionDao.getAllSubject(); 
       
		model.addObject("subjectlist",lists);
		model.setViewName("loginSuccessUser");
		
		
        
        return  model;
    }
	
	@RequestMapping(value = "/adminlogin", method = RequestMethod.GET)
    public ModelAndView adminlogin(ModelAndView model,HttpSession sesion) {
   
		if( (String)sesion.getAttribute("username")!=null && ((String)sesion.getAttribute("role")).equalsIgnoreCase("ROLE_ADMIN"))
		{
			System.out.println(sesion.getAttribute("username"));
		model.setViewName("errorPage");	
		return model;
		}
		
		sesion.setAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
		List<AddSubject> subjects= addSubjectDAO.findAll();
		sesion.setAttribute("role",(appRoleRepository.findById(appUserRepository.findByUserName(SecurityContextHolder.getContext().getAuthentication().getName()).getUserId()).get().getRoleName()));
		
		
        model.setViewName("loginSuccessAdmin");
        model.addObject("subjects",subjects);
        return  model;
    }
	
	@RequestMapping(value = "/addSubject", method = RequestMethod.GET)
    public ModelAndView addSubject(@RequestParam("id") int id, ModelAndView model,HttpSession session) {
 
		AddSubject addSubject=new AddSubject();
		addSubject.setSubjectId(id);
		addSubject.setUserName((String)session.getAttribute("username"));
        System.out.println(SubjectDescriptionDao.checkSubjectId(id));
		if(!SubjectDescriptionDao.checkSubjectId(id))
		{
		addSubjectDAO.save(addSubject);
		model.setViewName("addSubjectSuccess");
		return  model;
		}
		else
		{
			
			List<Subject> lists=  SubjectDescriptionDao.getAllSubject(); 
		    model.addObject("subjectlist",lists);
			model.setViewName("loginSuccessUser");
			return  model;
		}
        
    }
	
	
	@RequestMapping(value = "/deleteSubject", method = RequestMethod.GET)
    public ModelAndView deleteSubject(@RequestParam("id") int id, ModelAndView model,HttpSession session) {
 
		addSubjectDAO.deleteById(id);
		model.setViewName("deleteSubject");
		return model;
    }
	
	@RequestMapping(value = "/deleteUserSubject", method = RequestMethod.GET)
    public ModelAndView deleteUserSubject(@RequestParam("id") int id, ModelAndView model,HttpSession session) {
 
		addSubjectDAO.deleteById(id);
		model.setViewName("deleteUserSubject");
		return model;
    }

}
