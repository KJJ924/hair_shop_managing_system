package hair_shop.demo.designer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hair_shop.demo.modules.designer.domain.Designer;
import hair_shop.demo.modules.designer.dto.request.RequestDesigner;
import hair_shop.demo.modules.designer.repository.DesignerRepository;
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
class DesignerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DesignerRepository designerRepository;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void initData() {
        designerRepository.save(Designer.name("test"));
    }

    @AfterEach
    void clearData() {
        designerRepository.deleteAll();
    }

    @Test
    @DisplayName("디자이너 추가-성공")
    void addDesigner() throws Exception {
        RequestDesigner requestDesigner = new RequestDesigner();
        requestDesigner.setName("designerTest");
        String content = objectMapper.writeValueAsString(requestDesigner);
        mockMvc.perform(post("/designer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().isOk());

        Designer designerTest = designerRepository.findByName("designerTest").get();
        assertThat(designerTest).isNotNull();
    }

    @Test
    @DisplayName("디자이너 추가-실패(중복)")
    void addDesigner_fail_duplicate() throws Exception {
        RequestDesigner requestDesigner = new RequestDesigner();
        requestDesigner.setName("test");
        String content = objectMapper.writeValueAsString(requestDesigner);
        mockMvc.perform(post("/designer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(content))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("디자이너 목록 가져오기-성공")
    void getDesigner_list() throws Exception {
        mockMvc.perform(get("/designer"))
            .andDo(print())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}