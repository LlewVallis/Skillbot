package org.astropeci.skillbot.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * A paginated response for a list of items.
 */
@Getter
@RequiredArgsConstructor
public class PagedResponse<T> {

    private final List<T> elements;

    private final int pageNumber;
    private final int pageSize;
    private final int totalElements;

    public int getStartingCount() {
        return pageNumber * pageSize;
    }

    public int getEndingCount() {
        return getStartingCount() + elements.size();
    }

    public int getTotalPages() {
        return (int) Math.ceil((double) totalElements / pageSize);
    }
}
