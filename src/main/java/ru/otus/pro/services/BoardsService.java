package ru.otus.pro.services;

import org.springframework.data.domain.Pageable;
import ru.otus.pro.common.Paginated;
import ru.otus.pro.dto.BoardCreateDTO;
import ru.otus.pro.dto.BoardListItemDTO;
import ru.otus.pro.dto.BoardListItemExtendedDTO;

import java.time.LocalDateTime;


public interface BoardsService {
    Paginated<BoardListItemExtendedDTO> getExtendedList(String name, String description, LocalDateTime createDateStart, LocalDateTime createDateEnd, Pageable pageable);

    long create(String creator, BoardCreateDTO boardCreateDTO);

    void update(Long id, BoardCreateDTO boardCreateDTO);

    void delete(Long id);

    boolean exists(Long id);

    Paginated<BoardListItemDTO> getList(String name, String description, Pageable pageable);
}
