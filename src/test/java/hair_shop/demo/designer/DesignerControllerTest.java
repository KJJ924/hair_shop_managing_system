package hair_shop.demo.designer;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.designer.DesignerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DesignerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void initData(){
        designerRepository.save(Designer.builder().name("test").build());
    }

    @AfterEach
    void clearData(){
        designerRepository.deleteAll();
    }

    @Test
    @DisplayName("디자이너 추가-성공")
    void addDesigner() throws Exception {
        mockMvc.perform(post("/designer")
                .param("name","designerTest"))
                .andExpect(status().isOk());

        Designer designerTest = designerRepository.findByName("designerTest");
        assertThat(designerTest).isNotNull();
    }

    @Test
    @DisplayName("디자이너 추가-실패(중복)")
    void addDesigner_fail_duplicate() throws Exception {
        mockMvc.perform(post("/designer")
                .param("name","test"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("디자이너 목록 가져오기-성공")
    void getDesigner_list() throws Exception {
        mockMvc.perform(get("/designer"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("디자이너 목록 가져오기-실패(디자이너가 없음)")
    void getDesigner_list_fail() throws Exception {

        designerRepository.deleteAll();

        mockMvc.perform(get("/designer"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}