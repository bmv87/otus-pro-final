package ru.otus.pro.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.otus.pro.common.Paginated;
import ru.otus.pro.dto.*;
import ru.otus.pro.entities.Bulletin;
import ru.otus.pro.exceptions.NotFoundException;
import ru.otus.pro.exceptions.ServiceException;
import ru.otus.pro.mappers.BulletinMapper;
import ru.otus.pro.repositories.BulletinRepository;
import ru.otus.pro.specifications.BulletinSpecifications;
import ru.otus.pro.specifications.SpecificationsHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class BulletinServiceImpl implements BulletinService {

    private final BulletinRepository bulletinRepository;
    private final BulletinMapper mapper = BulletinMapper.INSTANCE;
    @Lazy
    private final BoardsService boardsService;

    @Override
    public Paginated<BulletinListItemExtendedDTO> getExtendedList(
            Long boardId,
            String title,
            String content,
            LocalDateTime createDateStart,
            LocalDateTime createDateEnd,
            Pageable pageable) {
        List<Specification<Bulletin>> specList = new ArrayList<>();
        if (StringUtils.hasText(title)) {
            specList.add(BulletinSpecifications.hasTitle(title));
        }
        if (StringUtils.hasText(content)) {
            specList.add(BulletinSpecifications.hasContent(content));
        }
        if (createDateStart != null && createDateEnd != null) {
            specList.add(BulletinSpecifications.createDateBetween(createDateStart, createDateEnd));
        } else if (createDateStart != null) {
            specList.add(BulletinSpecifications.createDateGreaterThanOrEqualTo(createDateStart));
        } else if (createDateEnd != null) {
            specList.add(BulletinSpecifications.createDateLessThanOrEqualTo(createDateEnd));
        }

        var listPage = bulletinRepository.findAll(
                SpecificationsHelper.and(specList),
                pageable);

        return new Paginated<>(mapper.mapEntitiesToExtendedListDto(listPage.getContent()), listPage.getTotalElements());
    }

    @Override
    public long create(String creator, BulletinCreateDTO bulletinCreateDTO) {
        if (!boardsService.exists(bulletinCreateDTO.getBoardId())) {
            throw new NotFoundException(String.format("Board with id %d not found", bulletinCreateDTO.getBoardId()));
        }
        var now = LocalDateTime.now();
        var newBulletin = mapper.mapDtoToEntity(bulletinCreateDTO);
        newBulletin.setCreator(creator);
        newBulletin.setCreateDate(now);
        newBulletin.setUpdateDate(now);
        try {
            return bulletinRepository.save(newBulletin).getId();
        } catch (Exception ex) {
            throw new ServiceException("New bulletin saving exception", ex);
        }
    }

    @Override
    public void update(Long id, BulletinUpdateDTO bulletinUpdateDTO) {
        var bulletin = bulletinRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Bulletin with id %d not found", id)));

        mapper.mapDtoToEntity(bulletinUpdateDTO, bulletin);
        bulletin.setUpdateDate(LocalDateTime.now());
        try {
            bulletinRepository.save(bulletin);
        } catch (Exception ex) {
            throw new ServiceException("Bulletin updating exception", ex);
        }
    }

    @Override
    public BulletinExtendedDTO getItem(Long id) {
        var bulletin = bulletinRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Bulletin with id %d not found", id)));
        return mapper.mapEntityToExtendedItemDto(bulletin);
    }

    @Override
    public void delete(Long id) {
        try {
            bulletinRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ServiceException("Bulletin deleting exception", ex);
        }
    }

    @Override
    public boolean exists(Long boardId) {
        return bulletinRepository.exists(BulletinSpecifications.hasBoardId(boardId));
    }

    @Override
    public Paginated<BulletinListItemDTO> getList(long boardId, Pageable pageable) {
        var listPage = bulletinRepository.findAll(
                Specification.where(BulletinSpecifications.hasBoardId(boardId))
                        .and(BulletinSpecifications.isActive(LocalDateTime.now())),
                pageable);

        return new Paginated<>(BulletinMapper.INSTANCE.mapEntitiesToListDto(listPage.getContent()), listPage.getTotalElements());
    }
}
