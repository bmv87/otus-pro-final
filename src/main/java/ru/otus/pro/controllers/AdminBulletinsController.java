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
import ru.otus.pro.dto.*;
import ru.otus.pro.services.BulletinService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/bulletins")
@SecurityRequirement(name = "JWT")
@Tag(name = "Bulletins (Admin)", description = "Methods for working with bulletin boards for the authorized users")
@RequiredArgsConstructor
public class AdminBulletinsController {

    private final BulletinService bulletinService;

    @GetMapping()
    @Operation(
            summary = "Get extended bulletins info list"
    )
    public Paginated<BulletinListItemExtendedDTO> getList(
            @Parameter(description = "Board id", required = false)
            @RequestParam(name = BulletinListItemExtendedDTO.BaseFields.boardId, required = false) Long boardId,
            @Parameter(description = "Bulletin title", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = BulletinListItemExtendedDTO.BaseFields.title, required = false) String title,
            @Parameter(description = "Bulletin content", required = false, schema = @Schema(type = "string"))
            @RequestParam(name = "content", required = false) String content,
            @Parameter(description = "Bulletin create date start", required = false)
            @RequestParam(name = "createDateStart", required = false) LocalDateTime createDateStart,
            @Parameter(description = "Bulletin create date end", required = false)
            @RequestParam(name = "createDateEnd", required = false) LocalDateTime createDateEnd,
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 20,
                    direction = Sort.Direction.DESC,
                    sort = BoardListItemExtendedDTO.Fields.updateDate) Pageable pageable) {

        return bulletinService.getExtendedList(boardId, title, content, createDateStart, createDateEnd, pageable);
    }


    @PostMapping
    @Operation(summary = "Create bulletin",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(name = "Bulletin id"))
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
            @Parameter(description = "Bulletin create info", required = true, schema = @Schema(implementation = BulletinCreateDTO.class))
            @Valid @RequestBody BulletinCreateDTO bulletinCreateDTO) {
        return bulletinService.create(authentication.getName(), bulletinCreateDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update bulletin",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(name = "Bulletin id"))
                    ),
                    @ApiResponse(
                            description = "Not found", responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
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
            @Parameter(description = "Bulletin id", required = true)
            @PathVariable Long id,
            @Parameter(description = "Bulletin update info", required = true, schema = @Schema(implementation = BulletinUpdateDTO.class))
            @Valid @RequestBody BulletinUpdateDTO bulletinUpdateDTO) {
        bulletinService.update(id, bulletinUpdateDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get bulletin",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BulletinExtendedDTO.class))
                    ),
                    @ApiResponse(
                            description = "Not found", responseCode = "404",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    ),
                    @ApiResponse(
                            description = "Service error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    )
            })
    public BulletinExtendedDTO getItem(
            @Parameter(description = "Bulletin id", required = true)
            @PathVariable Long id) {
        return bulletinService.getItem(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete bulletin",
            responses = {
                    @ApiResponse(
                            description = "Success response", responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Service error", responseCode = "500",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorDTO.class))
                    )
            })
    public void delete(
            @Parameter(description = "Bulletin id", required = true)
            @PathVariable Long id) {
        bulletinService.delete(id);
    }
}
