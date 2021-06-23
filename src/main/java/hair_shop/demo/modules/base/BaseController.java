package hair_shop.demo.modules.base;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author dkansk924@naver.com
 * @since 2021/06/21
 */

@Controller
public class BaseController {

    @GetMapping("/")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
