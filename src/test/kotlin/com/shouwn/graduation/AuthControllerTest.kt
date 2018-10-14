package com.shouwn.graduation

import com.fasterxml.jackson.databind.ObjectMapper
import com.shouwn.graduation.model.domain.dto.request.LoginRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@ExtendWith(SpringExtension::class)
@SpringBootTest
class AuthControllerTest @Autowired constructor(
        private val wac: WebApplicationContext
) {
    private lateinit var mvc: MockMvc

    @BeforeEach
    fun setup(){
        mvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build()
    }


    @Test
    fun loginTest() {
        val request = LoginRequest("tester", "test123")

        mvc.perform(post("/api/auth/signin")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.accessToken").isNotEmpty)
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
    }
}