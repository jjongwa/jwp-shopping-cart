package cart.controller;

import cart.entity.MemberEntity;
import cart.entity.ProductEntity;
import cart.service.AdminService;
import cart.service.MemberService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final AdminService adminService;

    private final MemberService memberService;

    public PageController(AdminService adminService, MemberService memberService) {
        this.adminService = adminService;
        this.memberService = memberService;
    }

    @GetMapping("/")
    public String allProducts(Model model) {
        return userAllProducts(model);
    }

    @GetMapping("/products")
    public String userAllProducts(Model model) {
        List<ProductEntity> productEntities = adminService.selectAllProducts();
        model.addAttribute("products", productEntities);
        return "index";
    }

    @GetMapping("/admin")
    public String adminAllProducts(Model model) {
        List<ProductEntity> productEntities = adminService.selectAllProducts();
        model.addAttribute("products", productEntities);
        return "admin";
    }

    @GetMapping("/settings")
    public String allUsers(Model model) {
        List<MemberEntity> memberEntities = memberService.selectAllMembers();
        model.addAttribute("members", memberEntities);
        return "settings";
    }

}
