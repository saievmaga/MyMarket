package com.saiev.MyMarket.controllers;

import com.saiev.MyMarket.entities.Product;
import com.saiev.MyMarket.entities.SystemUser;
import com.saiev.MyMarket.entities.User;
import com.saiev.MyMarket.services.CategoryService;
import com.saiev.MyMarket.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @GetMapping
    public String showAdminDashboard() {
        return "admin-panel";
    }

}
