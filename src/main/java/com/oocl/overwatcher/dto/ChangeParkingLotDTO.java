package com.oocl.overwatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author LIULE9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeParkingLotDTO {

  private Long userId;

  private String direction;

  private List<Long> parkingLotId;
}
