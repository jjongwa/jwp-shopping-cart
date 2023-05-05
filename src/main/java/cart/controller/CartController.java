package cart.controller;

import cart.auth.AuthInfo;
import cart.auth.BasicAuthorizationExtractor;
import cart.entity.CartItemEntity;
import cart.entity.ProductEntity;
import cart.service.CartItemService;
import cart.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CartController {

    private final BasicAuthorizationExtractor basicAuthorizationExtractor = new BasicAuthorizationExtractor();

    private final CartItemService cartItemService;
    private final MemberService memberService;

    public CartController(CartItemService cartItemService, MemberService memberService) {
        this.cartItemService = cartItemService;
        this.memberService = memberService;
    }

    @GetMapping("/cart")
    public String getCartItem() {
        return "cart";
    }

    @GetMapping("/cart/load")
    @ResponseBody
    public ResponseEntity<List<ProductEntity>> getCartItem(Model model, HttpServletRequest request) {
        AuthInfo authInfo = basicAuthorizationExtractor.extract(request);

        int memberId = memberService.findMemberId(authInfo.getEmail(), authInfo.getPassword());
        List<ProductEntity> cartItems = cartItemService.getCartItems(memberId);

        model.addAttribute("cartItems", cartItems);
        return ResponseEntity.ok().body(cartItems);
    }

    @PostMapping("/cart/{productId}")
    public ResponseEntity<Void> insertCartItem(HttpServletRequest request, @PathVariable int productId) {
        AuthInfo authInfo = basicAuthorizationExtractor.extract(request);

        int memberId = memberService.findMemberId(authInfo.getEmail(), authInfo.getPassword());
        cartItemService.addCartItem(new CartItemEntity(memberId, productId));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable int cartId) {
        cartItemService.deleteCartItem(cartId);
        return ResponseEntity.ok().build();
    }

}
