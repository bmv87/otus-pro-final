package ru.otus.pro.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.otus.pro.common.ErrorDTO;
import ru.otus.pro.common.Paginated;
import ru.otus.pro.dto.BoardCreateDTO;
import ru.otus.pro.dto.BoardListItemDTO;
import ru.otus.pro.dto.BoardListItemExtendedDTO;
import ru.otus.pro.services.BoardsService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/boards")
@SecurityRequirement(name = "JWT")
@Tag(name = "Boards (Admin)", description = "Methods for working with bulletin boards for the authorized users")
@RequiredArgsConstructor
public class AdminBoardsController {
    private final BoardsService boardsService;

    @GetMapping()
    @Operation(
            summary = "Get extended board info list"
    )
    public Paginated<BoardListItemExtendedDTO> getList(
            @Parameter(description = "Board name", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = BoardListItemDTO.BaseFields.name, required = false) String name,
            @Parameter(description = "Board description", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = BoardListItemDTO.BaseFields.description, required = false) String description,
            @Parameter(description = "Board create date start", required = false)
            @RequestParam(name = "createDateStart", required = false) LocalDateTime createDateStart,
            @Parameter(description = "Board create date end", required = false)
            @RequestParam(name = "createDateEnd", required = false) LocalDateTime createDateEnd,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 20,
                    direction = Sort.Direction.DESC,
                    sort = BoardListItemExtendedDTO.Fields.updateDate) Pageable pageable) {

        return boardsService.getExtendedList(name, description, createDateStart, createDateEnd, pageable);
    }

    @PostMapping
    @Operation(summary = "Create board",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(name = "Board id"))
                    ),
                    @ApiResponse(
                            description = "Validation error", responseCode = "422",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Service error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    )
            })
    public long create(
            Authentication authentication,
            @Parameter(description = "Board create info", required = true, schema = @Schema(implementation = BoardCreateDTO.class))
            @Valid @RequestBody BoardCreateDTO boardCreateDTO) {
        return boardsService.create(authentication.getName(), boardCreateDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update board",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Validation error", responseCode = "422",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Service error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    )
            })
    public void update(
            @Parameter(description = "Board id", required = true)
            @PathVariable Long id,
            @Parameter(description = "Board update info", required = true, schema = @Schema(implementation = BoardCreateDTO.class))
            @Valid @RequestBody BoardCreateDTO boardCreateDTO) {
        boardsService.update(id, boardCreateDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete board",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Validation error", responseCode = "422",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Service error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    )
            })
    public void delete(
            @Parameter(description = "Board id", required = true)
            @PathVariable Long id) {
        boardsService.delete(id);
    }
}
