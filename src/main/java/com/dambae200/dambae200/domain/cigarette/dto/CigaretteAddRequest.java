package com.dambae200.dambae200.domain.cigarette.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class CigaretteAddRequest {

    @NotBlank(message = "공식이름을 입력해주세요")
    private String official_name;

    @NotNull(message = "간편이름을 입력해주세요")
    private String customized_name;

    private Long id;

    private String file_path_medium;

    private String file_path_large;

    private boolean vertical;

}
