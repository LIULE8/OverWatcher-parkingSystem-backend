package com.oocl.overwatcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author LIULE9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO implements Serializable {

  @JsonProperty("roles")
  private String roleName;

  @JsonProperty("token")
  private String token;

  @JsonProperty("id")
  private String userId;

  @JsonProperty("username")
  private String username;

  @JsonProperty("msg")
  private String message;


  public LoginDTO(String message) {
    this.message = message;
  }
}
