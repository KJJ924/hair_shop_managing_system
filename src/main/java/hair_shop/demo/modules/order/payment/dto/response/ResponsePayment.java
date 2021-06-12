package hair_shop.demo.modules.order.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/12
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(Include.NON_NULL)
public class ResponsePayment {

    private String memberName;
    private String phoneNumber;
    private String paymentType;
    private Integer holdingPoint;
    private Integer paymentAmount;
    private Integer remainingPoint;
}
