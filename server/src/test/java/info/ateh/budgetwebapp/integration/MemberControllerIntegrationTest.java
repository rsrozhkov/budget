package info.ateh.budgetwebapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.ateh.budgetwebapp.BudgetWebApplication;
import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BudgetWebApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MemberService memberService;

    @Test
    public void all() throws Exception{
        List<Member> all = memberService.getAllMembers();
        mvc.perform(get("/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name", is(all.get(0).getName())));
    }

    @Test
    public void one() throws Exception{
        Member member = memberService.addNewMember(new Member("One"));
        mvc.perform(get("/members/{id}", member.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(member.getName())));
    }

    @Test
    public void oneNotFound() throws Exception{
        mvc.perform(get("/members/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void nameTaken() throws Exception{
        Member member = memberService.addNewMember(new Member("nameTakenTrue"));
        mvc.perform(get("/members/isNameTaken/{name}", member.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));

        mvc.perform(get("/members/isNameTaken/{name}", "nameTakenFalse")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    public void addNew() throws Exception{
        Member member = new Member("addNew");
        String jsonMember = mapper.writeValueAsString(member);

        mvc.perform(post("/members/")
                .content(jsonMember)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(member.getName())));
    }

    @Test
    public void addNewEmpty() throws Exception{
        mvc.perform(post("/members/")
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void replace() throws Exception{
        Member member = memberService.addNewMember(new Member("replace"));
        Long id = member.getId();

        Member newMember = new Member("MemberToReplace");
        String jsonMember = mapper.writeValueAsString(newMember);

        mvc.perform(put("/members/{id}", id)
                .content(jsonMember)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name", is(newMember.getName())));
    }

    @Test
    public void replaceIfNotExistMember() throws Exception{
        Member member = new Member("replaceIfNotExistMember");
        member.setId(9999L);
        String jsonMember = mapper.writeValueAsString(member);
        mvc.perform(put("/members/{id}", member.getId())
                .content(jsonMember)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(member.getName())));
    }

    @Test(expected = NestedServletException.class)
    public void replaceWrongId() throws Exception{
        Member newMember = new Member("MemberToReplace");
        String jsonMember = mapper.writeValueAsString(newMember);

        mvc.perform(put("/members/{id}", 0L)
                .content(jsonMember)
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void replaceEmptyMember() throws Exception{
        mvc.perform(put("/members/{id}", 1L)
                .content("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteMember() throws Exception{
        Member member = new Member("deleteMember");
        mvc.perform(delete("/members/{id}", member.getId())
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test (expected = NestedServletException.class)
    public void deleteMemberHasTransactions() throws Exception{
        mvc.perform(delete("/members/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON));
    }

}
