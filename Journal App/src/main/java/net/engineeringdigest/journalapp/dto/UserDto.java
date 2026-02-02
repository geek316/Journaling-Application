package net.engineeringdigest.journalapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Schema(description = "User's Username")
    private String userName;
    private String password;
    private String email;
    private boolean sentimentAnalysis;
}
