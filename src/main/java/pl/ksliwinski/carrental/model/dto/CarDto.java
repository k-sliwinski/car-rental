package pl.ksliwinski.carrental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotNull
    private Integer year;
    @NotBlank
    private String color;
    @NotNull
    private BigDecimal dailyFee;
    private LocalDate rentDate;
    @NotNull
    @AssertTrue
    private boolean available;
    @NotNull
    private Long companyId;
}
