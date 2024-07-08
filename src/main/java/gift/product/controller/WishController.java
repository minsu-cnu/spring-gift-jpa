package gift.product.controller;

import gift.product.dto.LoginMember;
import gift.product.dto.WishDto;
import gift.product.model.Wish;
import gift.product.service.WishService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<List<Wish>> getWishAll(HttpServletRequest request) {
        LoginMember loginMember = getLoginMember(request);
        List<Wish> wishAll = wishService.getWishAll(loginMember);
        return ResponseEntity.ok(wishAll);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wish> getWish(@PathVariable(name = "id") Long id,
        HttpServletRequest request) {
        LoginMember loginMember = getLoginMember(request);
        Wish wish = wishService.getWish(id, loginMember);
        return ResponseEntity.ok(wish);
    }

    @PostMapping("/insert")
    public ResponseEntity<Wish> insertProduct(@Valid @RequestBody WishDto wishDto,
        HttpServletRequest request) {
        LoginMember loginMember = getLoginMember(request);
        Wish responseWish = wishService.insertWish(wishDto, loginMember);

        return ResponseEntity.ok(responseWish);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteWish(@PathVariable(name = "id") Long id,
        HttpServletRequest request) {
        LoginMember loginMember = getLoginMember(request);
        wishService.deleteWish(id, loginMember);

        return ResponseEntity.ok().build();
    }

    private LoginMember getLoginMember(HttpServletRequest request) {
        return new LoginMember((Long) request.getAttribute("id"));
    }
}
