package com.kbds.serviceapi.apis.entity.key;

import java.io.Serializable;
import javax.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GwServiceAppMappingKey implements Serializable {

  private static final long serialVersionUID = -711685434231082611L;

  @Column(name = "SERVICE_ID")
  private Long serviceId;

  @Column(name = "APP_ID")
  private Long appId;

}
