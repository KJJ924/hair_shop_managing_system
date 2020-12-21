package hair_shop.demo.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.domain.Menu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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

    @BeforeEach
    void initMenu(){
        menuRepository.save(Menu.builder().name("BeforeMenu").price(10000).build());
    }
    @AfterEach
    void clearMenuRepo(){
        menuRepository.deleteAll();
    }

    @Test
    @DisplayName("메뉴 추가-성공")
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
    @DisplayName("메뉴 추가-실패")
    void addMenu_fail() throws Exception {
        Menu menu  = new Menu();
        menu.setPrice(2000);
        menu.setName("BeforeMenu");
        String content = objectMapper.writeValueAsString(menu);
        mockMvc.perform(post("/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
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
    @DisplayName("메뉴 가격수정-성공")
    void editMenuPrice() throws Exception {

        mockMvc.perform(put("/menu/price/BeforeMenu")
                .param("price","5000"))
                .andExpect(status().isOk());

        Menu testMenu = menuRepository.findByName("BeforeMenu");

        assertThat(testMenu.getPrice()).isEqualTo(5000);
    }

    @Test
    @DisplayName("메뉴 가격수정-실패(가격 미입력)")
    void editMenuPrice_nullPrice_fail() throws Exception {
        mockMvc.perform(put("/menu/price/BeforeMenu")
                .param("price",""))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 가격수정-실패(수정할 메뉴가 없음)")
    void editMenuPrice_notFoundMenu_fail() throws Exception {
        mockMvc.perform(put("/menu/price/NotFoundMenu")
                .param("price","20000"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 이름수정-성공")
    void editMenuName() throws Exception {

        mockMvc.perform(put("/menu/name/BeforeMenu")
                .param("newName","editName"))
                .andExpect(status().isOk());

        Menu testMenu = menuRepository.findByName("editName");

        assertThat(testMenu).isNotNull();
    }

    @Test
    @DisplayName("메뉴 이름수정-실패(변경할 메뉴 없음)")
    void editMenuName_notFoundMenu() throws Exception {
        mockMvc.perform(put("/menu/name/asdasdasdw")
                .param("newName","editName"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("메뉴 이름수정-실패(변경할 이름이 이미 존재함)")
    void editMenuName_newNameDuplicate() throws Exception {
        menuRepository.save(Menu.builder().name("TEST").price(10000).build());

        mockMvc.perform(put("/menu/name/BeforeMenu")
                .param("newName","TEST"))
                .andExpect(status().isBadRequest());
    }
}