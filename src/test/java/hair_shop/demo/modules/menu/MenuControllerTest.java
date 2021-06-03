package hair_shop.demo.modules.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.modules.menu.domain.Menu;
import hair_shop.demo.modules.menu.dto.request.RequestEditMenuName;
import hair_shop.demo.modules.menu.dto.request.RequestMenu;
import hair_shop.demo.modules.menu.dto.response.ResponseMenu;
import hair_shop.demo.modules.menu.repository.MenuRepository;
import hair_shop.demo.modules.menu.service.MenuService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuService menuService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void initMenu() {
        menuRepository.save(Menu.builder().name("BeforeMenu").price(10000).build());
    }

    @AfterEach
    void clearMenuRepo() {
        menuRepository.deleteAll();
    }

    @Test
    @DisplayName("메뉴 추가-성공")
    void addMenu() throws Exception {
        Menu menu = Menu.builder().name("testMenu").price(10000).build();
        String content = objectMapper.writeValueAsString(menu);
        mockMvc.perform(post("/menu")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());

        Optional<Menu> testMenu = menuRepository.findByName("testMenu");
        assertThat(testMenu.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("메뉴 추가-실패(메뉴중복)")
    void addMenu_fail() throws Exception {
        Menu menu = Menu.builder().name("BeforeMenu").price(2000).build();
        String content = objectMapper.writeValueAsString(menu);
        mockMvc.perform(post("/menu")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("메뉴 리스트받기")
    void getMenuList() throws Exception {
        List<ResponseMenu> menuList = menuService.allMenu();
        String content = objectMapper.writeValueAsString(menuList);

        mockMvc.perform(get("/menu"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().string(content))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("메뉴 가격수정-성공")
    void editMenuPrice() throws Exception {

        RequestMenu menu = new RequestMenu();
        menu.setName("BeforeMenu");
        menu.setPrice(5000);
        String content = objectMapper.writeValueAsString(menu);

        mockMvc.perform(put("/menu/price")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());

        Menu testMenu = menuRepository.findByName("BeforeMenu").get();

        assertThat(testMenu.getPrice()).isEqualTo(5000);
    }

    @Test
    @DisplayName("메뉴 가격수정-실패(가격 미입력)")
    void editMenuPrice_nullPrice_fail() throws Exception {

        RequestMenu menu = new RequestMenu();
        menu.setName("BeforeMenu");
        String content = objectMapper.writeValueAsString(menu);

        mockMvc.perform(put("/menu/price")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("메뉴 가격수정-실패(수정할 메뉴가 없음)")
    void editMenuPrice_notFoundMenu_fail() throws Exception {

        RequestMenu menu = new RequestMenu();
        menu.setName("BeforeMeu");
        menu.setPrice(1000);
        String content = objectMapper.writeValueAsString(menu);

        mockMvc.perform(put("/menu/price")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andDo(print())
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메뉴 이름수정-성공")
    void editMenuName() throws Exception {

        RequestEditMenuName requestEditMenuName = new RequestEditMenuName();
        requestEditMenuName.setNewName("editName");
        requestEditMenuName.setOriginName("BeforeMenu");
        String content = objectMapper.writeValueAsString(requestEditMenuName);

        mockMvc.perform(put("/menu/name")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());

        Optional<Menu> testMenu = menuRepository.findByName("editName");

        assertThat(testMenu.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("메뉴 이름수정-실패(변경할 메뉴 없음)")
    void editMenuName_notFoundMenu() throws Exception {
        RequestEditMenuName requestEditMenuName = new RequestEditMenuName();
        requestEditMenuName.setNewName("editName");
        requestEditMenuName.setOriginName("NotFoundMenu");
        String content = objectMapper.writeValueAsString(requestEditMenuName);

        mockMvc.perform(put("/menu/name")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("메뉴 이름수정-실패(변경할 이름이 이미 존재함)")
    void editMenuName_newNameDuplicate() throws Exception {
        menuRepository.save(Menu.builder().name("TEST").price(10000).build());

        RequestEditMenuName requestEditMenuName = new RequestEditMenuName();
        requestEditMenuName.setNewName("TEST");
        requestEditMenuName.setOriginName("BeforeMenu");
        String content = objectMapper.writeValueAsString(requestEditMenuName);

        mockMvc.perform(put("/menu/name")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().is4xxClientError());
    }
}