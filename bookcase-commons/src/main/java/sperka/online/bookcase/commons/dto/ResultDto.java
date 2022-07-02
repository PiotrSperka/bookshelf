package sperka.online.bookcase.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultDto {
    private Boolean success;
    private String errorMessage;

    public static ResultDto Success() {
        return new ResultDto(true, "");
    }
}
