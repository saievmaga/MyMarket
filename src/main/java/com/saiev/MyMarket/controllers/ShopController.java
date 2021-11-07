package com.saiev.MyMarket.controllers;

import com.saiev.MyMarket.entities.OrderItem;
import com.saiev.MyMarket.entities.Product;
import com.saiev.MyMarket.entities.User;
import com.saiev.MyMarket.repositories.specifications.ProductSpecs;
import com.saiev.MyMarket.services.*;
import com.saiev.MyMarket.utils.Order;
import com.saiev.MyMarket.utils.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/shop")
public class ShopController {
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    private UserService userService;
    private ProductService productService;
    private ShoppingCartService shoppingCartService;

    //    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
//        this.rabbitTemplate = rabbitTemplate;
//    }
    @Autowired
    public void setSendMessageService(SendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String shopPage(Model model,
                           @RequestParam(value = "page") Optional<Integer> page,
                           @RequestParam(value = "word", required = false) String word,
                           @RequestParam(value = "min", required = false) Double min,
                           @RequestParam(value = "max", required = false) Double max
    ) {
        final int currentPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Specification<Product> spec = Specification.where(null);
        StringBuilder filters = new StringBuilder();
        if (word != null) {
            spec = spec.and(ProductSpecs.titleContains(word));
            filters.append("&word=" + word);
        }
        if (min != null) {
            spec = spec.and(ProductSpecs.priceGreaterThanOrEq(min));
            filters.append("&min=" + min);
        }
        if (max != null) {
            spec = spec.and(ProductSpecs.priceLesserThanOrEq(max));
            filters.append("&max=" + max);
        }

        Page<Product> products = productService.getProductsWithPagingAndFiltering(currentPage, PAGE_SIZE, spec);

        model.addAttribute("products", products.getContent());
        model.addAttribute("page", currentPage);
        model.addAttribute("totalPage", products.getTotalPages());

        model.addAttribute("filters", filters.toString());

        model.addAttribute("min", min);
        model.addAttribute("max", max);
        model.addAttribute("word", word);


        return "shop-page";
    }

    @GetMapping("/cart/add/{id}")
    public String addProductToCart(Model model, @PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        shoppingCartService.addToCart(httpServletRequest.getSession(), id);
        String referrer = httpServletRequest.getHeader("referer");

        return "redirect:" + referrer;
    }

    private final static String QUEUE_NAME = "orderQueue";

    @GetMapping("/order/fill")
    public String placeOrder(Model model, HttpServletRequest httpServletRequest) {
        HttpSession httpSession = httpServletRequest.getSession();
        String userName = httpServletRequest.getUserPrincipal().getName();
        double totalCost = shoppingCartService.getTotalCost(httpSession);
        String msg = String.format("New order created. User = %s, total cost = %.2f", userName, totalCost);

        ShoppingCart cart = shoppingCartService.getCurrentCart(httpSession);
        User user = (User) httpSession.getAttribute("user");
        List<OrderItem> orderItems = cart.getItems();
        double price = cart.getTotalCost();

        Order order = new Order(user, orderItems, price);
        model.addAttribute("order", order);

        sendMessageService.sendMessage(QUEUE_NAME, msg);

        return "order-result";
    }
}
