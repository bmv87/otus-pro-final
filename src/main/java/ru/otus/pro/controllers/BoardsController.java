package ru.otus.pro.controllers;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.pro.common.Paginated;
import ru.otus.pro.dto.BoardListItemDTO;
import ru.otus.pro.services.BoardsService;

@RestController
@RequestMapping("/boards")
@Tag(name = "Boards (Any users)", description = "Methods for working with bulletin boards for the any users")
@RequiredArgsConstructor
public class BoardsController {

    private final BoardsService boardsService;

    @GetMapping()
    @Operation(
            summary = "Get board list info by ia"
    )
    public Paginated<BoardListItemDTO> getList(
            @Parameter(description = "Board name", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = BoardListItemDTO.BaseFields.name, required = false) String name,
            @Parameter(description = "Board description", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = BoardListItemDTO.BaseFields.description, required = false) String description,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 20,
                    direction = Sort.Direction.ASC,
                    sort = BoardListItemDTO.BaseFields.name) Pageable pageable) {

        return boardsService.getList(name, description, pageable);
    }
}
