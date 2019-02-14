package info.ateh.budgetwebapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.exception.MemberNotFoundException;
import info.ateh.budgetwebapp.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.exceptions.base.MockitoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import java.util.Arrays;
import java.util.List;


import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MemberController.class)
public class MemberControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MemberService service;

    @Test
    public void all() throws Exception{
        Member member = new Member("MemberName");
        List<Member> all = Arrays.asList(member);
        given(service.getAllMembers()).willReturn(all);
        mvc.perform(get("/members")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(member.getName())));
    }

    @Test
    public void one() throws Exception{
        Member member = new Member("MemberName");
        member.setId(1L);
        given(service.getById(member.getId())).willReturn(member);
        mvc.perform(get("/members/{id}", member.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()))
                .andExpect(jsonPath("$.name", is(member.getName())));
    }

    @Test
    public void oneNotFound() throws Exception{
        Long[] id = {1L,0L,-1L};
        for (Long aLong : id) {
            given(service.getById(aLong)).willThrow(MemberNotFoundException.class);
            mvc.perform(get("/members/{id}", aLong)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    public void nameTaken() throws Exception{
        given(service.isNameTaken("TakenName")).willReturn(true);
        given(service.isNameTaken("FreeName")).willReturn(false);

        mvc.perform(get("/members/isNameTaken/{name}", "TakenName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(true)));

        mvc.perform(get("/members/isNameTaken/{name}", "FreeName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(false)));
    }

    @Test
    public void addNew() throws Exception{
        Member member = new Member("MemberName");
        member.setId(1L);
        String jsonMember = mapper.writeValueAsString(member);
        given(service.addNewMember(member)).willReturn(member);
        mvc.perform(post("/members/")
                .content(jsonMember)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()))
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
        Member memberOne = new Member("MemberOneName");
        Member memberTwo = new Member("MemberTwoName");
        memberOne.setId(1L);
        memberTwo.setId(2L);

        String jsonMemberTwo = mapper.writeValueAsString(memberTwo);

        Member memberThree = new Member(memberTwo.getName());
        memberThree.setId(memberOne.getId());

        given(service.replaceMember(memberTwo,memberOne.getId())).willReturn(memberThree);

        mvc.perform(put("/members/{id}", memberOne.getId())
                .content(jsonMemberTwo)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberOne.getId()))
                .andExpect(jsonPath("$.name", is(memberTwo.getName())));
    }

    @Test
    public void replaceIfNotExistMember() throws Exception{
        Member member = new Member("MemberName");
        member.setId(999L);

        String jsonMember = mapper.writeValueAsString(member);

        given(service.replaceMember(member,member.getId())).willReturn(member);

        mvc.perform(put("/members/{id}", member.getId())
                .content(jsonMember)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(member.getId()))
                .andExpect(jsonPath("$.name", is(member.getName())));
    }

    @Test(expected = MockitoException.class)
    public void replaceWrongId() throws Exception{
        Member member = new Member("MemberName");
        Member newMember = new Member("MemberToReplace");
        String jsonMember = mapper.writeValueAsString(newMember);

        given(service.replaceMember(member,0L)).willThrow(new NestedServletException("replaceWrongId test ok"));

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
    public void deleteTest() throws Exception{
        Member member = new Member("MemberName");
        member.setId(1L);

        mvc.perform(delete("/members/1",member.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
