package hair_shop.demo.order;

import hair_shop.demo.designer.DesignerRepository;
import hair_shop.demo.domain.Designer;
import hair_shop.demo.domain.Member;
import hair_shop.demo.domain.Menu;
import hair_shop.demo.domain.OrderTable;
import hair_shop.demo.member.MemberRepository;
import hair_shop.demo.member.MemberService;
import hair_shop.demo.menu.MenuRepository;
import hair_shop.demo.order.form.OrderForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final DesignerRepository designerRepository;

    public void saveOrder(OrderForm orderForm) {
        OrderTable order = makeOrder(orderForm);
        orderRepository.save(order);
    }

    private OrderTable makeOrder(OrderForm orderForm){
        Designer designer =designerRepository.findByName(orderForm.getDesignerName());
        Member member = memberRepository.findByPhone(orderForm.getMemberPhoneNumber());
        Menu menu= menuRepository.findByName(orderForm.getMenuName());
        HashSet<Menu> menus = new HashSet<>();
        menus.add(menu);

        return OrderTable.builder()
                .menus(menus)
                .designers(designer)
                .member(member)
                .reservationStart(orderForm.getReservationStart())
                .reservationEnd(orderForm.getReservationEnd())
                .build();
    }
}
