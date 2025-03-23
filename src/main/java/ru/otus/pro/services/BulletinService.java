package ru.otus.pro.services;

import org.springframework.data.domain.Pageable;
import ru.otus.pro.common.Paginated;
import ru.otus.pro.dto.*;

import java.time.LocalDateTime;

public interface BulletinService {
    Paginated<BulletinListItemExtendedDTO> getExtendedList(Long boardId, String title, String content, LocalDateTime createDateStart, LocalDateTime createDateEnd, Pageable pageable);

    long create(String creator, BulletinCreateDTO bulletinCreateDTO);

    void update(Long id, BulletinUpdateDTO bulletinUpdateDTO);

    BulletinExtendedDTO getItem(Long id);

    void delete(Long id);

    boolean exists(Long boardId);

    Paginated<BulletinListItemDTO> getList(long boardId, Pageable pageable);
}
