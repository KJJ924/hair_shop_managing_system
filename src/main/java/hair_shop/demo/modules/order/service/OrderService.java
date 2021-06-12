package hair_shop.demo.modules.order.service;

import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.designer.service.DesignerService;
import hair_shop.demo.modules.member.domain.Member;
import hair_shop.demo.modules.member.service.MemberService;
import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.menu.service.MenuService;
import hair_shop.demo.modules.order.domain.OrderTable;
import hair_shop.demo.modules.order.dto.MonthData;
import hair_shop.demo.modules.order.dto.request.RequestOrder;
import hair_shop.demo.modules.order.dto.request.RequestOrderMenuEdit;
import hair_shop.demo.modules.order.dto.request.RequestOrderTimeEdit;
import hair_shop.demo.modules.order.dto.request.RequestPayment;
import hair_shop.demo.modules.order.dto.response.ResponseOrder;
import hair_shop.demo.modules.order.exception.NotFoundOrderException;
import hair_shop.demo.modules.order.exception.PaidReservationException;
import hair_shop.demo.modules.order.exception.TimeOverReservationStartException;
import hair_shop.demo.modules.order.payment.dto.response.ResponsePayment;
import hair_shop.demo.modules.order.payment.service.PaymentService;
import hair_shop.demo.modules.order.repository.OrderRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    private final MenuService menuService;
    private final DesignerService designerService;
    private final MemberService memberService;
    private final PaymentService paymentService;

    public ResponseOrder saveOrder(RequestOrder requestOrder) {
        Designer designer = designerService.findByName(requestOrder.getDesignerName());
        Member member = memberService.findByPhone(requestOrder.getMemberPhoneNumber());
        Menu menu = menuService.getMenu(requestOrder.getMenuName());

        if (requestOrder.isAfter()) {
            throw new TimeOverReservationStartException();
        }

        OrderTable order = OrderTable.builder()
            .designers(designer)
            .member(member)
            .reservationStart(requestOrder.getReservationStart())
            .reservationEnd(requestOrder.getReservationEnd())
            .build();
        order.menuAdd(menu);

        member.addOrder(orderRepository.save(order));

        return ResponseOrder.toMapper(order);
    }

    public ResponseOrder getOrder(Long orderId) {
        OrderTable order = findByOrderId(orderId);
        return ResponseOrder.toMapper(order);
    }

    public List<MonthData> getMonthData(LocalDate from, LocalDate to) {
        List<OrderTable> orderList = orderRepository.findByMonthDate(from, to);
        Map<Integer, List<OrderTable>> daySeparated = OrderTable.daySeparated(orderList);
        return MonthData.remakeMonthData(daySeparated);
    }

    public Map<Integer, List<OrderTable>> getWeekData(LocalDate from, LocalDate to) {
        List<OrderTable> orderList = orderRepository
            .findByCreateAtBetweenOrderByCreateAt(from, to);
        return OrderTable.daySeparated(orderList);
    }

    public ResponsePayment payment(RequestPayment requestPayment) {
        OrderTable order = findByOrderId(requestPayment.getOrderId());
        return paymentService.paymentFactory(requestPayment, order);
    }

    public ResponseOrder editTime(RequestOrderTimeEdit form) {
        if (form.isAfter()) {
            throw new TimeOverReservationStartException();
        }

        OrderTable order = findByOrderId(form.getId());

        LocalDateTime start = form.getReservationStart();
        LocalDateTime end = form.getReservationEnd();

        order.changeReservationTime(start, end);

        return ResponseOrder.toMapper(order);
    }

    public OrderTable findByOrderId(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(NotFoundOrderException::new);
    }

    public ResponseOrder deleteMenu(RequestOrderMenuEdit requestOrderMenuEdit) {
        OrderTable order = findByOrderId(requestOrderMenuEdit.getOrderId());
        order.menuDelete(menuService.getMenu(requestOrderMenuEdit.getMenuName()));
        return ResponseOrder.toMapper(order);
    }

    public ResponseOrder addMenu(RequestOrderMenuEdit requestOrderMenuEdit) {
        OrderTable order = findByOrderId(requestOrderMenuEdit.getOrderId());
        order.menuAdd(menuService.getMenu(requestOrderMenuEdit.getMenuName()));
        return ResponseOrder.toMapper(order);
    }

    public void deleteOrder(Long orderId) {
        OrderTable order = findByOrderId(orderId);
        if(order.checkPayment()){
            throw new PaidReservationException();
        }
        orderRepository.delete(order);
    }
}
