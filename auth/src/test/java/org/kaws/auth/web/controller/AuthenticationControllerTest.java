package org.kaws.auth.web.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kaws.AuthApplication;
import org.kaws.biz.AuthenticationBiz;
import org.kaws.controller.AuthenticationController;
import org.kaws.pojo.dto.AppKeyInfoDTO;
import org.kaws.pojo.dto.AuthenticationDTO;
import org.kaws.pojo.model.AppKeyInfo;
import org.kaws.service.AppKeyInfoService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import java.util.Map;
import java.util.function.BiFunction;


/**
 * @author Bosco
 * @date 2022/4/1 7:15 下午
 */


@SpringBootTest(classes = AuthApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Resource
    private AuthenticationController authenticationController;

    @Resource
    private AppKeyInfoService appKeyInfoService;

    @Resource
    private AuthenticationBiz authBiz;

    @Resource
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    public void register() throws Exception {
        AppKeyInfoDTO appKeyInfoDTO = new AppKeyInfoDTO();
        appKeyInfoDTO.setAppKey("HaoHao");
        appKeyInfoDTO.setRemark("Champion");

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appKeyInfoDTO)));

        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

    }


    @Test
    public void nonce() throws Exception {
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/auth/nonce")
                .accept(MediaType.APPLICATION_JSON)
                .param("appKey", "QiDianDong")
                .contentType(MediaType.TEXT_HTML));

        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void verify() throws Exception {
        BiFunction<AuthenticationDTO, String, String> createSign = (auth, appSecret) -> {
            String appSecretStr = appSecret + auth.getNonce();
            Map<String, Object> params = BeanUtil.beanToMap(auth, false, true);
            return params.keySet().stream().sorted()
                    .map(key -> key + "=" + params.get(key) + "&")
                    .reduce((x, y) -> x + y)
                    .map(content -> content.substring(0, content.length() - 1))
                    .map(content -> content.concat(appSecretStr))
                    .map(SecureUtil::sha256)
                    .map(String::toUpperCase)
                    .orElse(null);
        };


        AppKeyInfo entity = appKeyInfoService.getById(1);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/auth/nonce")
                .accept(MediaType.APPLICATION_JSON)
                .param("appKey", entity.getAppKey())
                .contentType(MediaType.TEXT_HTML))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Map map = objectMapper.readValue(content, Map.class);

        AuthenticationDTO auth = new AuthenticationDTO();
        auth.setAppKey(entity.getAppKey());
        auth.setTimestamp(System.currentTimeMillis() + 10 * 60 * 1000);
        auth.setNonce((Integer) map.get("data"));
        auth.setUrl("/auth/verify");


        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/auth/verify")
                .header("signature", createSign.apply(auth, entity.getAppSecret()))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(auth)));

        resultActions.andReturn().getResponse().setCharacterEncoding("UTF-8");
        resultActions.andExpect(MockMvcResultMatchers.status().isOk()).andDo(MockMvcResultHandlers.print());

    }


}