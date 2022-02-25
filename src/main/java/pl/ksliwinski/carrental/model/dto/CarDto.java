package pl.ksliwinski.carrental.model.dto;

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
    private Long id;
    @NotBlank
    private String brand;
    @NotBlank
    private String model;
    @NotBlank
    private Integer year;
    @NotBlank
    private String color;
    @NotBlank
    private BigDecimal dailyFee;
    private LocalDate rentDate;
    @NotNull
    @AssertTrue
    private boolean available;
    @NotBlank
    private Long companyId;
}