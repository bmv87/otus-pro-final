package ru.otus.pro.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import ru.otus.pro.common.ErrorDTO;
import ru.otus.pro.common.Paginated;
import ru.otus.pro.dto.BoardListItemExtendedDTO;
import ru.otus.pro.dto.BulletinDTO;
import ru.otus.pro.dto.BulletinListItemDTO;
import ru.otus.pro.services.BulletinService;

@RestController
@RequestMapping("/bulletins")
@Tag(name = "Bulletins (Any users)", description = "Methods for working with bulletin boards for the any users")
@RequiredArgsConstructor
public class BulletinsController {

    private final BulletinService bulletinService;

    @GetMapping()
    @Operation(
            summary = "Get bulletin list info by ia"
    )
    public Paginated<BulletinListItemDTO> getList(
            @Parameter(description = "Board id", required = true)
            @RequestParam(name = BulletinListItemDTO.BaseFields.boardId, required = true) Long boardId,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 20,
                    direction = Sort.Direction.DESC,
                    sort = BoardListItemExtendedDTO.Fields.updateDate) Pageable pageable) {

        return bulletinService.getList(boardId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get bulletin info by id",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BulletinDTO.class))
                    ),
                    @ApiResponse(
                            description = "Not found", responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    )
            }
    )
    public BulletinDTO getItem(
            @Parameter(description = "Bulletin id", required = true)
            @PathVariable Long id) {
        return bulletinService.getItem(id);
    }
}
