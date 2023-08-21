    package com.teamsupercat.roupangbackend.controller;

    import com.teamsupercat.roupangbackend.common.ResponseDto;
    import com.teamsupercat.roupangbackend.service.RefreshTokenService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.web.bind.annotation.PostMapping;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RestController;

    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;

    @RestController
    @RequiredArgsConstructor
    @Slf4j
    @RequestMapping("/api/v1/refresh")
    public class RefreshController {

        private final RefreshTokenService refreshTokenService;

        @PostMapping
        public ResponseDto<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response)  {

            return refreshTokenService.RefreshAccessToken(request, response);

        }
    }