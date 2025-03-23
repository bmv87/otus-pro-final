package ru.otus.pro.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.otus.pro.dto.BoardCreateDTO;
import ru.otus.pro.dto.BoardListItemDTO;
import ru.otus.pro.dto.BoardListItemExtendedDTO;
import ru.otus.pro.entities.BulletinBoard;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BulletinBoardMapper {
    BulletinBoardMapper INSTANCE = Mappers.getMapper(BulletinBoardMapper.class);

    BulletinBoard mapDtoToEntity(BoardCreateDTO dto);

    void mapDtoToEntity(BoardCreateDTO dto, @MappingTarget BulletinBoard entity);

    @Named("entityToListItemDto")
    BoardListItemDTO mapEntityToListItemDto(BulletinBoard entity);

    @Named("entityToExtendedListItemDto")
    BoardListItemExtendedDTO mapEntityToExtendedListItemDto(BulletinBoard entity);

    @IterableMapping(qualifiedByName = "entityToListItemDto")
    List<BoardListItemDTO> mapEntitiesToListDto(List<BulletinBoard> entities);

    @IterableMapping(qualifiedByName = "entityToExtendedListItemDto")
    List<BoardListItemExtendedDTO> mapEntitiesToExtendedListDto(List<BulletinBoard> entities);
}
