package hair_shop.demo.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.domain.Menu;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("메뉴 추가")
    void addMenu() throws Exception {
        Menu menu  = new Menu();
        menu.setPrice(10000);
        menu.setName("testMenu");
        String content = objectMapper.writeValueAsString(menu);
        mockMvc.perform(post("/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        Menu testMenu = menuRepository.findByName("testMenu");
        assertThat(testMenu).isNotNull();
    }

    @Test
    @DisplayName("메뉴 리스트받기")
    void getMenuList() throws Exception {
        List<Menu> menuList = menuRepository.findAll();
        String content = objectMapper.writeValueAsString(menuList);

        mockMvc.perform(get("/menu"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(content))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메뉴 가격수정")
    void editMenuPrice() throws Exception {
        Menu menu  = Menu.builder().price(10000).name("testMenu2").build();
        menuRepository.save(menu);

        mockMvc.perform(put("/menu/price/testMenu2")
                .param("price","5000"))
                .andExpect(status().isOk());

        Menu testMenu = menuRepository.findByName("testMenu2");

        assertThat(testMenu.getPrice()).isEqualTo(5000);
    }


}