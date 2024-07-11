package gift.product.service;

import gift.product.dto.LoginMember;
import gift.product.dto.WishDto;
import gift.product.model.Member;
import gift.product.model.Product;
import gift.product.model.Wish;
import gift.product.repository.AuthRepository;
import gift.product.repository.WishRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;
    private final AuthRepository authRepository;

    public WishService(WishRepository wishRepository, ProductService productService, AuthRepository authRepository) {
        this.wishRepository = wishRepository;
        this.productService = productService;
        this.authRepository = authRepository;
    }

    public List<Wish> getWishAll(LoginMember loginMember) {
        return wishRepository.findAllByMemberId(loginMember.id());
    }

    public Wish getWish(Long id, LoginMember loginMember) {
        return getValidatedWish(id, loginMember);
    }

    @Transactional
    public Wish insertWish(WishDto wishDto, LoginMember loginMember) {
        Product product = productService.getProduct(wishDto.productId());

        Member member = getMember(loginMember);

        Wish wish = new Wish(member, product);
        return wishRepository.save(wish);
    }

    @Transactional
    public void deleteWish(Long id, LoginMember loginMember) {
        getValidatedWish(id, loginMember);
        wishRepository.deleteById(id);
    }

    private Member getMember(LoginMember loginMember) {
        Optional<Member> member = authRepository.findById(loginMember.id());

        if (member.isEmpty()) {
            throw new NoSuchElementException("회원 정보가 존재하지 않습니다.");
        }

        return member.get();
    }

    private Wish getValidatedWish(Long id, LoginMember loginMember) {
        Optional<Wish> wishOptional = wishRepository.findByIdAndMemberId(id, loginMember.id());

        if (wishOptional.isEmpty()) {
            throw new NoSuchElementException("해당 ID의 위시 항목이 위시리스트에 존재하지 않습니다.");
        }

        return wishOptional.get();
    }
}
