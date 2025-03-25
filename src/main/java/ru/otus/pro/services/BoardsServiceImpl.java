package ru.otus.pro.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.pro.common.Paginated;
import ru.otus.pro.dto.BoardCreateDTO;
import ru.otus.pro.dto.BoardListItemDTO;
import ru.otus.pro.dto.BoardListItemExtendedDTO;
import ru.otus.pro.entities.BulletinBoard;
import ru.otus.pro.exceptions.NotFoundException;
import ru.otus.pro.exceptions.ServiceException;
import ru.otus.pro.exceptions.ValidationException;
import ru.otus.pro.mappers.BulletinBoardMapper;
import ru.otus.pro.repositories.BulletinBoardRepository;
import ru.otus.pro.specifications.BulletinBoardSpecifications;
import ru.otus.pro.specifications.SpecificationsHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class BoardsServiceImpl implements BoardsService {

    private final BulletinBoardRepository bulletinBoardRepository;
    private final BulletinBoardMapper mapper = BulletinBoardMapper.INSTANCE;
    @Lazy
    private final BulletinService bulletinService;

    @Override
    public Paginated<BoardListItemExtendedDTO> getExtendedList(
            String name,
            String description,
            LocalDateTime createDateStart,
            LocalDateTime createDateEnd,
            Pageable pageable) {

        List<Specification<BulletinBoard>> specList = new ArrayList<>();
        if (StringUtils.hasText(name)) {
            specList.add(BulletinBoardSpecifications.hasName(name));
        }
        if (StringUtils.hasText(description)) {
            specList.add(BulletinBoardSpecifications.hasDescription(description));
        }
        if (createDateStart != null && createDateEnd != null) {
            specList.add(BulletinBoardSpecifications.createDateBetween(createDateStart, createDateEnd));
        } else if (createDateStart != null) {
            specList.add(BulletinBoardSpecifications.createDateGreaterThanOrEqualTo(createDateStart));
        } else if (createDateEnd != null) {
            specList.add(BulletinBoardSpecifications.createDateLessThanOrEqualTo(createDateEnd));
        }

        var listPage = bulletinBoardRepository.findAll(
                SpecificationsHelper.and(specList),
                pageable);

        return new Paginated<>(mapper.mapEntitiesToExtendedListDto(listPage.getContent()), listPage.getTotalElements());
    }

    @Override
    public long create(String creator, BoardCreateDTO boardCreateDTO) {
        if (bulletinBoardRepository.exists(BulletinBoardSpecifications.equalName(boardCreateDTO.getName()))) {
            throw new ValidationException("Bulletin Board name not unique");
        }
        var now = LocalDateTime.now();
        var newBoard = mapper.mapDtoToEntity(boardCreateDTO);
        newBoard.setCreator(creator);
        newBoard.setCreateDate(now);
        newBoard.setUpdateDate(now);
        try {
            return bulletinBoardRepository.save(newBoard).getId();
        } catch (Exception ex) {
            throw new ServiceException("New bulletin board saving exception", ex);
        }
    }

    @Override
    public void update(Long id, BoardCreateDTO boardCreateDTO) {
        var board = bulletinBoardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Board with id %d not found", id)));

        if (!boardCreateDTO.getName().equalsIgnoreCase(board.getName())) {
            List<Specification<BulletinBoard>> specList = new ArrayList<>();
            specList.add(BulletinBoardSpecifications.equalName(boardCreateDTO.getName()));
            specList.add(BulletinBoardSpecifications.notEqualId(id));
            if (bulletinBoardRepository.exists(SpecificationsHelper.and(specList))) {
                throw new ValidationException("Bulletin Board name not unique");
            }
        }

        mapper.mapDtoToEntity(boardCreateDTO, board);
        board.setUpdateDate(LocalDateTime.now());
        try {
            bulletinBoardRepository.save(board);
        } catch (Exception ex) {
            throw new ServiceException("Bulletin board updating exception", ex);
        }
    }

    @Override
    public void delete(Long id) {
        if (bulletinService.exists(id)) {
            throw new ValidationException("Bulletin Board has linked items");
        }
        try {
            bulletinBoardRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ServiceException("Bulletin board deleting exception", ex);
        }
    }

    @Override
    public boolean exists(Long id) {
        return bulletinBoardRepository.existsById(id);
    }

    @Override
    public Paginated<BoardListItemDTO> getList(String name, String description, Pageable pageable) {
        List<Specification<BulletinBoard>> specList = new ArrayList<>();
        if (StringUtils.hasText(name)) {
            specList.add(BulletinBoardSpecifications.hasName(name));
        }
        if (StringUtils.hasText(description)) {
            specList.add(BulletinBoardSpecifications.hasDescription(description));
        }

        var listPage = bulletinBoardRepository.findAll(
                SpecificationsHelper.and(specList),
                pageable);

        return new Paginated<>(mapper.mapEntitiesToListDto(listPage.getContent()), listPage.getTotalElements());
    }
}
