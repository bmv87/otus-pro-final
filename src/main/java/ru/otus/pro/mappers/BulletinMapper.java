package ru.otus.pro.mappers;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.otus.pro.dto.*;
import ru.otus.pro.entities.Bulletin;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BulletinMapper {
    BulletinMapper INSTANCE = Mappers.getMapper(BulletinMapper.class);

    Bulletin mapDtoToEntity(BulletinCreateDTO dto);

    void mapDtoToEntity(BulletinUpdateDTO dto, @MappingTarget Bulletin entity);

    @Mapping(target = "boardName", source = "board.name")
    BulletinDTO mapEntityToItemDto(Bulletin entity);

    @Mapping(target = "boardName", source = "board.name")
    BulletinExtendedDTO mapEntityToExtendedItemDto(Bulletin entity);

    @Named("entityToListItemDto")
    @Mapping(target = "boardName", source = "board.name")
    BulletinListItemDTO mapEntityToListItemDto(Bulletin entity);

    @Named("entityToExtendedListItemDto")
    @Mapping(target = "boardName", source = "board.name")
    BulletinListItemExtendedDTO mapEntityToExtendedListItemDto(Bulletin entity);

    @IterableMapping(qualifiedByName = "entityToListItemDto")
    List<BulletinListItemDTO> mapEntitiesToListDto(List<Bulletin> entities);

    @IterableMapping(qualifiedByName = "entityToExtendedListItemDto")
    List<BulletinListItemExtendedDTO> mapEntitiesToExtendedListDto(List<Bulletin> entities);
}
