package project.code_wiki.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class DataTableDto {
    private int draw;
    private int recordsTotal;
    private int recordsFiltered;
    private List<?> data;
}
