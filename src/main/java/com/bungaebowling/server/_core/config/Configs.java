package com.bungaebowling.server._core.config;

import java.util.Collections;
import java.util.List;

public class Configs {
    public final static List<String> CORS = Collections.unmodifiableList(
            List.of("http://localhost:3000", // 리액트 개발용 3000포트
                        "*")  // 모든 IP 주소 허용 (나중에 프론트 앤드 IP만 허용하게 바꿔야함)
    );
}
