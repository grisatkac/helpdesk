package by.tms.tkach.helpdesk.dto.department;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
public class PagingDTO<T> {
    Integer currentPage = 0;
    Integer pageSize = 5;
    Integer totalPages = 2;
    String sortBy;
    String sortDir = "desc";
    String reverseSortDir = "asc";
    List<T> items;
    Integer totalItems;
    Long authUserId;
    String currentURI;
}
